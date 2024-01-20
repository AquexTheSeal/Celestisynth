package com.aqutheseal.celestisynth.api.item;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.network.util.ChangeCameraTypePacket;
import com.aqutheseal.celestisynth.common.network.util.ShakeScreenServerPacket;
import com.aqutheseal.celestisynth.manager.CSNetworkManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
import java.util.function.Predicate;

public interface CSWeaponUtil {
    String CS_CONTROLLER_TAG_ELEMENT = "csController";
    String CS_EXTRAS_ELEMENT = "csExtras";
    String ANIMATION_TIMER_KEY = "cs.animationTimer";
    String ANIMATION_BEGUN_KEY = "cs.hasAnimationBegun";

    default void hurtNoKB(Player holder, LivingEntity target, float damage, boolean isBlockable) {
        if (damage == 0) return;

        double preAttribute = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();

        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100); //TODO Attrib modifiers!!

        target.invulnerableTime = 0;

        if (!(isBlockable && target.getUseItem().getItem() instanceof ShieldItem)) target.hurt(DamageSource.playerAttack(holder), damage);
        else useAndDamageItem(target.getUseItem(), target.level, target, (int) (damage / 3));

        target.doEnchantDamageEffects(holder, target);
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(preAttribute);
    }

    static MobEffectInstance nonVisiblePotionEffect(MobEffect effect, int ticks, int amplifier) {
        return new MobEffectInstance(effect, ticks, 2, true, false, false);
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

    default void useAndDamageItem(ItemStack pStack, Level pLevel, LivingEntity targetOwnerEntity, int damageAmount) {
        if (!pLevel.isClientSide) {
            pStack.hurtAndBreak(damageAmount, targetOwnerEntity, (ownerEntity) -> {
                if (targetOwnerEntity.getMainHandItem() == pStack) ownerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                else if (targetOwnerEntity.getOffhandItem() == pStack) ownerEntity.broadcastBreakEvent(InteractionHand.OFF_HAND);
            });
        }

        if (targetOwnerEntity instanceof Player ownerPlayer) ownerPlayer.awardStat(Stats.ITEM_USED.get(pStack.getItem()));
    }

    default void sendExpandingParticles(Level level, ParticleType<?> particleType, double x, double y, double z, int amount, float expansionMultiplier) {
        for (int i = 0; i < amount; i++) {
            RandomSource random = level.getRandom();
            float offX = (-0.5f + random.nextFloat()) * expansionMultiplier;
            float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
            float offZ = (-0.5f + random.nextFloat()) * expansionMultiplier;

            ParticleUtil.sendParticles(level, particleType, x, y, z, 0, offX, offY, offZ);
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

    default Entity getLookedAtEntity(Player player, double range) {
        double distance = range * range;
        Vec3 eyePos = player.getEyePosition(1);
        Vec3 viewVec = player.getViewVector(1);
        Vec3 targetVec = eyePos.add(viewVec.x * range, viewVec.y * range, viewVec.z * range);
        AABB aabb = player.getBoundingBox().expandTowards(viewVec.scale(range)).inflate(4.0D, 4.0D, 4.0D);
        EntityHitResult hitResult = expandedHitResult(player, eyePos, targetVec, aabb, (entity) -> !entity.isSpectator(), distance);

        return hitResult != null ? hitResult.getEntity() : null;
    }

    default void shakeScreensForNearbyPlayers(Player holder, Level level, double range, int maxDuration, int startFadingOut, float maxIntensity) {
        if (level.isClientSide()) {
            List<Player> entities = level.getEntitiesOfClass(Player.class, holder.getBoundingBox().inflate(range, range, range));

            for (Player entity : entities) shakeScreens(entity, maxDuration, startFadingOut, Math.max(0, maxIntensity - (float) (entity.distanceToSqr(holder) * 0.0001)));
        }
    }

    default void shakeScreens(Player target, int duration, int startFadingOut, float intensity) {
        if (target != null) CSNetworkManager.sendToServer(new ShakeScreenServerPacket(target.getUUID(), duration, startFadingOut, intensity));
    }

    default double calculateXLook(Player player) {
        return player.getLookAngle().x();
    }

    default double calculateYLook(Player player, double yMult) {
        double lookY = player.getLookAngle().y();

        if (lookY > 0) return lookY * yMult;
        else return lookY * 0.5;
    }

    default double calculateYLook(Player player) {
        return player.getLookAngle().y();
    }

    default double calculateZLook(Player player) {
        return player.getLookAngle().z();
    }

    default void setCameraAngle(Player player, int ordinal) {
        if (!player.level.isClientSide()) CSNetworkManager.sendToAll(new ChangeCameraTypePacket(player.getId(), ordinal));
    }

    static void disableRunningWeapon(Entity owner) {
        if (owner instanceof Player playerOwner) {
            AnimationManager.playAnimation(owner.level, AnimationManager.AnimationsList.CLEAR);

            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (playerOwner.getItemBySlot(slot).getItem() instanceof CSWeapon cs) {
                    CompoundTag data = playerOwner.getItemBySlot(slot).getTagElement(CS_CONTROLLER_TAG_ELEMENT);
                    CompoundTag dataAlt = playerOwner.getItemBySlot(slot).getTagElement(CS_EXTRAS_ELEMENT);

                    if (data != null) data.getAllKeys().clear();
                    if (dataAlt != null)  dataAlt.getAllKeys().clear();
                    cs.resetExtraValues(playerOwner.getItemBySlot(slot), playerOwner);
                }
            }

            if (playerOwner.isUsingItem()) playerOwner.releaseUsingItem();
        }
    }

    @Nullable
    static EntityHitResult expandedHitResult(Entity pShooter, Vec3 pStartVec, Vec3 pEndVec, AABB pBoundingBox, Predicate<Entity> pFilter, double pDistance) {
        Level level = pShooter.level;
        double range = pDistance;
        Entity confirmedTarget = null;
        Vec3 clipVec = null;

        for (Entity potentialTarget : level.getEntities(pShooter, pBoundingBox, pFilter)) {
            AABB boundsHitbox = potentialTarget.getBoundingBox().inflate((double)potentialTarget.getPickRadius() + 1.5);
            Optional<Vec3> potentialClippedVec = boundsHitbox.clip(pStartVec, pEndVec);

            if (boundsHitbox.contains(pStartVec)) {
                if (range >= 0.0D) {
                    confirmedTarget = potentialTarget;
                    clipVec = potentialClippedVec.orElse(pStartVec);
                    range = 0.0D;
                }
            } else if (potentialClippedVec.isPresent()) {
                Vec3 vec31 = potentialClippedVec.get();
                double distToTargetPos = pStartVec.distanceToSqr(vec31);

                if (distToTargetPos < range || range == 0.0D) {
                    if (potentialTarget.getRootVehicle() == pShooter.getRootVehicle() && !potentialTarget.canRiderInteract()) {
                        if (range == 0.0D) {
                            confirmedTarget = potentialTarget;
                            clipVec = vec31;
                        }
                    } else {
                        confirmedTarget = potentialTarget;
                        clipVec = vec31;
                        range = distToTargetPos;
                    }
                }
            }
        }

        return confirmedTarget == null ? null : new EntityHitResult(confirmedTarget, clipVec);
    }
}
