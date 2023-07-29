package com.aqutheseal.celestisynth.events;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.LivingMixinSupport;
import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.entities.SkillCastPoltergeistWard;
import com.aqutheseal.celestisynth.item.weapons.AquafloraItem;
import com.aqutheseal.celestisynth.item.weapons.BreezebreakerItem;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID)
public class CSUtilityEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemR = entity.getMainHandItem();
        ItemStack itemL = entity.getOffhandItem();
        if (itemR.getItem() instanceof CSWeapon cs && !(itemL.getItem() instanceof CSWeapon)) {
            cs.onPlayerHurt(event, itemR, itemL);
        }
        if (itemL.getItem() instanceof CSWeapon cs && !(itemR.getItem() instanceof CSWeapon)) {
            cs.onPlayerHurt(event, itemR, itemL);
        }
        if (itemR.getItem() instanceof CSWeapon cs && itemL.getItem() instanceof CSWeapon) {
            cs.onPlayerHurt(event, itemR, itemL);
        }
    }



    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CSWeapon.disableRunningWeapon(player);
        }
        if (event.getEntity() instanceof LivingMixinSupport lms) {
            if (lms.getPhantomTagger() != null) {
                SkillCastPoltergeistWard projectile = CSEntityRegistry.POLTERGEIST_WARD.get().create(event.getEntity().getLevel());
                projectile.setOwnerUuid(lms.getPhantomTagger().getUUID());
                projectile.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
                event.getEntity().getLevel().addFreshEntity(projectile);
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
                player.playSound(CSSoundRegistry.CS_STEP.get());
                if (itemR instanceof CSWeapon wp) {
                    wp.sendExpandingParticles(entity.level, ParticleTypes.SMOKE, player.blockPosition(), 75, 0.35F);
                } else {
                    CSWeapon wp = (CSWeapon) itemL;
                    wp.sendExpandingParticles(entity.level, ParticleTypes.SMOKE, player.blockPosition(), 75, 0.35F);
                }
                player.setDeltaMovement(entity.getDeltaMovement().multiply(3, 2.3, 3));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerAttacked(LivingAttackEvent event) {
        checkAndCancel(event, event.getEntity().getMainHandItem());
        checkAndCancel(event, event.getEntity().getOffhandItem());
    }

    private static void checkAndCancel(LivingAttackEvent event, ItemStack itemStack) {
        CompoundTag tagElement = itemStack.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
        if (tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraItem.ATTACK_BLOOMING)) {
            event.setCanceled(true);
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
