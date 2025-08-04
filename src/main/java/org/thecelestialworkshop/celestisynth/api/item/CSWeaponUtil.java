package org.thecelestialworkshop.celestisynth.api.item;

import org.thecelestialworkshop.celestisynth.api.animation.player.AnimationManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.LayerManager;
import org.thecelestialworkshop.celestisynth.api.mixin.PlayerMixinSupport;
import org.thecelestialworkshop.celestisynth.common.network.c2s.ShakeScreenForAllPacket;
import org.thecelestialworkshop.celestisynth.common.network.s2c.ChangeCameraTypePacket;
import org.thecelestialworkshop.celestisynth.common.registry.CSAttributes;
import org.thecelestialworkshop.celestisynth.common.registry.CSDamageSources;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.manager.CSNetworkManager;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface CSWeaponUtil {
    String CS_CONTROLLER_TAG_ELEMENT = "csController";
    String CS_EXTRAS_ELEMENT = "csExtras";
    String ANIMATION_TIMER_KEY = "cs.animationTimer";
    String ANIMATION_BEGUN_KEY = "cs.hasAnimationBegun";

    default float calculateAttributeDependentDamage(LivingEntity holder, ItemStack stack, float attackAttributeMultiplier) {
        float holderAttribute = (float) holder.getAttributeValue(Attributes.ATTACK_DAMAGE) + EnchantmentHelper.getDamageBonus(holder.getMainHandItem(), MobType.UNDEFINED);
        float holderAttackDamage = (holderAttribute - this.getDamageOfItem(holder.getMainHandItem()) + this.getDamageOfItem(stack)) * attackAttributeMultiplier;

        return holderAttackDamage;
    }

    default void attributeDependentAttack(LivingEntity holder, LivingEntity target, ItemStack stack, float attackAttributeMultiplier, AttackHurtTypes attackHurtType) {
        this.initiateAbilityAttack(holder, target, this.calculateAttributeDependentDamage(holder, stack, attackAttributeMultiplier), attackHurtType);
    }

    private float getDamageOfItem(ItemStack stack) {
        return (float) (stack.getAttributeModifiers(EquipmentSlot.MAINHAND).get(Attributes.ATTACK_DAMAGE).stream().mapToDouble(AttributeModifier::getAmount).sum() + EnchantmentHelper.getDamageBonus(stack, MobType.UNDEFINED));
    }

    default void initiateAbilityAttack(LivingEntity holder, LivingEntity target, float damage, DamageSource damageSource, AttackHurtTypes attackHurtType) {
        if (damage == 0) return;
        CSDamageSources source = new CSDamageSources(target.level().registryAccess());
        DamageSource regularDamage = source.basicPlayerAttack(holder);
        DamageSource rapidDamage = source.rapidPlayerAttack(holder);
        DamageSource regularDamageNoKB = source.basicPlayerAttackWithoutKB(holder);
        DamageSource rapidDamageNoKB = source.rapidPlayerAttackWithoutKB(holder);
        DamageSource finalDamageSource;
        if (damageSource != null) {
            finalDamageSource = damageSource;
        } else {
            finalDamageSource = switch (attackHurtType) {
                case REGULAR -> regularDamage;
                case NO_KB -> regularDamageNoKB;
                case RAPID -> rapidDamage;
                case RAPID_NO_KB -> rapidDamageNoKB;
            };
        }
        double finalDamage = damage * holder.getAttributeValue(CSAttributes.CELESTIAL_DAMAGE.get()) / target.getAttributeValue(CSAttributes.CELESTIAL_DAMAGE_REDUCTION.get());
        if (!attackHurtType.doKnockback()) {
            double preAttribute = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
            target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(1000);
            attack(holder, target, (float) finalDamage, finalDamageSource, attackHurtType);
            target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(preAttribute);
        } else {
            attack(holder, target, (float) finalDamage, finalDamageSource, attackHurtType);
        }
    }

    default void initiateAbilityAttack(LivingEntity holder, LivingEntity target, float damage, AttackHurtTypes attackHurtType) {
        initiateAbilityAttack(holder, target, damage, null, attackHurtType);
    }

    private void attack(LivingEntity holder, LivingEntity target, float damage, DamageSource damageSource, AttackHurtTypes attackHurtType) {
        if (attackHurtType.isRapid()) {
            target.invulnerableTime = 0;
        }
        target.hurt(damageSource, damage);
        //target.doEnchantDamageEffects(holder, target);
    }

    default CompoundTag attackController(ItemStack stack) {
        return stack.getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
    }

    default CompoundTag attackExtras(ItemStack stack) {
        return stack.getOrCreateTagElement(CSWeapon.CS_EXTRAS_ELEMENT);
    }

    static MobEffectInstance nonVisiblePotionEffect(MobEffect effect, int ticks, int amplifier) {
        return new MobEffectInstance(effect, ticks, amplifier, true, false, false);
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
                if (targetOwnerEntity.getMainHandItem() == pStack) {
                    ownerEntity.broadcastBreakEvent(InteractionHand.MAIN_HAND);
                } else if (targetOwnerEntity.getOffhandItem() == pStack) {
                    ownerEntity.broadcastBreakEvent(InteractionHand.OFF_HAND);
                }
            });
        }
        if (targetOwnerEntity instanceof Player ownerPlayer) {
            ownerPlayer.awardStat(Stats.ITEM_USED.get(pStack.getItem()));
        }
    }

    default void sendExpandingParticles(Level level, ParticleType<?> particleType, double x, double y, double z, int amount, float expansionMultiplier) {
        for (int i = 0; i < amount; i++) {
            RandomSource random = level.getRandom();
            float offX = (float) (random.nextGaussian() * expansionMultiplier);
            float offY = (float) (random.nextGaussian() * expansionMultiplier);
            float offZ = (float) (random.nextGaussian() * expansionMultiplier);
            ParticleUtil.sendParticles(level, particleType, x, y, z, 0, offX, offY, offZ);
        }
    }

    default void sendExpandingParticles(Level level, ParticleType<?> particleType, BlockPos origin, int amount, float expansionMultiplier) {
        sendExpandingParticles(level, particleType, origin.getX(), origin.getY(), origin.getZ(), amount, expansionMultiplier);
    }

    default AABB createAABB(Vec3i pos, double range) {
        return createAABB(pos.getX(), pos.getY(), pos.getZ(), range);
    }

    default AABB createAABB(Vec3i pos, double xzRange, double yRange) {
        return createAABB(pos.getX(), pos.getY(), pos.getZ(),xzRange, yRange);
    }

    default AABB createAABB(double x, double y, double z, double range) {
        return createAABB(x, y, z, range, range);
    }

    default AABB createAABB(double x, double y, double z, double xzRange, double yRange) {
        return new AABB(x + xzRange, y + yRange, z + xzRange, x - xzRange, y - yRange, z - xzRange);
    }

    default List<Entity> iterateEntities(Level level, AABB aabb) {
        return level.getEntitiesOfClass(Entity.class, aabb);
    }

    default boolean checkDualWield(Player player, Class<? extends CSWeapon> weapon) {
        return weapon.isInstance(player.getMainHandItem().getItem()) && (weapon.isInstance(player.getOffhandItem().getItem()));
    }

    default void shakeScreensForNearbyPlayers(Entity holder, Level level, double range, int maxDuration, int startFadingOut, float maxIntensity) {
        if (level.isClientSide()) {
            List<Player> entities = level.getEntitiesOfClass(Player.class, holder.getBoundingBox().inflate(range, range, range));

            for (Player entity : entities) shakeScreens(entity, maxDuration, startFadingOut, Math.max(0, maxIntensity - (float) (entity.distanceToSqr(holder) * 0.00001)));
        }
    }

    default void shakeScreens(Player target, int duration, int startFadingOut, float intensity) {
        if (target != null) CSNetworkManager.sendToServer(new ShakeScreenForAllPacket(target.getUUID(), duration, startFadingOut, intensity));
    }

    default double calculateXLook(LivingEntity player) {
        return player.getLookAngle().x();
    }

    default double calculateYLook(LivingEntity player, double yMult) {
        double lookY = player.getLookAngle().y();

        if (lookY > 0) return lookY * yMult;
        else return lookY * 0.5;
    }

    default double calculateYLook(LivingEntity player) {
        return player.getLookAngle().y();
    }

    default double calculateZLook(LivingEntity player) {
        return player.getLookAngle().z();
    }

    default void setCameraAngle(Player player, int ordinal) {
        if (!player.level().isClientSide()) CSNetworkManager.sendToAll(new ChangeCameraTypePacket(player.getId(), ordinal));
    }

    static void disableRunningWeapon(Entity owner) {
        if (owner instanceof Player playerOwner) {
            AnimationManager.playAnimation(owner.level(), CSPlayerAnimations.CLEAR.get());
            AnimationManager.playAnimation(owner.level(), CSPlayerAnimations.CLEAR.get(), LayerManager.MIRRORED_LAYER);

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

    default BlockPos getFloorPositionUnderPlayer(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        do {
            mutablePos.move(Direction.DOWN);
        } while (mutablePos.getY() > level.getMinBuildHeight() && level.getBlockState(mutablePos).isPathfindable(level, mutablePos, PathComputationType.LAND));
        return new BlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
    }

    default int getFloorPositionUnderPlayerYLevel(Level level, BlockPos pos) {
        return getFloorPositionUnderPlayer(level, pos).getY();
    }


    default List<LivingEntity> getEntitiesInLine(Player player, double maxDistance) {
        Vec3 originPosition = player.getEyePosition();
        Vec3 lookDirection = player.getLookAngle().normalize().multiply(1, 0, 1);
        Vec3 endPosition = originPosition.add(lookDirection.scale(maxDistance));
        AABB rangeBox = new AABB(originPosition, endPosition);

        List<Entity> possibleEntitiesInRange = player.level().getEntities(player, rangeBox, entity -> entity instanceof LivingEntity);
        List<LivingEntity> livingEntitiesInLine = new ArrayList<>();

        for (Entity entity : possibleEntitiesInRange) {
            if (entity instanceof LivingEntity living) {
                AABB entityBox = entity.getBoundingBox().inflate(0.5, 0.5, 0.5);
                Optional<Vec3> optionalIntersection = entityBox.clip(originPosition, endPosition);
                if (optionalIntersection.isPresent()) {
                    livingEntitiesInLine.add(living);
                }
            }
        }

        return livingEntitiesInLine;
    }

    default Entity getLookedAtEntity(LivingEntity holder, double range) {
        double distance = range * range;
        Vec3 eyePos = holder.getEyePosition(1);
        Vec3 viewVec = holder.getViewVector(1);
        Vec3 targetVec = eyePos.add(viewVec.x * range, viewVec.y * range, viewVec.z * range);
        AABB aabb = holder.getBoundingBox().expandTowards(viewVec.scale(range)).inflate(4.0D, 4.0D, 4.0D);
        EntityHitResult hitResult = expandedHitResult(holder, eyePos, targetVec, aabb, (entity) -> !entity.isSpectator(), distance);
        return hitResult != null ? hitResult.getEntity() : null;
    }


    @Nullable
    static EntityHitResult expandedHitResult(Entity pShooter, Vec3 pStartVec, Vec3 pEndVec, AABB pBoundingBox, Predicate<Entity> pFilter, double pDistance) {
        Level level = pShooter.level();
        double range = pDistance;
        Entity confirmedTarget = null;
        Vec3 clipVec = null;
        for (Entity potentialTarget : level.getEntities(pShooter, pBoundingBox, pFilter)) {
            AABB boundsHitbox = potentialTarget.getBoundingBox().inflate((double)potentialTarget.getPickRadius() + 2.5);
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

    default void playSoundAt(Level level, SoundEvent sound, BlockPos pos) {
        level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    default void chantMessage(Entity player, String id, int time, int color) {
        if (player instanceof PlayerMixinSupport mixinPlayer && player.level().isClientSide) {
            mixinPlayer.setChantMark(-time);
            mixinPlayer.setChantColor(color);
            mixinPlayer.setChantMessage("chant.celestisynth." + id);
        }
    }

    default void flashScreen(Entity player, int pulseScale, int speed) {
        if (player instanceof PlayerMixinSupport mixinPlayer && player.level().isClientSide) {
            mixinPlayer.setPulseScale(pulseScale);
            mixinPlayer.setPulseTimeSpeed(speed);
        }
    }

    default void pulseImageOnUI(Entity player, String textureName, int time) {
        if (player instanceof PlayerMixinSupport mixinPlayer && player.level().isClientSide) {
            mixinPlayer.setTexturePulseMark(time);
            mixinPlayer.setTexturePulseImage(textureName);
        }
    }
}
