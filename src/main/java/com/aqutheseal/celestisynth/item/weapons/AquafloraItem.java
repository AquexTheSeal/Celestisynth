package com.aqutheseal.celestisynth.item.weapons;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import com.google.common.collect.Lists;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AquafloraItem extends SwordItem implements CSWeapon {
    public static final String CHECK_PASSIVE = "cs.checkPassiveIfBlooming";
    public static final String ATTACK_BLOOMING = "cs.bloomSkill";
    public static final String INITIAL_PERSPECTIVE = "cs.initPerspective";
    public static final String INITIAL_VIEW_ANGLE = "cs.initViewAngle";

    public AquafloraItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public int getSkillsAmount() {
        return 4;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player) {
            if (player.getMainHandItem() == pStack) {
                CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_SLICE, 0, 1.3, 0);
            }
            if (player.getOffhandItem() == pStack) {
                CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_SLICE_INVERTED, 0, 1.3, 0);
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        CompoundTag itemTag = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        boolean checkMain = player.getItemBySlot(EquipmentSlot.MAINHAND).getItem() instanceof AquafloraItem;
        boolean checkOff = player.getItemBySlot(EquipmentSlot.OFFHAND).getItem() instanceof AquafloraItem;

        if (!player.getCooldowns().isOnCooldown(this) && !itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
            if (player.isShiftKeyDown()) {
                onShiftSkill(itemstack, level, player);
                useAndDamageItem(itemstack, level, player, 3);
                if (!itemTag.getBoolean(CHECK_PASSIVE)) {
                    player.getCooldowns().addCooldown(this, CSConfig.COMMON.aquafloraBloomShiftSkillCD.get());
                } else {
                    player.getCooldowns().addCooldown(this, CSConfig.COMMON.aquafloraShiftSkillCD.get());
                }
            } else {
                if (!itemTag.getBoolean(CHECK_PASSIVE)) {
                    itemTag.putBoolean(ATTACK_BLOOMING, false);
                    if (hand == InteractionHand.MAIN_HAND && !checkOff) {
                        AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT);
                    } else if (hand == InteractionHand.OFF_HAND && !checkMain) {
                        AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT);
                    } else if (checkMain || checkOff) {
                        boolean shouldRight = level.random.nextBoolean();
                        if (shouldRight) {
                            AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT);
                        } else {
                            AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT);
                        }
                    }
                    player.getCooldowns().addCooldown(this, CSConfig.COMMON.aquafloraSkillCD.get());
                } else {
                    itemTag.putBoolean(ATTACK_BLOOMING, true);
                    AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_AQUAFLORA_ASSASSINATE);
                    player.getCooldowns().addCooldown(this, CSConfig.COMMON.aquafloraBloomSkillCD.get());
                }
                useAndDamageItem(itemstack, level, player, 2);
            }
        }
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public void forceTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (entity instanceof Player player) {
                int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
                data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
                if (data.getBoolean(ATTACK_BLOOMING)) {
                    tickBloomNormal(itemStack, level, player, animationTimer);
                } else {
                    tickNormal(itemStack, level, player, animationTimer);
                }
            }
        }
    }

    public void tickNormal(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 1) {
            CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_PIERCE_START, calculateXLook(player) * 2, 1.3 + (calculateYLook(player) * 3), calculateZLook(player) * 2);
            player.playSound(CSSoundRegistry.CS_BLING.get(), 0.15F, 0.5F);
            if (level.isClientSide()) {
                shakeScreens(player, 15, 5, 0.02F);
            }
        }
        if (animationTimer >= 0 && animationTimer <= 15) {
            player.playSound(CSSoundRegistry.CS_AIR_SWING.get(), 0.25F, 1.3F + level.random.nextFloat());
            CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_STAB, -0.5 + level.random.nextDouble() + calculateXLook(player) * 2, 2.2 + (-0.5 + level.random.nextDouble()) + (calculateYLook(player) * 3), -0.5 + level.random.nextDouble() + calculateZLook(player) * 2);
            List<Entity> entities = iterateEntities(level, createAABB(player.blockPosition().offset(calculateXLook(player) * 4.5, 1 + (calculateYLook(player) * 4.5), calculateZLook(player) * 4.5), 2));
            entities.addAll(iterateEntities(level, createAABB(player.blockPosition().offset(calculateXLook(player) * 3, 1 + (calculateYLook(player) * 3), calculateZLook(player) * 3), 2)));
            entities.addAll(iterateEntities(level, createAABB(player.blockPosition().offset(calculateXLook(player) * 1.5, 1 + (calculateYLook(player) * 1.5), calculateZLook(player) * 1.5), 2)));
            if (entities.size() > 0) {
                player.playSound(CSSoundRegistry.CS_BLING.get(), 0.15F, 1F + level.random.nextFloat());
            }
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        hurtNoKB(player, target, (float) (double) CSConfig.COMMON.aquafloraSkillDmg.get() + getSharpnessValue(itemStack, 0.15F));
                        createHitEffect(itemStack, level, player, target);
                    }
                }
            }
        }
        if (animationTimer >= 20) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void tickBloomNormal(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (animationTimer == 1) {
            if (level.isClientSide()) {
                data.putFloat(INITIAL_VIEW_ANGLE, player.getXRot());
                data.putInt(INITIAL_PERSPECTIVE, Minecraft.getInstance().options.getCameraType().ordinal());
            }
        }

        if (animationTimer >= 1) {
            player.setXRot(90);
            if (level.isClientSide()) {
                Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
        }

        if (animationTimer >= 15 && animationTimer % (checkDualWield(player, AquafloraItem.class) ? 2 : 5) == 0) {
            Predicate<Entity> filter = (e) -> e != player && e instanceof LivingEntity le && (player.hasLineOfSight(le) || le.hasLineOfSight(player)) &&  le.isAlive() && !player.isAlliedTo(le);
            List<LivingEntity> entities = iterateEntities(level, createAABB(player.blockPosition(), 12)).stream().filter(filter).map(LivingEntity.class::cast).toList();
            LivingEntity target = entities.size() > 0 ? entities.get(level.random.nextInt(entities.size())) : null;
            if (target == player || target == null) {
                AnimationManager.playAnimation(level, AnimationManager.AnimationsList.CLEAR);
                data.putInt(ANIMATION_TIMER_KEY, 0);
                data.putBoolean(ANIMATION_BEGUN_KEY, false);
                if (level.isClientSide()) {
                    player.setXRot(data.getFloat(INITIAL_VIEW_ANGLE));
                    Minecraft.getInstance().options.setCameraType(CameraType.values()[data.getInt(INITIAL_PERSPECTIVE)]);
                }
                return;
            }

            double oX = -4 + level.random.nextInt(8);
            double oZ = -4 + level.random.nextInt(8);

            if (level.isClientSide()) {
                double dx = target.getX() - (player.getX() + oX);
                double dz = target.getZ() - (player.getZ() + oZ);
                double yaw = -Math.atan2(dx, dz);
                yaw = yaw * (180.0 / Math.PI);
                yaw = yaw + (yaw < 0 ? 360 : 0);
                player.setYRot((float) yaw);
            }

            CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_DASH, 0, 0.55, 0);
            player.moveTo(target.blockPosition().offset(oX, 1, oZ), player.getYRot(), player.getXRot());
            CSEffect.createInstance(player, target, CSEffectTypes.AQUAFLORA_FLOWER_BIND);
            CSEffect.createInstance(player, target, CSEffectTypes.AQUAFLORA_ASSASSINATE, 0, 1, 0);
            player.playSound(CSSoundRegistry.CS_BLING.get(), 0.15F, 0.5F);
            playRandomBladeSound(player, 4);
            hurtNoKB(player, target, (float) (CSConfig.COMMON.aquafloraBloomSkillDmg.get() * (checkDualWield(player, AquafloraItem.class) ? 0.55 : 1)) + getSharpnessValue(itemStack, 0.75F));
            createAquafloraFirework(itemStack, level, player, target.getX(), target.getY() + 1, target.getZ());
        }
        if (animationTimer >= 120) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
            if (level.isClientSide()) {
                player.setXRot(data.getFloat(INITIAL_VIEW_ANGLE));
                Minecraft.getInstance().options.setCameraType(CameraType.values()[data.getInt(INITIAL_PERSPECTIVE)]);
            }
        }
    }

    public void onShiftSkill(ItemStack itemStack, Level level, Player player) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        if (data.getBoolean(CHECK_PASSIVE)) {
            AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_POLTERGEIST_RETREAT);
            sendExpandingParticles(level, ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 55, 1.2F);
            CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_FLOWER, 0, -1, 0);
            List<Entity> entities = iterateEntities(level, createAABB(player.blockPosition(), 12));
            player.playSound(CSSoundRegistry.CS_BLING.get(), 0.4F, 0.5F);
            for (Entity target : entities) {
                if (target instanceof LivingEntity lt && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                    CSEffect.createInstance(player, target, CSEffectTypes.AQUAFLORA_FLOWER_BIND);
                    hurtNoKB(player, lt, (float) (double) CSConfig.COMMON.aquafloraBloomShiftSkillDmg.get());
                    target.setDeltaMovement((player.getX() - target.getX()) * 0.35, (player.getY() - target.getY()) * 0.35, (player.getZ() - target.getZ()) * 0.35);
                }
            }
            double check = player.isOnGround() ? 2 : 0.75;
            player.setDeltaMovement(player.getDeltaMovement().subtract(calculateXLook(player) * check, 0, calculateZLook(player) * check));
        } else {
            AnimationManager.playAnimation(level, AnimationManager.AnimationsList.ANIM_AQUAFLORA_BASH);
            List<Entity> entities = iterateEntities(level, createAABB(player.blockPosition().offset(calculateXLook(player) * 3, 1 + (calculateYLook(player) * 3), calculateZLook(player) * 3), 3));
            player.playSound(SoundEvents.WITHER_BREAK_BLOCK, 0.7F, 1.5F);
            CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_BASH, calculateXLook(player) * 2, 1.5, calculateZLook(player) * 2);
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        target.setDeltaMovement((target.getX() - player.getX()) * 0.4,   1, (target.getZ() - player.getZ()) * 0.4);
                        hurtNoKB(player, target, (float) (double) CSConfig.COMMON.aquafloraShiftSkillDmg.get() + getSharpnessValue(itemStack, 1F));
                        createHitEffect(itemStack, level, player, target);
                        CSWeapon.disableRunningWeapon(target);
                    }
                }
            }
            double check = player.isOnGround() ? 0.3 : 0.14;
            if (level.isClientSide()) {
                shakeScreens(player, 3, 2, 0.015F);
            }
            player.setDeltaMovement(player.getDeltaMovement().add(calculateXLook(player) * check, 0, calculateZLook(player) * check));
        }
        data.putBoolean(CHECK_PASSIVE, !data.getBoolean(CHECK_PASSIVE));
        data.putBoolean(ANIMATION_BEGUN_KEY, false);
    }

    public void createHitEffect(ItemStack itemStack, Level level, Player player, LivingEntity target) {
        Random random = new Random();
        sendExpandingParticles(level, ParticleTypes.END_ROD, target.getX(), target.getY(), target.getZ(), 25, 0.5F);
        player.playSound(CSSoundRegistry.CS_BLING.get(), 0.2F, 1F + random.nextFloat());
    }

    public static void createAquafloraFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z) {
        Random random = new Random();
        ItemStack star = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag compoundtag = star.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        DyeColor[] allowedColors = new DyeColor[]{DyeColor.PINK, DyeColor.GREEN, DyeColor.WHITE};
        list.add(allowedColors[random.nextInt(allowedColors.length)].getFireworkColor());
        compoundtag.putIntArray("Colors", list);
        compoundtag.putByte("Type", (byte)(FireworkRocketItem.Shape.SMALL_BALL.getId()));
        CompoundTag itemCompound = itemStack.getOrCreateTagElement("Fireworks");
        ListTag listtag = new ListTag();
        CompoundTag starCompound = star.getTagElement("Explosion");
        if (starCompound != null) {
            listtag.add(starCompound);
        }
        itemCompound.putByte("Flight", (byte) 3);
        if (!listtag.isEmpty()) {
            itemCompound.put("Explosions", listtag);
        }
        level.createFireworks(x, y, z, 0.01, 0.01, 0.01, itemCompound);
    }
}
