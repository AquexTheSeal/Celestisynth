package com.aqutheseal.celestisynth.common.item;

import com.aqutheseal.celestisynth.client.animation.AnimationManager;
import com.aqutheseal.celestisynth.common.entity.BreezebreakerTornado;
import com.aqutheseal.celestisynth.common.entity.CSEffect;
import com.aqutheseal.celestisynth.common.entity.CSEntities;
import com.aqutheseal.celestisynth.common.entity.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.common.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.common.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.common.sound.CSSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BreezebreakerItem extends SwordItem implements CSWeapon {
    public static final String BB_COMBO_POINTS = "cs.attackIndex";
    public static final String AT_BUFF_STATE = "cs.buffState";
    public static final String BUFF_STATE_LIMITER = "cs.buffStateLimiter";

    public static final String ATTACK_INDEX = "cs.attackIndex";
    public static final String IS_SHIFT_RIGHT = "cs.isShiftRight";
    public static final int
            ATTACK_NORMAL_SINGLE = 0,
            ATTACK_NORMAL_DOUBLE = 1,
            ATTACK_SHIFT = 2,
            ATTACK_SPRINT = 3,
            ATTACK_MIDAIR = 4;

    public BreezebreakerItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public int getSkillsAmount() {
        return 5;
    }

    @Override
    public int getPassiveAmount() {
        return 2;
    }

    @Override
    public boolean hasPassive() {
        return true;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        CompoundTag itemTag = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (!player.getCooldowns().isOnCooldown(itemstack.getItem()) && !itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (player.isShiftKeyDown() && player.isOnGround()) {
                boolean isMainHandBreezebreaker = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof BreezebreakerItem;
                boolean isOffHandBreezebreaker = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem() instanceof BreezebreakerItem;

                itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
                addComboPoint(itemstack, player);
                itemTag.putInt(ATTACK_INDEX, ATTACK_SHIFT);

                if (hand == InteractionHand.MAIN_HAND && !isOffHandBreezebreaker) {
                    AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_RIGHT);
                    itemTag.putBoolean(IS_SHIFT_RIGHT, true);
                } else if (hand == InteractionHand.OFF_HAND && !isMainHandBreezebreaker) {
                    AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_LEFT);
                    itemTag.putBoolean(IS_SHIFT_RIGHT, false);
                } else if (isMainHandBreezebreaker || isOffHandBreezebreaker) {
                    boolean shouldShiftRight = level.random.nextBoolean();
                    if (shouldShiftRight) {
                        AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_RIGHT);
                    } else {
                        AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SHIFT_LEFT);
                    }
                    itemTag.putBoolean(IS_SHIFT_RIGHT, shouldShiftRight);
                }
                player.getCooldowns().addCooldown(itemstack.getItem(), buffStateModified(itemstack, 35));
                return InteractionResultHolder.success(itemstack);
            }
            if (player.isSprinting() && player.isOnGround()) {
                itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
                addComboPoint(itemstack, player);
                itemTag.putInt(ATTACK_INDEX, ATTACK_SPRINT);
                AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_SPRINT_ATTACK);
                player.getCooldowns().addCooldown(itemstack.getItem(), buffStateModified(itemstack, 15));
                return InteractionResultHolder.success(itemstack);
            }
            if (!player.isOnGround()) {
                itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
                addComboPoint(itemstack, player);
                itemTag.putInt(ATTACK_INDEX, ATTACK_MIDAIR);
                AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_JUMP_ATTACK);
                player.getCooldowns().addCooldown(itemstack.getItem(), buffStateModified(itemstack, 40));
                return InteractionResultHolder.success(itemstack);
            }
        }

        if (player.getCooldowns().isOnCooldown(itemstack.getItem()) || itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            return InteractionResultHolder.fail(itemstack);
        } else {
            player.startUsingItem(hand);
            return InteractionResultHolder.consume(itemstack);
        }
    }

    public void releaseUsing(ItemStack itemstack, @NotNull Level level, @NotNull LivingEntity entity, int i) {
        CompoundTag itemTag = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (entity instanceof Player player) {
            int dur = this.getUseDuration(itemstack) - i;
            if (dur >= 0) {
                itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
                addComboPoint(itemstack, player);
                if (dur < 10) {
                    AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_NORMAL_SINGLE);
                    itemTag.putInt(ATTACK_INDEX, ATTACK_NORMAL_SINGLE);
                } else {
                    AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_NORMAL_DOUBLE);
                    itemTag.putInt(ATTACK_INDEX, ATTACK_NORMAL_DOUBLE);
                }

                int cooldown = switch (itemTag.getInt(ATTACK_INDEX)) {
                    case ATTACK_NORMAL_SINGLE -> buffStateModified(itemstack, 15);
                    case ATTACK_NORMAL_DOUBLE -> buffStateModified(itemstack, 20);
                    default -> throw new IllegalStateException("Unexpected value: " + itemTag.getInt(ATTACK_INDEX));
                };

                player.getCooldowns().addCooldown(itemstack.getItem(), cooldown);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        CompoundTag data1 = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);

        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof BreezebreakerItem)) {
            sendExpandingParticles(level, ParticleTypes.END_ROD, entity.getX(), entity.getY(), entity.getZ(), 1, 0.1F);
        }

        if (data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (entity instanceof Player player) {
                if (player.getMainHandItem() != itemStack) {
                    player.getInventory().selected = itemSlot;
                }
                if (level.isClientSide()) {
                    if (Minecraft.getInstance().screen != null) {
                        Minecraft.getInstance().screen = null;
                    }
                }
                int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
                data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
                switch (data.getInt(ATTACK_INDEX)) {
                    case ATTACK_NORMAL_SINGLE, ATTACK_NORMAL_DOUBLE -> tickNormalAttack(itemStack, level, player, animationTimer);
                    case ATTACK_SHIFT -> tickShiftAttack(itemStack, level, player, animationTimer);
                    case ATTACK_SPRINT -> tickDashAttack(itemStack, level, player, animationTimer);
                    case ATTACK_MIDAIR -> tickMidairAttack(itemStack, level, player, animationTimer);
                    default -> throw new IllegalStateException("Unexpected value: " + data.getInt(ATTACK_INDEX));
                }
            }
        }

        if (data1.getBoolean(AT_BUFF_STATE)) {
            if (entity instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 2, 1));
                for (int i = 0; i < 45; i++) {
                    Vec3 lookDirection = player.getLookAngle().normalize();
                    double offGlobal = 0;
                    double offX = offGlobal + lookDirection.x() * -1;
                    double offY = offGlobal + lookDirection.y() * -1;
                    double offZ = offGlobal + lookDirection.z() * -1;
                    CSUtilityFunctions.sendParticles(level, ParticleTypes.POOF, entity.getX(), entity.getY() + 1, entity.getZ(), 0, offX, offY, offZ);
                }
            }

            data1.putInt(BUFF_STATE_LIMITER, data1.getInt(BUFF_STATE_LIMITER) + 1);
            if (data1.getInt(BUFF_STATE_LIMITER) >= 200) {
                data1.putBoolean(AT_BUFF_STATE, false);
                data1.putInt(BB_COMBO_POINTS, 0);
                data1.putInt(BUFF_STATE_LIMITER, 0);
            }
        }
    }

    public void tickNormalAttack(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 6 || (animationTimer == 11 && data.getInt(ATTACK_INDEX) == ATTACK_NORMAL_DOUBLE)) {
            double range = 6.0;
            double rangeSq = Mth.square(range);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, range).move(calculateXLook(player), 0, calculateZLook(player)));
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target) && target.distanceToSqr(player) < rangeSq) {
                        //constantAttack(player, target, CSConfig.COMMON.breezebreakerSkillDmg.get() + ((float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                        constantAttack(player, target, ((float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(level, ParticleTypes.POOF, target.blockPosition().above(), 15, 0);
                    }
                }
            }

            player.playSound(CSSounds.CS_WIND_STRIKE.get());
            if (data.getInt(ATTACK_INDEX) == ATTACK_NORMAL_SINGLE) {
                CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_SLASH, calculateXLook(player), 0, calculateZLook(player));
                player.playSound(CSSounds.CS_AIR_SWING.get(), 1.0F, 1.0F);
            } else if (data.getInt(ATTACK_INDEX) == ATTACK_NORMAL_DOUBLE) {
                CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_SLASH_INVERTED, calculateXLook(player), 0, calculateZLook(player));
                player.playSound(CSSounds.CS_AIR_SWING.get(), 1.0F, 2.0F);
            }
        }


        if (animationTimer >= 15 && data.getInt(ATTACK_INDEX) == ATTACK_NORMAL_SINGLE) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
        if (animationTimer >= 20 && data.getInt(ATTACK_INDEX) == ATTACK_NORMAL_DOUBLE) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void tickShiftAttack(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 10) {
            player.playSound(CSSounds.CS_WIND_STRIKE.get());
            player.playSound(CSSounds.CS_WHIRLWIND.get());
            if (!level.isClientSide()) {
                BreezebreakerTornado projectile = CSEntities.BREEZEBREAKER_TORNADO.get().create(level);
                projectile.setOwnerUuid(player.getUUID());
                projectile.setAngleX((float) calculateXLook(player));
                projectile.setAngleY((float) calculateYLook(player));
                projectile.setAngleZ((float) calculateZLook(player));
                projectile.setAddAngleX((float) calculateXLook(player));
                projectile.setAddAngleY((float) calculateYLook(player));
                projectile.setAddAngleZ((float) calculateZLook(player));
                projectile.moveTo(player.getX(), player.getY() + 1, player.getZ());
                level.addFreshEntity(projectile);
            }
        }

        if (animationTimer >= 20) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void tickDashAttack(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 10) {
            sendExpandingParticles(level, ParticleTypes.CAMPFIRE_COSY_SMOKE, player.blockPosition(), 45, 0.2F);

            if (getLookAtEntity(player, 12) instanceof LivingEntity living) {
                //constantAttack(player, living, CSConfig.COMMON.breezebreakerSprintSkillDmg.get() + ((float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                constantAttack(player, living, ((float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                living.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 60, 2));
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 2));
                sendExpandingParticles(level, ParticleTypes.FIREWORK, player.blockPosition().above(), 45, 0);
            }

            double speed = 7;
            Vec3 delta = player.getDeltaMovement().add(new Vec3(calculateXLook(player) * speed, 0, calculateZLook(player) * speed));
            this.setDeltaPlayer(player, delta);
            sendExpandingParticles(level, ParticleTypes.POOF, player.blockPosition().above(), 45, 2);
            CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_DASH, (delta.x()) * 2, 0, (delta.z()) * 2);
            player.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
            player.playSound(CSSounds.CS_IMPACT_HIT.get(), 1.0F, 1.0F);
            player.playSound(CSSounds.CS_STEP.get(), 1.0F, 1.0F);

        }
        if (animationTimer >= 20) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void tickMidairAttack(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 10) {
            double range = 7.5;
            double rangeSq = Mth.square(range);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, 2));
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target) && target.distanceToSqr(player) < rangeSq) {
                        //constantAttack(player, target, CSConfig.COMMON.breezebreakerSprintSkillDmg.get() + ((float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                        constantAttack(player, target, ((float) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));
                        sendExpandingParticles(level, ParticleTypes.POOF, target.blockPosition().above(), 45, 0);
                    }
                }
            }

            CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_WHEEL, 0, -1, 0);
            player.playSound(CSSounds.CS_FIRE_SHOOT.get(), 1.0F, 1.0F);
            player.playSound(CSSounds.CS_AIR_SWING.get(), 1.0F, 1.0F);
            player.playSound(CSSounds.CS_WIND_STRIKE.get());
            sendExpandingParticles(level, ParticleTypes.END_ROD, player.blockPosition().above(), 75, 0);
        }

        if (animationTimer >= 25) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void addComboPoint(ItemStack itemStack, Player player) {
        CompoundTag data1 = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        if (data1.getInt(BB_COMBO_POINTS) < 10) {
            player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP);
            data1.putInt(BB_COMBO_POINTS, data1.getInt(BB_COMBO_POINTS) + 1);
        } else {
            player.playSound(SoundEvents.ELDER_GUARDIAN_CURSE);
            data1.putBoolean(AT_BUFF_STATE, !data1.getBoolean(AT_BUFF_STATE));
            data1.putInt(BB_COMBO_POINTS, 0);
        }
    }

    public int buffStateModified(ItemStack itemStack, int originalValue) {
        CompoundTag data1 = itemStack.getOrCreateTagElement(CS_EXTRAS_ELEMENT);
        if (data1.getBoolean(AT_BUFF_STATE)) {
            return originalValue / 2;
        }
        return originalValue;
    }

    /*@Override
    public void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
        if (event.getSource().is(DamageTypeTags.IS_FALL)) {
            event.setCanceled(true);
        } else {
            event.setAmount(event.getAmount() * 2F);
        }
    }*/

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }
}