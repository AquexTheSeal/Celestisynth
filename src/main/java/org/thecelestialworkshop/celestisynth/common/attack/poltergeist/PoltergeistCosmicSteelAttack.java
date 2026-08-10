package org.thecelestialworkshop.celestisynth.common.attack.poltergeist;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.entity.helper.CSVisualType;
import org.thecelestialworkshop.celestisynth.common.entity.skillcast.SkillCastPoltergeistWard;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;

public class PoltergeistCosmicSteelAttack extends WeaponAttackInstance {
    public static final String IS_IMPACT_LARGE = "cs.isImpactLarge";
    public static final String QUEUE_COMBO_LARGE = "cs.queueComboLarge";
    public static final String SMASH_HEIGHT = "cs.poltergeistSmashHeight";
    public static final String SMASH_COUNT_FOR_PASSIVE = "cs.smashCountForPassive";

    // Experimental
//    public static final ParticleEmitterInfo POLTER = new ParticleEmitterInfo(Celestisynth.prefix("poltereis"));
//    public static final ParticleEmitterInfo POLTEREPIC = new ParticleEmitterInfo(Celestisynth.prefix("poltereisepic"));

    public PoltergeistCosmicSteelAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_POLTERGEIST_SMASH.get();
    }

    @Override
    public int getCooldown() {
        return 200;
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return !player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        useAndDamageItem(getStack(), level, player, 5);
        getTagExtras().putBoolean(QUEUE_COMBO_LARGE, false);
    }

    @Override
    public void tickAttack() {
        boolean isGiantImpact = getTagExtras().getBoolean(IS_IMPACT_LARGE);

        if (getTimerProgress() == 5) {
            if (getTagExtras().getBoolean(IS_IMPACT_LARGE)) {
                player.push(0, 1, 0);
            }
        }

        if (getTimerProgress() == 18) {
            int groundY = getFloorPositionUnderPlayer(level, player.blockPosition()).getY();
            player.setDeltaMovement(0, groundY - player.getY(), 0);
            player.fallDistance = 0;
        }

        if (getTimerProgress() == 20) {
            CSVisualType crack =  isGiantImpact ? CSVisualTypes.POLTERGEIST_IMPACT_CRACK_LARGE.get() : CSVisualTypes.POLTERGEIST_IMPACT_CRACK.get();
            double range = isGiantImpact ? 6.5 : 4;
            double xx = calculateXLook(player) * 3;
            double zz = calculateZLook(player) * 3;

            player.playSound(SoundEvents.END_GATEWAY_SPAWN, 1.0F, 1.75F);
            player.playSound(CSSoundEvents.LOUD_IMPACT.get(), 1.5F, 1.0F);
            CSEffectEntity.createInstance(player, null, crack, xx, isGiantImpact ? -1.3 : -0.5, zz);

//            if (!level.isClientSide) {
//                ParticleEmitterInfo particle2 = (isGiantImpact ? POLTEREPIC : POLTER).clone().position(player.position().add(xx, 0.01, zz)).scale(isGiantImpact ? 5F : 4F);
//                AAALevel.addParticle(level, particle2);
//            }

            this.doImpact(isGiantImpact, xx, zz, range);

            if (isGiantImpact) {
                if (!level.isClientSide()) {
                    SkillCastPoltergeistWard projectile = CSEntityTypes.POLTERGEIST_WARD.get().create(level);
                    projectile.setOwnerUUID(player.getUUID());
                    projectile.moveTo(player.getX() + xx, player.getY(), player.getZ() + zz);
                    level.addFreshEntity(projectile);
                }
                shakeScreensForNearbyPlayers(player, level, 24, 60, 30,  0.035F);
            } else {
                CSEffectEntity.createInstance(player, null, CSVisualTypes.POLTERGEIST_WARD_SUMMON_SMALL.get(), xx, 1, zz);
                shakeScreensForNearbyPlayers(player, level, 12, 30, 15,  0.01F);
            }

            addComboPoint(getStack(), player);

            if (getTagController().getInt(SMASH_HEIGHT) > 1) {
                player.getCooldowns().removeCooldown(getStack().getItem());
            }

            if (getTagExtras().getBoolean(QUEUE_COMBO_LARGE)) {
                player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SPEED, 80, 1));
                getTagExtras().putBoolean(IS_IMPACT_LARGE, true);
                getTagExtras().putBoolean(QUEUE_COMBO_LARGE, false);
            }
        }
    }

    public void doImpact(boolean isGiantImpact, double kbX, double kbZ, double range) {
        for (Entity entityBatch : iterateEntities(level, createAABB(player.blockPosition().offset((int) kbX, 1, (int) kbZ), range))) {
            if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {

                float dmgCalc = (isGiantImpact ? 1.5F : 1.2F);
                float attributedDmg = this.calculateAttributeDependentDamage(player, stack, dmgCalc);
                float smashHeightAdd = this.getTagController().getInt(SMASH_HEIGHT);
                this.initiateAbilityAttack(player, target, attributedDmg + smashHeightAdd, AttackHurtTypes.NO_KB);

                target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.CONFUSION, 100, 0));
                target.hurtMarked = true;
                target.setDeltaMovement((target.getX() - (player.getX() + kbX)) / 3, (target.getY() - player.getY()) / 3, (target.getZ() - (player.getZ() + kbZ)) / 3);
                CSWeaponUtil.disableRunningWeapon(target);

                if (!level.isClientSide()) {
                    CSEntityCapabilityProvider.get(target).ifPresent(data -> {
                        data.setPhantomTag(player, 100);
                    });
                }

                if (target.isDeadOrDying()) {
                    player.getCooldowns().removeCooldown(getStack().getItem());
                    this.getTagExtras().putBoolean(QUEUE_COMBO_LARGE, true);
                }
            }

            if (entityBatch instanceof Projectile projectile) projectile.setDeltaMovement(0, 1.2, 0);
        }
    }

    @Override
    public void stopUsing() {
        getTagController().putInt(SMASH_HEIGHT, 0);
        getTagExtras().putBoolean(QUEUE_COMBO_LARGE, false);
    }

    @Override
    public BlockPos getFloorPositionUnderPlayer(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        do {
            mutablePos.move(Direction.DOWN);
            getTagController().putInt(SMASH_HEIGHT, getTagController().getInt(SMASH_HEIGHT) + 1);
        } while (mutablePos.getY() > level.getMinBuildHeight() && level.getBlockState(mutablePos).isPathfindable(PathComputationType.LAND));
        return new BlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
    }

    public void addComboPoint(ItemStack itemStack, Player player) {
        boolean isImpactLarge = attackExtras(itemStack).getBoolean(IS_IMPACT_LARGE);

        if (!isImpactLarge && attackExtras(itemStack).getInt(SMASH_COUNT_FOR_PASSIVE) < 9) {
            player.playSound(SoundEvents.ENDERMAN_TELEPORT);
            attackExtras(itemStack).putInt(SMASH_COUNT_FOR_PASSIVE, attackExtras(itemStack).getInt(SMASH_COUNT_FOR_PASSIVE) + 1);
        } else if (!isImpactLarge && attackExtras(itemStack).getInt(SMASH_COUNT_FOR_PASSIVE) >= 9) {
            player.playSound(SoundEvents.END_PORTAL_SPAWN);
            attackExtras(itemStack).putBoolean(IS_IMPACT_LARGE, true);
        } else if (isImpactLarge) {
            attackExtras(itemStack).putBoolean(IS_IMPACT_LARGE, false);
            attackExtras(itemStack).putInt(SMASH_COUNT_FOR_PASSIVE, 0);
        }
    }
}
