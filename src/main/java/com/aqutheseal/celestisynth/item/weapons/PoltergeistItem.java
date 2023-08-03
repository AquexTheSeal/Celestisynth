package com.aqutheseal.celestisynth.item.weapons;

import com.aqutheseal.celestisynth.LivingMixinSupport;
import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.SkillCastPoltergeistWard;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;

public class PoltergeistItem extends AxeItem implements CSWeapon {

    public static final String IS_RETREAT_KEY = "cs.isRetreatAttack";
    public static final String IS_IMPACT_LARGE = "cs.isImpactLarge";
    public static final String SMASH_COUNT_FOR_PASSIVE = "cs.smashCountForPassive";

    public PoltergeistItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public int getSkillsAmount() {
        return 2;
    }

    @Override
    public boolean hasPassive() {
        return true;
    }

    @Override
    public int getPassiveAmount() {
        return 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        CompoundTag itemTag = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (!player.getCooldowns().isOnCooldown(this) && !itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
            itemTag.putBoolean(IS_RETREAT_KEY, player.isShiftKeyDown());
            if (itemTag.getBoolean(IS_RETREAT_KEY)) {
                AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_POLTERGEIST_RETREAT);
            } else {
                AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_POLTERGEIST_SMASH);
            }
            useAndDamageItem(itemstack, level, player, 5);
            player.getCooldowns().addCooldown(this, itemTag.getBoolean(IS_RETREAT_KEY) ? CSConfig.COMMON.poltergeistShiftSkillCD.get() : CSConfig.COMMON.poltergeistSkillCD.get());
        }
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 60, 0));
        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void forceTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (entity instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 2));

                int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
                data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
                if (!data.getBoolean(IS_RETREAT_KEY)) {
                    tickMelee(itemStack, level, player, animationTimer);
                } else {
                    tickRetreat(itemStack, level, player, animationTimer);
                }
            }
        }
    }

    public void tickRetreat(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 1) {
            double range = 4;
            for (Entity entityBatch : iterateEntities(level, createAABB(player.blockPosition().above(), range))) {
                if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                    hurtNoKB(player, target, (float) (double) CSConfig.COMMON.poltergeistShiftSkillDmg.get() + getSharpnessValue(itemStack, 1.2F));
                    target.playSound(CSSoundRegistry.CS_SWORD_CLASH.get(), 0.25F, 0.5F);
                    if (target instanceof LivingMixinSupport lms) {
                        lms.setPhantomTagger(player);
                    }
                } else if (entityBatch instanceof Projectile) {
                    entityBatch.remove(Entity.RemovalReason.DISCARDED);
                }
            }
            CSEffect.createInstance(player, null, CSEffectTypes.POLTERGEIST_RETREAT, calculateXLook(player) * 2, 1, calculateZLook(player) * 2);
            sendExpandingParticles(level, ParticleTypes.SOUL, player.blockPosition(), 45, 0.5F);
            
            double multiplier = player.isOnGround() ? -2 : -1.5;
            player.setDeltaMovement(calculateXLook(player) * multiplier, 0, calculateZLook(player) * multiplier);
            player.hurtMarked = true;
            player.playSound(SoundEvents.ENDER_CHEST_OPEN, 1.0F, 1.5F);
            player.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.5F);
        }
        if (animationTimer >= 10) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void tickMelee(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        CompoundTag dataAlt = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        boolean isGiantImpact = dataAlt.getBoolean(IS_IMPACT_LARGE);
        boolean forceCreateImpact = false;

        if (animationTimer == 20) {
            double xx = calculateXLook(player) * (isGiantImpact ? 4 : 3);
            double zz = calculateZLook(player) * (isGiantImpact ? 4 : 3);

            if (!player.isOnGround() && !player.getAbilities().flying) {
                player.moveTo(player.getX(), calculateNonCollidingPos(level, player.blockPosition()).getY() + 1, player.getZ());
                forceCreateImpact = true;
            }

            double range = (!forceCreateImpact && !player.isOnGround()) ? 4 : isGiantImpact ? 4 : 6.5;
            for (Entity entityBatch : iterateEntities(level, createAABB(player.blockPosition().offset(xx, 1, zz), range))) {
                if (entityBatch instanceof LivingEntity target && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                    hurtNoKB(player, target, isGiantImpact ? (float) (double) CSConfig.COMMON.poltergeistSkillDmg.get() : 12 + getSharpnessValue(itemStack, 1.2F));
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                    target.hurtMarked = true;
                    target.setDeltaMovement((target.getX() - (player.getX() + xx)) / 4, (target.getY() - player.getY()) / 4, (target.getZ() - (player.getZ() + zz)) / 4);
                    CSWeapon.disableRunningWeapon(target);
                    if (target instanceof LivingMixinSupport lms) {
                        lms.setPhantomTagger(player);
                    }
                }
                if (entityBatch instanceof Projectile projectile) {
                    projectile.remove(Entity.RemovalReason.DISCARDED);
                }
            }

            if (isGiantImpact && !level.isClientSide()) {
                SkillCastPoltergeistWard projectile = CSEntityRegistry.POLTERGEIST_WARD.get().create(level);
                projectile.setOwnerUuid(player.getUUID());
                projectile.moveTo(player.getX() + xx, player.getY(), player.getZ() + zz);
                level.addFreshEntity(projectile);
            }

            if (player.isOnGround() || forceCreateImpact) {
                CSEffect.createInstance(player, null, isGiantImpact ? CSEffectTypes.POLTERGEIST_IMPACT_CRACK_LARGE : CSEffectTypes.POLTERGEIST_IMPACT_CRACK, xx, isGiantImpact ? -1.55 : -0.35, zz);
                if (!isGiantImpact) {
                    CSEffect.createInstance(player, null, CSEffectTypes.POLTERGEIST_WARD_SUMMON_SMALL, xx, 1, zz);
                }
                if (isGiantImpact) {
                    shakeScreensForNearbyPlayers(player, player.level, 24, 60, 30,  0.035F);
                } else {
                    shakeScreensForNearbyPlayers(player, player.level, 12, 30, 15,  0.01F);
                }
            } else {
                player.playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 0.5F);
            }

            addComboPoint(itemStack, player);
            if (!player.isOnGround()) {
                player.getCooldowns().removeCooldown(this);
                data.putInt(ANIMATION_TIMER_KEY, 0);
                data.putBoolean(ANIMATION_BEGUN_KEY, false);
            }
        }

        if (animationTimer >= 25) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void addComboPoint(ItemStack itemStack, Player player) {
        CompoundTag data1 = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        boolean isImpactLarge = data1.getBoolean(IS_IMPACT_LARGE);
        if (!isImpactLarge && data1.getInt(SMASH_COUNT_FOR_PASSIVE) < 9) {
            player.playSound(SoundEvents.ENDERMAN_TELEPORT);
            data1.putInt(SMASH_COUNT_FOR_PASSIVE, data1.getInt(SMASH_COUNT_FOR_PASSIVE) + 1);
        } else if (!isImpactLarge && data1.getInt(SMASH_COUNT_FOR_PASSIVE) >= 9) {
            player.playSound(SoundEvents.END_PORTAL_SPAWN);
            data1.putBoolean(IS_IMPACT_LARGE, true);
        } else if (isImpactLarge) {
            data1.putBoolean(IS_IMPACT_LARGE, false);
            data1.putInt(SMASH_COUNT_FOR_PASSIVE, 0);
        }
    }

    public BlockPos calculateNonCollidingPos(Level level, BlockPos pos) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        do {
            mutablePos.move(Direction.DOWN);
        } while (mutablePos.getY() > level.getMinBuildHeight() && level.getBlockState(mutablePos).isPathfindable(level, mutablePos, PathComputationType.LAND));
        return new BlockPos(mutablePos.getX(), mutablePos.getY(), mutablePos.getZ());
    }

    public void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
        LivingEntity entity = event.getEntity();
        CompoundTag tagR = mainHandItem.getItem().getShareTag(entity.getMainHandItem());
        CompoundTag tagL = offHandItem.getItem().getShareTag(entity.getOffhandItem());
        if ((tagR != null && (tagR.getBoolean(ANIMATION_BEGUN_KEY)) || (tagL != null && (tagL.getBoolean(ANIMATION_BEGUN_KEY))))) {
            event.setAmount(event.getAmount() * 0.5F);
        }
    }
}
