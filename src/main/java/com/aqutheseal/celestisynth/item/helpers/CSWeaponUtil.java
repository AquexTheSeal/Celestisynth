package com.aqutheseal.celestisynth.item.helpers;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.network.CSNetwork;
import com.aqutheseal.celestisynth.network.util.ChangeCameraTypePacket;
import com.aqutheseal.celestisynth.network.util.ShakeScreenServerPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;

public interface CSWeaponUtil {

    String CS_CONTROLLER_TAG_ELEMENT = "csController";
    String CS_EXTRAS_ELEMENT = "csExtras";

    String ANIMATION_TIMER_KEY = "cs.animationTimer";
    String ANIMATION_BEGUN_KEY = "cs.hasAnimationBegun";

    static void disableRunningWeapon(Entity entity) {
        if (entity instanceof Player player) {
            AnimationManager.playAnimation(entity.level, AnimationManager.AnimationsList.CLEAR);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (player.getItemBySlot(slot).getItem() instanceof CSWeapon cs) {
                    CompoundTag data = player.getItemBySlot(slot).getTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
                    CompoundTag dataAlt = player.getItemBySlot(slot).getTagElement(CSWeapon.CS_EXTRAS_ELEMENT);
                    if (data != null) data.getAllKeys().clear();
                    if (dataAlt != null)  dataAlt.getAllKeys().clear();
                    cs.resetExtraValues(player.getItemBySlot(slot), player);
                }
            }
            if (player.isUsingItem()) {
                player.releaseUsingItem();
            }
        }
    }

    default void hurtNoKB(Player holder, LivingEntity target, float damage, boolean isBlockable) {
        if (damage == 0) return;
        double preAttribute = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
        target.invulnerableTime = 0;
        if (!(isBlockable && target.getUseItem().getItem() instanceof ShieldItem)) {
            target.hurt(DamageSource.playerAttack(holder), damage);
        } else {
            useAndDamageItem(target.getUseItem(), target.level, target, (int) (damage / 3));
        }
        target.doEnchantDamageEffects(holder, target);
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(preAttribute);
    }

    default void hurtNoKB(Player holder, LivingEntity target, float damage) {
        hurtNoKB(holder, target, damage, false);
    }

    default void setDeltaPlayer(Player player, double x, double y, double z) {
        player.hurtMarked = true;
        player.setDeltaMovement(x, y, z);
    }

    default void setDeltaPlayer(Player player, Vec3 vec) {
        player.hurtMarked = true;
        player.setDeltaMovement(vec);
    }

    default float getSharpnessValue(ItemStack stack, float multiplier) {
        return EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, stack) * multiplier;
    }

    default void useAndDamageItem(ItemStack pStack, Level pLevel, LivingEntity player, int damageAmount) {
        if (!pLevel.isClientSide) {
            pStack.hurtAndBreak(damageAmount, player, (p_43388_) -> {
                if (player.getMainHandItem() == pStack) {
                    p_43388_.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                } else if (player.getOffhandItem() == pStack) {
                    p_43388_.broadcastBreakEvent(InteractionHand.OFF_HAND);
                }
            });
        }
        if (player instanceof Player playerE) {
            playerE.awardStat(Stats.ITEM_USED.get(pStack.getItem()));
        }
    }

    default void sendExpandingParticles(Level level, ParticleType<?> particleType, double x, double y, double z, int amount, float expansionMultiplier) {
        for (int i = 0; i < amount; i++) {
            Random random = new Random();
            float offX = (-0.5f + random.nextFloat()) * expansionMultiplier;
            float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
            float offZ = (-0.5f + random.nextFloat()) * expansionMultiplier;
            CSUtilityFunctions.sendParticles(level, particleType, x, y, z, 0, offX, offY, offZ);
        }
    }

    default void sendExpandingParticles(Level level, ParticleType<?> particleType, BlockPos origin, int amount, float expansionMultiplier) {
        sendExpandingParticles(level, particleType, origin.getX(), origin.getY(), origin.getZ(), amount, expansionMultiplier);
    }

    default AABB createAABB(BlockPos pos, double range) {
        return createAABB(pos.getX(), pos.getY(), pos.getZ(), range);
    }

    default AABB createAABB(double x, double y, double z, double range) {
        return new AABB(x + range, y + range, z + range, x - range, y - range, z - range);
    }

    default List<Entity> iterateEntities(Level level, AABB aabb) {
        return level.getEntitiesOfClass(Entity.class, aabb);
    }

    default boolean checkDualWield(Player player, Class<? extends CSWeapon> weapon) {
        return weapon.isInstance(player.getMainHandItem().getItem()) && (weapon.isInstance(player.getOffhandItem().getItem()));
    }

    default Entity getLookAtEntity(Player player, double range) {
        double distance = range * range;
        Vec3 vec = player.getEyePosition(1);
        Vec3 vec1 = player.getViewVector(1);
        Vec3 targetVec = vec.add(vec1.x * range, vec1.y * range, vec1.z * range);
        AABB aabb = player.getBoundingBox().expandTowards(vec1.scale(range)).inflate(4.0D, 4.0D, 4.0D);
        EntityHitResult result = expandedHitResult(player, vec, targetVec, aabb, (entity) -> !entity.isSpectator(), distance);
        return result != null ? result.getEntity() : null;
    }

    default void shakeScreensForNearbyPlayers(Player holder, Level level, double range, int maxDuration, int startFadingOut, float maxIntensity) {
        if (level.isClientSide()) {
            List<Player> entities = level.getEntitiesOfClass(Player.class, holder.getBoundingBox().inflate(range, range, range));
            for (Player entity : entities) {
                shakeScreens(entity, maxDuration, startFadingOut, Math.max(0, maxIntensity - (float) (entity.distanceToSqr(holder) * 0.0001)));
            }
        }
    }

    default void shakeScreens(Player target, int duration, int startFadingOut, float intensity) {
        if (target != null) {
            CSNetwork.sendToServer(new ShakeScreenServerPacket(target.getUUID(), duration, startFadingOut, intensity));
        }
    }

    default double calculateXLook(Player player) {
        return player.getLookAngle().x();
    }

    default double calculateYLook(Player player, double yMult) {
        double y = player.getLookAngle().y();
        if (y > 0) {
            return y * yMult;
        } else {
            return y * 0.5;
        }
    }

    default double calculateYLook(Player player) {
        return player.getLookAngle().y();
    }

    default double calculateZLook(Player player) {
        return player.getLookAngle().z();
    }

    default void setCameraAngle(Player player, int ordinal) {
        if (!player.level.isClientSide()) {
            CSNetwork.sendToAll(new ChangeCameraTypePacket(player.getId(), ordinal));
        }
    }

    @Nullable
    public static EntityHitResult expandedHitResult(Entity pShooter, Vec3 pStartVec, Vec3 pEndVec, AABB pBoundingBox, Predicate<Entity> pFilter, double pDistance) {
        Level level = pShooter.level;
        double d0 = pDistance;
        Entity entity = null;
        Vec3 vec3 = null;

        for(Entity entity1 : level.getEntities(pShooter, pBoundingBox, pFilter)) {
            AABB aabb = entity1.getBoundingBox().inflate((double)entity1.getPickRadius() + 1.5);
            Optional<Vec3> optional = aabb.clip(pStartVec, pEndVec);
            if (aabb.contains(pStartVec)) {
                if (d0 >= 0.0D) {
                    entity = entity1;
                    vec3 = optional.orElse(pStartVec);
                    d0 = 0.0D;
                }
            } else if (optional.isPresent()) {
                Vec3 vec31 = optional.get();
                double d1 = pStartVec.distanceToSqr(vec31);
                if (d1 < d0 || d0 == 0.0D) {
                    if (entity1.getRootVehicle() == pShooter.getRootVehicle() && !entity1.canRiderInteract()) {
                        if (d0 == 0.0D) {
                            entity = entity1;
                            vec3 = vec31;
                        }
                    } else {
                        entity = entity1;
                        vec3 = vec31;
                        d0 = d1;
                    }
                }
            }
        }

        return entity == null ? null : new EntityHitResult(entity, vec3);
    }
}
