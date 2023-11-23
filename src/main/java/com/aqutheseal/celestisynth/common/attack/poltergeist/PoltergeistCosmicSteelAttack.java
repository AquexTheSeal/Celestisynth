package com.aqutheseal.celestisynth.common.attack.poltergeist;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.api.mixin.LivingMixinSupport;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.base.CSEffect;
import com.aqutheseal.celestisynth.common.entity.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.common.entity.skill.SkillCastPoltergeistWard;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
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
        return 25;
    }

    @Override
    public boolean getCondition() {
        return !getPlayer().isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        useAndDamageItem(getStack(), getPlayer().level, getPlayer(), 5);
    }

    @Override
    public void tickAttack() {
        CompoundTag data = getStack().getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        CompoundTag dataAlt = getStack().getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        boolean isGiantImpact = dataAlt.getBoolean(IS_IMPACT_LARGE);
        boolean forceCreateImpact = false;

        if (getTimerProgress() == 20) {
            double xx = calculateXLook(player) * (isGiantImpact ? 4 : 3);
            double zz = calculateZLook(player) * (isGiantImpact ? 4 : 3);

            if (!player.isOnGround() && !player.getAbilities().flying) {
                getPlayer().moveTo(player.getX(), calculateNonCollidingPos(player.level, getPlayer().blockPosition()).getY() + 1, getPlayer().getZ());

                forceCreateImpact = true;
            }

            double range = (!forceCreateImpact && !player.isOnGround()) ? 4 : isGiantImpact ? 4 : 6.5;

            for (Entity entityBatch : iterateEntities(player.level, createAABB(player.blockPosition().offset(xx, 1, zz), range))) {
                if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                    hurtNoKB(player, target, isGiantImpact ? (float) (double) CSConfigManager.COMMON.poltergeistSkillDmg.get() : (float) (CSConfigManager.COMMON.poltergeistSkillDmg.get() + 4) + getSharpnessValue(getStack(), 1.2F));
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));

                    target.hurtMarked = true;

                    target.setDeltaMovement((target.getX() - (player.getX() + xx)) / 4, (target.getY() - getPlayer().getY()) / 4, (target.getZ() - (player.getZ() + zz)) / 4);
                    CSWeaponUtil.disableRunningWeapon(target);

                    if (target instanceof LivingMixinSupport lms) lms.setPhantomTagger(player);
                }

                if (entityBatch instanceof Projectile projectile) projectile.remove(Entity.RemovalReason.DISCARDED);
            }

            if (isGiantImpact && !player.level.isClientSide()) {
                SkillCastPoltergeistWard projectile = CSEntityTypes.POLTERGEIST_WARD.get().create(player.level);

                projectile.setOwnerUuid(player.getUUID());
                projectile.moveTo(player.getX() + xx, getPlayer().getY(), getPlayer().getZ() + zz);
                getPlayer().level.addFreshEntity(projectile);
            }

            if (player.isOnGround() || forceCreateImpact) {
                CSEffect.createInstance(player, null, isGiantImpact ? CSEffectTypes.POLTERGEIST_IMPACT_CRACK_LARGE : CSEffectTypes.POLTERGEIST_IMPACT_CRACK, xx, isGiantImpact ? -1.55 : -0.35, zz);

                if (!isGiantImpact) CSEffect.createInstance(player, null, CSEffectTypes.POLTERGEIST_WARD_SUMMON_SMALL, xx, 1, zz);

                if (isGiantImpact) shakeScreensForNearbyPlayers(player, getPlayer().level, 24, 60, 30,  0.035F);
                else shakeScreensForNearbyPlayers(player, getPlayer().level, 12, 30, 15,  0.01F);
            } else getPlayer().playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.5F);

            addComboPoint(getStack(), player);

            if (!player.isOnGround()) {
                getPlayer().getCooldowns().removeCooldown(getStack().getItem());

                data.putInt(ANIMATION_TIMER_KEY, 0);
                data.putBoolean(ANIMATION_BEGUN_KEY, false);
            }
        }
    }

    @Override
    public void stopUsing() {
    }

    public BlockPos calculateNonCollidingPos(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());

        do {
            mutablePos.move(Direction.DOWN);
        } while (mutablePos.getY() > level.getMinBuildHeight() && level.getBlockState(mutablePos).isPathfindable(level, mutablePos, PathComputationType.LAND));

        return new BlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
    }

    public void addComboPoint(ItemStack itemStack, Player player) {
        CompoundTag elementAltsTag = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        boolean isImpactLarge = elementAltsTag.getBoolean(IS_IMPACT_LARGE);

        if (!isImpactLarge && elementAltsTag.getInt(SMASH_COUNT_FOR_PASSIVE) < 9) {
            getPlayer().playSound(SoundEvents.ENDERMAN_TELEPORT);
            elementAltsTag.putInt(SMASH_COUNT_FOR_PASSIVE, elementAltsTag.getInt(SMASH_COUNT_FOR_PASSIVE) + 1);
        } else if (!isImpactLarge && elementAltsTag.getInt(SMASH_COUNT_FOR_PASSIVE) >= 9) {
            getPlayer().playSound(SoundEvents.END_PORTAL_SPAWN);
            elementAltsTag.putBoolean(IS_IMPACT_LARGE, true);
        } else if (isImpactLarge) {
            elementAltsTag.putBoolean(IS_IMPACT_LARGE, false);
            elementAltsTag.putInt(SMASH_COUNT_FOR_PASSIVE, 0);
        }
    }
}
