package com.aqutheseal.celestisynth.item;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.entities.BreezebreakerTornado;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.Random;

public class BreezebreakerItem extends SwordItem implements CSWeapon {
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

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        CompoundTag itemTag = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (!player.getCooldowns().isOnCooldown(itemstack.getItem()) && !itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (player.isShiftKeyDown() && player.isOnGround()) {
                boolean isMainHandBreezebreaker = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof BreezebreakerItem;
                boolean isOffHandBreezebreaker = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem() instanceof BreezebreakerItem;

                itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
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
                player.getCooldowns().addCooldown(itemstack.getItem(), 35);
                return InteractionResultHolder.success(itemstack);
            }
            if (player.isSprinting() && player.isOnGround()) {
                return InteractionResultHolder.success(itemstack);
            }
            if (!player.isOnGround()) {
                itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
                itemTag.putInt(ATTACK_INDEX, ATTACK_MIDAIR);
                AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_JUMP_ATTACK);
                player.getCooldowns().addCooldown(itemstack.getItem(), 40);
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

    public void releaseUsing(ItemStack itemstack, Level level, LivingEntity entity, int i) {
        CompoundTag itemTag = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (entity instanceof Player player) {
            int dur = this.getUseDuration(itemstack) - i;
            if (dur >= 0) {
                itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
                if (dur < 10) {
                    AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_NORMAL_SINGLE);
                    itemTag.putInt(ATTACK_INDEX, ATTACK_NORMAL_SINGLE);
                } else {
                    AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_BREEZEBREAKER_NORMAL_DOUBLE);
                    itemTag.putInt(ATTACK_INDEX, ATTACK_NORMAL_DOUBLE);
                }

                int cooldown = switch (itemTag.getInt(ATTACK_INDEX)) {
                    case ATTACK_NORMAL_SINGLE -> 15;
                    case ATTACK_NORMAL_DOUBLE -> 20;
                    default -> throw new IllegalStateException("Unexpected value: " + itemTag.getInt(ATTACK_INDEX));
                };

                player.getCooldowns().addCooldown(itemstack.getItem(), cooldown);
            }
        }
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (entity instanceof Player player) {
                if (player.getMainHandItem() == itemStack) {
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
                    /*
                    case ATTACK_SPRINT -> ;
                    */
                    case ATTACK_MIDAIR -> tickMidairAttack(itemStack, level, player, animationTimer);
                    default -> throw new IllegalStateException("Unexpected value: " + data.getInt(ATTACK_INDEX));
                }
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
                        constantAttack(player, target, 13 + ((float) EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));

                        for (int i = 0; i < 15; i++) {
                            Random random = new Random();
                            float offX = -0.5f + random.nextFloat();
                            float offY = -0.5f + random.nextFloat();
                            float offZ = -0.5f + random.nextFloat();
                            if (level instanceof ServerLevel) {
                                CSUtilityFunctions.sendParticles((ServerLevel) level, ParticleTypes.POOF,
                                        target.getX(), target.getY() + 1, target.getZ(), 0,
                                        offX, offY, offZ
                                );

                            }
                        }
                    }
                }
            }

            player.playSound(CSSoundRegistry.CS_WIND_STRIKE.get());
            if (data.getInt(ATTACK_INDEX) == ATTACK_NORMAL_SINGLE) {
                CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_SLASH, calculateXLook(player), 0, calculateZLook(player));
                player.playSound(CSSoundRegistry.CS_AIR_SWING.get(), 1.0F, 1.0F);
            } else if (data.getInt(ATTACK_INDEX) == ATTACK_NORMAL_DOUBLE) {
                CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_SLASH_INVERTED, calculateXLook(player), 0, calculateZLook(player));
                player.playSound(CSSoundRegistry.CS_AIR_SWING.get(), 1.0F, 2.0F);
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
            if (!level.isClientSide()) {
                BreezebreakerTornado projectile = CSEntityRegistry.BREEZEBREAKER_TORNADO.get().create(level);
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

    public void tickMidairAttack(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 10) {
            double range = 7.5;
            double rangeSq = Mth.square(range);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, range));
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target) && target.distanceToSqr(player) < rangeSq) {
                        constantAttack(player, target, 8 + ((float) EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, itemStack)));
                        target.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 40, 1));

                        for (int i = 0; i < 45; i++) {
                            Random random = new Random();
                            float offX = -0.5f + random.nextFloat();
                            float offY = -0.5f + random.nextFloat();
                            float offZ = -0.5f + random.nextFloat();
                            if (level instanceof ServerLevel) {
                                CSUtilityFunctions.sendParticles((ServerLevel) level, ParticleTypes.POOF,
                                        target.getX(), target.getY() + 1, target.getZ(), 0,
                                        offX, offY, offZ
                                );

                            }
                        }
                    }
                }
            }

            CSEffect.createInstance(player, null, CSEffectTypes.BREEZEBREAKER_SLASH_VERTICAL, 0, -1, 0);
            player.playSound(CSSoundRegistry.CS_FIRE_SHOOT.get(), 1.0F, 1.0F);
            player.playSound(CSSoundRegistry.CS_AIR_SWING.get(), 1.0F, 1.0F);
            player.playSound(CSSoundRegistry.CS_WIND_STRIKE.get());

            for (int i = 0; i < 75; i++) {
                Random random = new Random();
                float offX = -0.5f + random.nextFloat();
                float offY = -0.5f + random.nextFloat();
                float offZ = -0.5f + random.nextFloat();
                if (level instanceof ServerLevel) {
                    CSUtilityFunctions.sendParticles((ServerLevel) level, ParticleTypes.END_ROD,
                            player.getX(), player.getY() + 1, player.getZ(), 0,
                            offX, offY, offZ
                    );

                }
            }
        }

        if (animationTimer >= 25) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    @Override
    public void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
        if (event.getSource().is(DamageTypeTags.IS_FALL)) {
            event.setCanceled(true);
        }
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }
}