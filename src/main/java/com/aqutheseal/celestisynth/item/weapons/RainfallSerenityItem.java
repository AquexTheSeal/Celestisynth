package com.aqutheseal.celestisynth.item.weapons;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.UtilRainfallArrow;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSParticleRegistry;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class RainfallSerenityItem extends BowItem implements CSWeapon {
    public static float SPEED = 7.5F;

    public RainfallSerenityItem(Properties pProperties) {
        super(pProperties);
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
    public int getSkillsAmount() {
        return 2;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        CompoundTag data = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, true);
        if (ret != null) return ret;

        pPlayer.startUsingItem(pHand);
        data.putBoolean(ANIMATION_BEGUN_KEY, true);
        if (pHand == InteractionHand.MAIN_HAND) {
            AnimationManager.playAnimation(pLevel, AnimationManager.AnimationsList.ANIM_RAINFALL_AIM_RIGHT);
        } else {
            AnimationManager.playAnimation(pLevel, AnimationManager.AnimationsList.ANIM_RAINFALL_AIM_LEFT);
        }


        return InteractionResultHolder.consume(itemstack);
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        super.onUsingTick(stack, player, count);
        if (player.getUsedItemHand() == InteractionHand.MAIN_HAND) {
            player.setYBodyRot(player.getYRot() - 45);
        } else {
            player.setYBodyRot(player.getYRot() + 90);
        }
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            CompoundTag data = pStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(pStack, i);

            AnimationManager.playAnimation(pLevel, AnimationManager.AnimationsList.CLEAR);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);

            if (!((double)f < 1.0D)) {
                if (f == 1.0F) {
                    CSEffect.createInstance(player, null, CSEffectTypes.RAINFALL_SHOOT, calculateXLook(player) * 2, 0.5 + (calculateYLook(player, 5) * 1), calculateZLook(player) * 2);
                    player.setDeltaMovement(player.getDeltaMovement().subtract(calculateXLook(player) * 0.5, 0, calculateZLook(player) * 0.5));
                }
                int amount = 50;
                float expansionMultiplier = 0.5F;
                for (int e = 0; e < amount; e++) {
                    Random random = new Random();
                    double angle = random.nextDouble() * 2 * Math.PI;
                    float offX = (float) Math.cos(angle) * expansionMultiplier;
                    float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
                    float offZ = (float) Math.sin(angle) * expansionMultiplier;
                    CSUtilityFunctions.sendParticles(player.level, CSParticleRegistry.RAINFALL_ENERGY_SMALL.get(), player.getX(), player.getY(), player.getZ(), 0, offX, offY, offZ);
                }

                List<Float> angles = new ArrayList<>();
                angles.add(0.0F);
                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pStack) > 0) {
                    if (pLevel.random.nextBoolean()) {
                        angles.add(-15.0F);
                        angles.add(15.0F);
                    } else {
                        angles.add(-30.0F);
                        angles.add(30.0F);
                    }
                    if (pLevel.random.nextInt(3) == 0) {
                        angles.add(-30 + (pLevel.random.nextFloat() * 60.0F));
                    }
                }
                for (float angle : angles) {
                    if (!pLevel.isClientSide) {
                        UtilRainfallArrow rainfallArrow = new UtilRainfallArrow(pLevel, player);
                        rainfallArrow = (UtilRainfallArrow) customArrow(rainfallArrow);
                        rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        rainfallArrow.setOrigin(new BlockPos(player.getX(), player.getY() + 1.5, player.getZ()));
                        rainfallArrow.setPierceLevel((byte) 3);
                        rainfallArrow.setBaseDamage(CSConfig.COMMON.rainfallSerenityArrowDmg.get() + (pLevel.random.nextDouble() * 3));
                        rainfallArrow.setImbueQuasar(true);

                        Vec3 vec31 = pEntityLiving.getUpVector(1.0F);
                        Quaternion quaternion = new Quaternion(new Vector3f(vec31), angle, true);
                        Vec3 vec3 = pEntityLiving.getViewVector(1.0F);
                        Vector3f vector3f = new Vector3f(vec3);
                        vector3f.transform(quaternion);

                        rainfallArrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), f * 3.0F, 0);

                        if (f == 1.0F) {
                            rainfallArrow.setStrong(true);
                        }

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, pStack);
                        if (j > 0) {
                            rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + j);
                        }

                        int p = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, pStack);
                        if (p > 0) {
                            rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + (p * 1.75));
                        }

                        int m = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pStack);
                        if (m > 0) {
                            rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() * 0.75F);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, pStack);
                        if (k > 0) {
                            rainfallArrow.setKnockback(k);
                        }

                        int fl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack);
                        if (fl > 0) {
                            rainfallArrow.setFlaming(true);
                        }

                        pLevel.addFreshEntity(rainfallArrow);
                    }

                    pStack.hurtAndBreak(1, player, (p_40665_) -> {
                        p_40665_.broadcastBreakEvent(player.getUsedItemHand());
                    });
                }

                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), CSSoundRegistry.CS_LASER_SHOOT.get(), SoundSource.PLAYERS, 0.7F * f, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        return super.customArrow(arrow);
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        List<Enchantment> enchantments = new ArrayList<>();
        enchantments.add(Enchantments.POWER_ARROWS);
        enchantments.add(Enchantments.PUNCH_ARROWS);
        enchantments.add(Enchantments.FLAMING_ARROWS);
        enchantments.add(Enchantments.MULTISHOT);
        enchantments.add(Enchantments.PIERCING);

         if (enchantments.contains(enchantment)) {
             return true;
         }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public float getDrawSpeed(ItemStack stack) {
        float i = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack);
        return (float) (double) CSConfig.COMMON.rainfallSerenityDrawSpeed.get() + i * 5;
    }

    public static float getPowerForTime(ItemStack stack, int pCharge) {
        float f = (float)pCharge / ((RainfallSerenityItem) stack.getItem()).getDrawSpeed(stack);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }

    @Override
    public Predicate<ItemStack> getAllSupportedProjectiles() {
        return null;
    }

    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }
}
