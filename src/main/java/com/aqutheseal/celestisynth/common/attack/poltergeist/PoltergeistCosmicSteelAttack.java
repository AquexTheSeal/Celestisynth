package com.aqutheseal.celestisynth.common.attack.poltergeist;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualType;
import com.aqutheseal.celestisynth.common.entity.skill.SkillCastPoltergeistWard;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
    public static final String SMASH_HEIGHT = "cs.poltergeistSmashHeight";
    public static final String SMASH_COUNT_FOR_PASSIVE = "cs.smashCountForPassive";

    public PoltergeistCosmicSteelAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_POLTERGEIST_SMASH;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.poltergeistSkillCD.get();
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
    }

    @Override
    public void tickAttack() {
        boolean isGiantImpact = getTagExtras().getBoolean(IS_IMPACT_LARGE);

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
            doImpact(isGiantImpact, xx, zz, range);

            player.playSound(SoundEvents.END_GATEWAY_SPAWN, 1.0F, 1.75F);
            player.playSound(CSSoundEvents.LOUD_IMPACT.get(), 1.5F, 1.0F);
            CSEffectEntity.createInstance(player, null, crack, xx, isGiantImpact ? -1.3 : -0.5, zz);

            if (isGiantImpact) {
                if (!level.isClientSide()) {
                    SkillCastPoltergeistWard projectile = CSEntityTypes.POLTERGEIST_WARD.get().create(level);
                    projectile.setOwnerUuid(player.getUUID());
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
        }
    }

    public void doImpact(boolean isGiantImpact, double kbX, double kbZ, double range) {
        for (Entity entityBatch : iterateEntities(level, createAABB(player.blockPosition().offset((int) kbX, 1, (int) kbZ), range))) {
            if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                initiateAbilityAttack(player, target, (isGiantImpact ? (float) (double) CSConfigManager.COMMON.poltergeistSkillDmg.get() * 1.4F : (float) (double) (CSConfigManager.COMMON.poltergeistSkillDmg.get())) + getSharpnessValue(getStack(), 1.2F) + getTagController().getInt(SMASH_HEIGHT), AttackHurtTypes.NO_KB);
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
            }

            if (entityBatch instanceof Projectile projectile) projectile.setDeltaMovement(0, 3, 0);
        }
    }

    @Override
    public void stopUsing() {
        getTagController().putInt(SMASH_HEIGHT, 0);
    }

    @Override
    public BlockPos getFloorPositionUnderPlayer(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        do {
            mutablePos.move(Direction.DOWN);
            if (CSConfigManager.COMMON.enablePoltergeistHeightDmg.get()) {
                getTagController().putInt(SMASH_HEIGHT, getTagController().getInt(SMASH_HEIGHT) + 1);
            }
        } while (mutablePos.getY() > level.getMinBuildHeight() && level.getBlockState(mutablePos).isPathfindable(level, mutablePos, PathComputationType.LAND));
        return new BlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
    }

    public void addComboPoint(ItemStack itemStack, Player player) {
        CompoundTag elementAltsTag = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        boolean isImpactLarge = elementAltsTag.getBoolean(IS_IMPACT_LARGE);

        if (!isImpactLarge && elementAltsTag.getInt(SMASH_COUNT_FOR_PASSIVE) < 9) {
            player.playSound(SoundEvents.ENDERMAN_TELEPORT);
            elementAltsTag.putInt(SMASH_COUNT_FOR_PASSIVE, elementAltsTag.getInt(SMASH_COUNT_FOR_PASSIVE) + 1);
        } else if (!isImpactLarge && elementAltsTag.getInt(SMASH_COUNT_FOR_PASSIVE) >= 9) {
            player.playSound(SoundEvents.END_PORTAL_SPAWN);
            elementAltsTag.putBoolean(IS_IMPACT_LARGE, true);
        } else if (isImpactLarge) {
            elementAltsTag.putBoolean(IS_IMPACT_LARGE, false);
            elementAltsTag.putInt(SMASH_COUNT_FOR_PASSIVE, 0);
        }
    }
}
