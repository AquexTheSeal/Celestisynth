package com.aqutheseal.celestisynth.common.events;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSArmorItem;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.common.entity.projectile.SolarisBomb;
import com.aqutheseal.celestisynth.common.entity.skill.SkillCastPoltergeistWard;
import com.aqutheseal.celestisynth.common.item.weapons.BreezebreakerItem;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSTags;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import com.google.common.collect.Streams;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.stream.Collectors;

public class CSCommonMiscEvents {

    @SubscribeEvent
    public static void onLivingTickEvent(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        CSEntityCapabilityProvider.get(entity).ifPresent(data -> {
            if (data.getQuasarImbueSource() != null) {
                double radius = 0.5 + entity.getBbWidth();
                double speed = 0.1;
                double offX = radius * Math.sin(speed * entity.tickCount);
                double offY = -Math.sin(entity.tickCount) * 0.2;
                double offZ = radius * Math.cos(speed * entity.tickCount);
                ParticleUtil.sendParticle(entity.level(), CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), entity.getX() + offX, entity.getY() + offY + 1, entity.getZ() + offZ);
            }

            if (data.getQuasarImbueTime() <= 0) {
                data.clearQuasarImbue();
            } else {
                data.decreaseQuasarImbueTime();
            }

            if (data.getPhantomTagTime() <= 0) {
                data.clearPhantomTag();
            } else {
                data.decreasePhantomTagTime();
            }

            if (data.getTrueInvisibility() > 0) {
                data.decreaseTrueInvisibility();
            }

            if (data.getFrostbound() > 0) {
                if (entity.tickCount % 10 == 0) {
                    entity.setTicksFrozen(10);
                }
                entity.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 2, 4));
                entity.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.WEAKNESS, 2, 1));
                entity.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.DIG_SLOWDOWN, 2, 4));
                double radius = 0.8 + entity.getBbWidth();
                double speed = 0.2;
                double offX = radius * Math.sin(speed * entity.tickCount);
                double offY = 1;
                double offZ = radius * Math.cos(speed * entity.tickCount);
                ParticleUtil.sendParticle(entity.level(), ParticleTypes.SNOWFLAKE, entity.getX() + offX, entity.getY() + offY, entity.getZ() + offZ);

                if (entity.getType().is(CSTags.EntityTypes.FROSTBOUND_SENSITIVE) && entity.tickCount % 5 == 0) {
                    entity.hurt(entity.damageSources().freeze(), 2);
                }

                if (entity.isOnFire()) {
                    if (entity.tickCount % 10 == 0) {
                        entity.playSound(SoundEvents.FIRE_EXTINGUISH);
                    }
                    data.decreaseFrostbound(5);
                } else {
                    data.decreaseFrostbound();
                }
            }
        });
    }

    @SubscribeEvent
    public static void onLivingHurtEvent(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        ItemStack itemR = entity.getMainHandItem();
        ItemStack itemL = entity.getOffhandItem();
        if (itemR.getItem() instanceof CSWeapon cs && itemL.getItem() instanceof CSWeapon cs2) {
            cs.onPlayerHurt(event, itemR);
            cs2.onPlayerHurt(event, itemL);
        } else if (itemR.getItem() instanceof CSWeapon cs) {
            cs.onPlayerHurt(event, itemR);
        } else if (itemL.getItem() instanceof CSWeapon cs) {
            cs.onPlayerHurt(event, itemR);
        }

        for (ArmorItem.Type armorType : ArmorItem.Type.values()) {
            if (entity.getItemBySlot(armorType.getSlot()).getItem() instanceof CSArmorItem armor) {
                armor.hurtWearer(event);
            }
        }

        SolarisBomb.handleHurtEvent(event);
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CSWeaponUtil.disableRunningWeapon(player);
        }

        CSEntityCapabilityProvider.get(event.getEntity()).ifPresent(data -> {
            if (data.getPhantomTagSource() instanceof Player player) {
                SkillCastPoltergeistWard poltergeistProjectile = CSEntityTypes.POLTERGEIST_WARD.get().create(event.getEntity().level());
                poltergeistProjectile.setOwnerUuid(player.getUUID());
                poltergeistProjectile.moveTo(event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ());
                event.getEntity().level().addFreshEntity(poltergeistProjectile);
            }
        });
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

        if (entity instanceof Player player && player.level().isClientSide()) AnimationManager.playAnimation(AnimationManager.AnimationsList.CLEAR, true);
        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem)  event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        Item itemR = entity.getMainHandItem().getItem();
        Item itemL = entity.getOffhandItem().getItem();

        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem) {
            if (entity instanceof Player player) {
                if (entity.level().isClientSide()) AnimationManager.playAnimation(AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_JUMP, true);

                player.playSound(CSSoundEvents.HOP.get());

                if (itemR instanceof CSWeapon wp) wp.sendExpandingParticles(entity.level(), ParticleTypes.SMOKE, player.blockPosition(), 75, 0.35F);
                else {
                    CSWeapon wp = (CSWeapon) itemL;
                    wp.sendExpandingParticles(entity.level(), ParticleTypes.SMOKE, player.blockPosition(), 75, 0.35F);
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
}
