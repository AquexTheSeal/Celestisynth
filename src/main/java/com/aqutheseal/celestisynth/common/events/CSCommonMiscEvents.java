package com.aqutheseal.celestisynth.common.events;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.api.mixin.LivingMixinSupport;
import com.aqutheseal.celestisynth.api.mixin.PlayerMixinSupport;
import com.aqutheseal.celestisynth.common.attack.aquaflora.AquafloraSlashFrenzyAttack;
import com.aqutheseal.celestisynth.common.capabilities.EntityFrostboundProvider;
import com.aqutheseal.celestisynth.common.entity.skill.SkillCastPoltergeistWard;
import com.aqutheseal.celestisynth.common.item.weapons.BreezebreakerItem;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import com.google.common.collect.Streams;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.stream.Collectors;

public class CSCommonMiscEvents {

    @SubscribeEvent
    public static void onLivingTickEvent(LivingEvent.LivingTickEvent event) {
        if (event.getEntity() instanceof LivingMixinSupport lms) {
            if (lms.getQuasarImbued() != null) {
                double radius = 0.5 + event.getEntity().getBbWidth();
                double speed = 0.1;
                double offX = radius * Math.sin(speed * event.getEntity().tickCount);
                double offY = -Math.sin(event.getEntity().tickCount) * 0.2;
                double offZ = radius * Math.cos(speed * event.getEntity().tickCount);
                ParticleUtil.sendParticle(event.getEntity().level, CSParticleTypes.RAINFALL_ENERGY_SMALL.get(),
                        event.getEntity().getX() + offX, event.getEntity().getY() + offY + 1, event.getEntity().getZ() + offZ);
            }
        }

        event.getEntity().getCapability(EntityFrostboundProvider.ENTITY_FROSTBOUND).ifPresent(frostbound -> {
            if (frostbound.getFrostbound() > 0) {
                if (event.getEntity().tickCount % 5 == 0) {
                    event.getEntity().setTicksFrozen(2);
                }
                event.getEntity().addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 2, 4));

                double radius = 0.8 + event.getEntity().getBbWidth();
                double speed = 0.2;
                double offX = radius * Math.sin(speed * event.getEntity().tickCount);
                double offY = 1;
                double offZ = radius * Math.cos(speed * event.getEntity().tickCount);
                ParticleUtil.sendParticle(event.getEntity().level, ParticleTypes.SNOWFLAKE,
                        event.getEntity().getX() + offX, event.getEntity().getY() + offY, event.getEntity().getZ() + offZ);
            }

            frostbound.decreaseFrostbound();
        });
    }

    @SubscribeEvent
    public static void onLivingAttackEvent(LivingAttackEvent event) {
        checkAndCancelAttack(event, event.getEntity().getMainHandItem());
        checkAndCancelAttack(event, event.getEntity().getOffhandItem());
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemR = entity.getMainHandItem();
        ItemStack itemL = entity.getOffhandItem();

        if (itemR.getItem() instanceof CSWeapon cs && !(itemL.getItem() instanceof CSWeapon)) cs.onPlayerHurt(event, itemR, itemL);
        if (itemL.getItem() instanceof CSWeapon cs && !(itemR.getItem() instanceof CSWeapon)) cs.onPlayerHurt(event, itemR, itemL);
        if (itemR.getItem() instanceof CSWeapon cs && itemL.getItem() instanceof CSWeapon) cs.onPlayerHurt(event, itemR, itemL);
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CSWeaponUtil.disableRunningWeapon(player);
        }
        if (event.getEntity() instanceof LivingMixinSupport lms && lms.getPhantomTagger() != null) {
            SkillCastPoltergeistWard poltergeistProjectile = CSEntityTypes.POLTERGEIST_WARD.get().create(event.getEntity().getLevel());

            poltergeistProjectile.setOwnerUuid(lms.getPhantomTagger().getUUID());
            poltergeistProjectile.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
            event.getEntity().getLevel().addFreshEntity(poltergeistProjectile);
        }
    }

    @SubscribeEvent
    public static void onPlayerCopy(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            CSWeaponUtil.disableRunningWeapon(event.getEntity());
        }
    }

    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        Item itemR = entity.getMainHandItem().getItem();
        Item itemL = entity.getOffhandItem().getItem();

        if (entity instanceof Player player && player.getLevel().isClientSide()) AnimationManager.playAnimation(AnimationManager.AnimationsList.CLEAR, true);
        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem)  event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        Item itemR = entity.getMainHandItem().getItem();
        Item itemL = entity.getOffhandItem().getItem();

        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem) {
            if (entity instanceof Player player) {
                if (player.getLevel().isClientSide()) AnimationManager.playAnimation(AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_JUMP, true);

                player.playSound(CSSoundEvents.CS_HOP.get());

                if (itemR instanceof CSWeapon wp) wp.sendExpandingParticles(entity.level, ParticleTypes.SMOKE, player.blockPosition(), 75, 0.35F);
                else {
                    CSWeapon wp = (CSWeapon) itemL;
                    wp.sendExpandingParticles(entity.level, ParticleTypes.SMOKE, player.blockPosition(), 75, 0.35F);
                }

                player.setDeltaMovement(entity.getDeltaMovement().multiply(2.75, 2.25, 2.75));
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedInEvent(PlayerEvent.PlayerLoggedInEvent event) {
        Inventory inv = event.getEntity().getInventory();
        ObjectArrayList<ItemStack> invCompartments = Streams.concat(inv.items.stream(), inv.armor.stream(), inv.offhand.stream()).collect(Collectors.toCollection(ObjectArrayList::new));

        for (ItemStack stack : invCompartments) {
            if (!stack.isEmpty() && stack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT) != null) stack.getTag().remove(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
        }
    }

    private static void checkAndCancelAttack(LivingAttackEvent event, ItemStack itemStack) {
        CompoundTag tagElement = itemStack.getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);

        if (tagElement != null && tagElement.getBoolean(CSWeapon.ANIMATION_BEGUN_KEY) && tagElement.getBoolean(AquafloraSlashFrenzyAttack.ATTACK_ONGOING)) {
            event.setCanceled(true);
        }
    }
}
