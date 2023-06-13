package com.aqutheseal.celestisynth.events;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.item.BreezebreakerItem;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID)
public class CSUtilityEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemR = entity.getMainHandItem();
        ItemStack itemL = entity.getOffhandItem();
        if (itemR != ItemStack.EMPTY) {
            if (itemR.getItem() instanceof CSWeapon cs) {
                cs.onPlayerHurt(event, itemR, itemL);
            }
        }
        if (itemL != ItemStack.EMPTY) {
            if (itemL.getItem() instanceof CSWeapon cs) {
                cs.onPlayerHurt(event, itemR, itemL);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        Item itemR = entity.getMainHandItem().getItem();
        Item itemL = entity.getOffhandItem().getItem();
        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem) {
            if (entity instanceof Player player) {
                if (player.getLevel().isClientSide()) {
                    AnimationManager.playAnimation(AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_JUMP, true);
                }
                player.setDeltaMovement(entity.getDeltaMovement().multiply(3, 2.3, 3));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerHitGround(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Item itemR = entity.getMainHandItem().getItem();
        Item itemL = entity.getOffhandItem().getItem();

        if (entity instanceof Player player) {
            if (player.getLevel().isClientSide()) {
                AnimationManager.playAnimation(AnimationManager.AnimationsList.CLEAR, true);
            }
        }

        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem) {
            event.setCanceled(true);
        }
    }
}
