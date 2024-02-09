package com.aqutheseal.celestisynth.client.events;

import com.aqutheseal.celestisynth.api.item.CSArmorItem;
import com.aqutheseal.celestisynth.api.item.CSArmorProperties;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.api.mixin.PlayerMixinSupport;
import com.aqutheseal.celestisynth.client.renderers.entity.layer.FrostboundGeoLayer;
import com.aqutheseal.celestisynth.common.attack.aquaflora.AquafloraSlashFrenzyAttack;
import com.aqutheseal.celestisynth.common.item.weapons.AquafloraItem;
import com.aqutheseal.celestisynth.common.registry.CSRarityTypes;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import software.bernie.geckolib.event.GeoRenderEvent;

import java.util.List;
import java.util.ListIterator;

public class CSClientMiscEvents {

    @SubscribeEvent
    public static void onGeoEntityRender(GeoRenderEvent.Entity.CompileRenderLayers event) {
        event.addLayer(new FrostboundGeoLayer<>(event.getRenderer()));
    }

    @SubscribeEvent
    public static void onScreenRender(ScreenEvent.Opening event) {
        if (Minecraft.getInstance().player != null) {
            ItemStack itemR = Minecraft.getInstance().player.getMainHandItem();
            ItemStack itemL = Minecraft.getInstance().player.getOffhandItem();

            if (itemR.getItem() instanceof CSWeapon) {
                if (itemR.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT) != null && itemR.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
                    event.setCanceled(true);
                }
            }
            if (itemL.getItem() instanceof CSWeapon) {
                if (itemL.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT) != null && itemL.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT).getBoolean(CSWeapon.ANIMATION_BEGUN_KEY)) {
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

    // Credits to BobMowzie for camera math
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

        if (duration > 0 && !Minecraft.getInstance().isPaused() && mc.level.isClientSide()) {
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

    @SubscribeEvent
    public static void onToolTipComponent(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        String name = ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath();

        if (!stack.isEmpty() && stack.getItem() instanceof CSArmorItem armor) {
            List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
            List<Either<FormattedText, TooltipComponent>> elementsToAdd = new ObjectArrayList<>();
            CSArmorProperties properties = armor.getArmorProperties();

            if (properties.isStunImmune()) elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.armor_stun_immune").withStyle(ChatFormatting.LIGHT_PURPLE)));
            if (properties.getDamageReflectionPercent() > 0) elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.armor_damage_reflection_percent", properties.getDamageReflectionPercent()).withStyle(ChatFormatting.LIGHT_PURPLE)));
            if (properties.getDamageReflectionAddition() > 0) elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.armor_damage_reflection_addition", properties.getDamageReflectionAddition()).withStyle(ChatFormatting.LIGHT_PURPLE)));
            if (properties.getMobEffectDurationMultiplier() > 0) elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.armor_mob_effect_duration_multiplier", properties.getMobEffectDurationMultiplier()).withStyle(ChatFormatting.LIGHT_PURPLE)));
            if (properties.getSkillDamageMultiplier() > 0) elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.armor_skill_damage_multiplier", properties.getSkillDamageMultiplier()).withStyle(ChatFormatting.LIGHT_PURPLE)));

            ListIterator<Either<FormattedText, TooltipComponent>> iterator = elementsToAdd.listIterator(elementsToAdd.size());

            while (iterator.hasPrevious()) elements.add(1, iterator.previous());
        }

        if (!stack.isEmpty() && stack.getItem() instanceof CSWeapon cs) {
            List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
            List<Either<FormattedText, TooltipComponent>> elementsToAdd = new ObjectArrayList<>();

            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.celestial_tier").withStyle(ChatFormatting.AQUA).withStyle(ChatFormatting.BOLD)));
            elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.shift_notice").withStyle(ChatFormatting.GREEN)));
            addBorders(elementsToAdd);

            if (cs.getPassiveAmount() > 0) {
                elementsToAdd.add(Either.left(Component.translatable("item.celestisynth.passive_notice").withStyle(ChatFormatting.GOLD)));

                if (Screen.hasShiftDown() || Screen.hasControlDown()) elementsToAdd.add(Either.left(Component.literal(" ")));

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

            if (Screen.hasShiftDown() || Screen.hasControlDown()) elementsToAdd.add(Either.left(Component.literal(" ")));

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

            while (iterator.hasPrevious()) elements.add(1, iterator.previous());
        }
    }

    private static void checkAndSetAngle(ViewportEvent.ComputeCameraAngles event, ItemStack itemStack) {
        if (itemStack.getItem() instanceof AquafloraItem) {
            CompoundTag tagElement = itemStack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
            if (tagElement != null && tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraSlashFrenzyAttack.ATTACK_ONGOING)) event.setPitch(90);
        }
    }

    private static void checkAndSetFOV(ViewportEvent.ComputeFov event, ItemStack itemStack) {
        CompoundTag tagElement = itemStack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);

        if (tagElement != null && itemStack.getItem() instanceof AquafloraItem aq) {
            if (tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraSlashFrenzyAttack.ATTACK_ONGOING)) event.setFOV(140);
        }
    }

    private static void addBorders(List<Either<FormattedText, TooltipComponent>> list) {
        boolean shouldExpand = Screen.hasShiftDown() || Screen.hasControlDown();
        MutableComponent border = Component.literal(shouldExpand ? "[ -------------------- o -------------------- ]" : "[ ------------ o ------------ ]");
        MutableComponent edge = Component.literal(" ");

        list.add(Either.left(edge));
        list.add(Either.left(border.withStyle(shouldExpand ? ChatFormatting.GREEN : ChatFormatting.GRAY)));
        list.add(Either.left(edge));
    }
}
