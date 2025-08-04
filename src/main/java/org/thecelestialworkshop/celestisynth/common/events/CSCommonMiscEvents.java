package org.thecelestialworkshop.celestisynth.common.events;

import org.thecelestialworkshop.celestisynth.api.animation.player.AnimationManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.LayerManager;
import org.thecelestialworkshop.celestisynth.api.item.CSArmorItem;
import org.thecelestialworkshop.celestisynth.api.item.CSWeapon;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import org.thecelestialworkshop.celestisynth.common.compat.CompatRegistryManager;
import org.thecelestialworkshop.celestisynth.common.entity.mob.natural.Traverser;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.SolarisBomb;
import org.thecelestialworkshop.celestisynth.common.entity.skillcast.SkillCastPoltergeistWard;
import org.thecelestialworkshop.celestisynth.common.item.weapons.BreezebreakerItem;
import org.thecelestialworkshop.celestisynth.common.registry.*;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import com.google.common.collect.Streams;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
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
    public static void onWeaponCrit(CriticalHitEvent event) {

    }

    @SubscribeEvent
    public static void giveItemAttributes(ItemAttributeModifierEvent event) {
        CompatRegistryManager.manageCompatAttributes(event);
    }

    @SubscribeEvent
    public static void onLivingHealEvent(LivingHealEvent event) {
        if (event.getEntity().hasEffect(CSMobEffects.CURSEBANE.get())) {
            event.setAmount((float) (event.getAmount() / (1 + (event.getEntity().getEffect(CSMobEffects.CURSEBANE.get()).getAmplifier() * 2.4))));
        }
        if (event.getEntity().hasEffect(CSMobEffects.HELLBANE.get())) {
            event.setAmount((float) (event.getAmount() * (1 + (event.getEntity().getEffect(CSMobEffects.HELLBANE.get()).getAmplifier() * 1.6))));
        }
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

        CSArmorItem.hurtWearer(event);

//        if (event.getSource().getEntity() instanceof LivingEntity source) {
//            source.getHandSlots().forEach(slot -> {
//                for (Map.Entry<Enchantment, Integer> enchantmentMap : EnchantmentHelper.getEnchantments(slot).entrySet()) {
//                    if (enchantmentMap.getKey() instanceof BaseEnchantment enchantment) {
//                        int level = enchantmentMap.getValue();
//                        enchantment.afterAttack(source, event.getEntity(), slot, level);
//                    }
//                }
//            });
//        }

        SolarisBomb.handleHurtEvent(event);
    }

    @SubscribeEvent
    public static void onLivingCritEvent(CriticalHitEvent event) {
        if (event.getTarget() instanceof Traverser traverser) {
            if (event.isVanillaCritical()) {
                traverser.resetAnimationTick();
                traverser.setAction(Traverser.ACTION_STUNNED);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            CSWeaponUtil.disableRunningWeapon(player);
        }

        CSEntityCapabilityProvider.get(event.getEntity()).ifPresent(data -> {
            if (data.getPhantomTagSource() instanceof Player player) {
                SkillCastPoltergeistWard poltergeistProjectile = CSEntityTypes.POLTERGEIST_WARD.get().create(event.getEntity().level());
                poltergeistProjectile.setOwnerUUID(player.getUUID());
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

        if (entity instanceof Player player && player.level().isClientSide()) AnimationManager.playAnimation(CSPlayerAnimations.CLEAR.get(), LayerManager.LOW_PRIORITY_LAYER);
        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem)  event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLivingJumpEvent(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntity();
        Item itemR = entity.getMainHandItem().getItem();
        Item itemL = entity.getOffhandItem().getItem();

        if (itemR instanceof BreezebreakerItem || itemL instanceof BreezebreakerItem) {
            if (entity instanceof Player player) {
                if (entity.level().isClientSide()) AnimationManager.playAnimation(CSPlayerAnimations.ANIM_BREEZEBREAKER_JUMP.get(), LayerManager.LOW_PRIORITY_LAYER);

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
