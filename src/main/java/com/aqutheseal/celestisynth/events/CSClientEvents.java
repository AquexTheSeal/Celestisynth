package com.aqutheseal.celestisynth.events;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.LivingMixinSupport;
import com.aqutheseal.celestisynth.PlayerMixinSupport;
import com.aqutheseal.celestisynth.item.helpers.CSRarityTypes;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.item.weapons.AquafloraItem;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Either;
import com.mojang.math.Vector3f;
import net.mehvahdjukaar.moonlight.core.mixins.ThirdPersonRendererMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CSClientEvents {

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Opening event) {
        if (Minecraft.getInstance().player != null) {
            ItemStack itemR = Minecraft.getInstance().player.getMainHandItem();
            ItemStack itemL = Minecraft.getInstance().player.getOffhandItem();
            if (itemR.getItem() instanceof CSWeapon) {
                if (itemR.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                    event.setCanceled(true);
                }
            }
            if (itemL.getItem() instanceof CSWeapon) {
                if (itemL.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onTooltipColor(RenderTooltipEvent.Color event) {
        int argb = (0xFF << 24);
        if (event.getItemStack().getRarity() == CSRarityTypes.CELESTIAL) {
            event.setBackgroundStart(argb + 0x0E1F41);
            event.setBackgroundEnd(argb + 0x0E0B29);
            event.setBorderStart(argb + 0xEed042);
            event.setBorderEnd(argb + 0xBF512A);
        }
    }

    // Credit: BobMowzie
    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;
        PlayerMixinSupport supportedPlayer = (PlayerMixinSupport) player;

        if (!mc.options.getCameraType().isFirstPerson()) {
            checkAndSetAngle(event, player.getOffhandItem());
            checkAndSetAngle(event, player.getMainHandItem());
        }

        float delta = Minecraft.getInstance().getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        float intensity = supportedPlayer.getScreenShakeIntensity();
        float duration = supportedPlayer.getScreenShakeDuration();
        if (duration > 0 && !Minecraft.getInstance().isPaused() && player.level.isClientSide()) {
            event.setPitch((float) (event.getPitch() + intensity * Math.cos(ticksExistedDelta * 3 + 2) * 25));
            event.setYaw((float) (event.getYaw() + intensity * Math.cos(ticksExistedDelta * 5 + 1) * 25));
            event.setRoll((float) (event.getRoll() + intensity * Math.cos(ticksExistedDelta * 4) * 25));
        }
    }

    @SubscribeEvent
    public static void onCameraZoom(ViewportEvent.ComputeFov event) {
        Minecraft mc = Minecraft.getInstance();
        if (!mc.options.getCameraType().isFirstPerson()) {
            checkAndSetFOV(event, mc.player.getOffhandItem());
            checkAndSetFOV(event, mc.player.getMainHandItem());
        }
    }

    private static void checkAndSetAngle(ViewportEvent.ComputeCameraAngles event, ItemStack itemStack) {
        if (itemStack.getItem() instanceof AquafloraItem) {
            CompoundTag tagElement = itemStack.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
            if (tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraItem.ATTACK_BLOOMING)) {
                event.setPitch(90);
            }
        }
    }

    private static void checkAndSetFOV(ViewportEvent.ComputeFov event, ItemStack itemStack) {
        if (itemStack.getItem() instanceof AquafloraItem) {
            CompoundTag tagElement = itemStack.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
            if (tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraItem.ATTACK_BLOOMING)) {
                event.setFOV(150);
            }
        }
    }

    @SubscribeEvent
    public static void onToolTipComponent(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();
        if(!stack.isEmpty() && stack.getItem() instanceof CSWeapon cs) {
            List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
            List<Either<FormattedText, TooltipComponent>> elementsToAdd = new ArrayList<>();
            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.celestial_tier").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD)));
            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.shift_notice").withStyle(ChatFormatting.GREEN)));
            addBorders(elementsToAdd);
            if (cs.hasPassive()) {
                elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.passive_notice").withStyle(ChatFormatting.GOLD)));
                if (Screen.hasShiftDown() || Screen.hasControlDown()) {
                    elementsToAdd.add(Either.left(Component.literal(" ")));
                }
                for (int i = 1; i < cs.getPassiveAmount() + 1; i++) {
                    elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".passive_" + i).withStyle(ChatFormatting.LIGHT_PURPLE)));
                    if (Screen.hasShiftDown() || Screen.hasControlDown()) {
                        elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".passive_desc_" + i).withStyle(ChatFormatting.GRAY)));
                        elementsToAdd.add(Either.left(Component.literal(" ")));
                    }
                }
                addBorders(elementsToAdd);
            }
            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.skill_notice").withStyle(ChatFormatting.GOLD)));
            if (Screen.hasShiftDown() || Screen.hasControlDown()) {
                elementsToAdd.add(Either.left(Component.literal(" ")));
            }
            for (int i = 1; i < cs.getSkillsAmount() + 1; i++) {
                elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".skill_" + i).withStyle(ChatFormatting.LIGHT_PURPLE)));
                if (Screen.hasShiftDown() || Screen.hasControlDown()) {
                    elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".condition_" + i).withStyle(ChatFormatting.RED)));
                    elementsToAdd.add(Either.left(Component.translatable("item.celestisynth." + name + ".desc_" + i).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC)));
                    elementsToAdd.add(Either.left(Component.literal(" ")));
                }
            }
            addBorders(elementsToAdd);
            ListIterator<Either<FormattedText, TooltipComponent>> iterator = elementsToAdd.listIterator(elementsToAdd.size());
            while (iterator.hasPrevious()) {
                elements.add(1, iterator.previous());
            }
        }
    }

    public static void addBorders(List<Either<FormattedText, TooltipComponent>> list) {
        boolean shouldExpand = Screen.hasShiftDown() || Screen.hasControlDown();
        MutableComponent border = Component.literal(shouldExpand ? "[ -------------------- o -------------------- ]" : "[ ------------ o ------------ ]");
        MutableComponent edge = Component.literal(" ");

        list.add(Either.left(edge));
        list.add(Either.left(border.withStyle(shouldExpand ? ChatFormatting.GREEN : ChatFormatting.GRAY)));
        list.add(Either.left(edge));
    }
}
