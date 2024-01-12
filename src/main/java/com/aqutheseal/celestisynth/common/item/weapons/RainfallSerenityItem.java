package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.common.entity.base.CSEffect;
import com.aqutheseal.celestisynth.common.entity.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.common.entity.projectile.RainfallArrow;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

import java.util.List;

public class RainfallSerenityItem extends BowItem implements CSWeapon {

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
        ItemStack heldStack = pPlayer.getItemInHand(pHand);
        CompoundTag elementData = heldStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(heldStack, pLevel, pPlayer, pHand, true);

        if (ret != null) return ret;

        if (!pPlayer.isShiftKeyDown()) {
            pPlayer.startUsingItem(pHand);
            elementData.putBoolean(ANIMATION_BEGUN_KEY, true);
            if (pHand == InteractionHand.MAIN_HAND) {
                AnimationManager.playAnimation(pPlayer.level, AnimationManager.AnimationsList.ANIM_RAINFALL_AIM_RIGHT);
            } else {
                AnimationManager.playAnimation(pPlayer.level, AnimationManager.AnimationsList.ANIM_RAINFALL_AIM_LEFT);
            }

            return InteractionResultHolder.consume(heldStack);

        } else {
            if (!pPlayer.getCooldowns().isOnCooldown(this)) {
                shiftSkill(pLevel, pPlayer);
                pPlayer.getCooldowns().addCooldown(this, 200);
                return InteractionResultHolder.success(heldStack);
            }

            return InteractionResultHolder.fail(heldStack);
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        super.onUsingTick(stack, player, count);

        player.setYBodyRot(player.getYRot());
    }

    public void shiftSkill(Level level, Player player) {
        CSEffect.createInstance(player, null, CSEffectTypes.RAINFALL_VANISH, calculateXLook(player) * 3, 1, calculateZLook(player) * 3);
        CSEffect.createInstance(player, null, CSEffectTypes.RAINFALL_VANISH_CIRCLE, 0, -1.5, 0);
        player.playSound(CSSoundEvents.CS_VANISH.get());
        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 3, true, false, false));
        player.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 0, true, false, false));
    }

    @Override
    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            CompoundTag elementData = pStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);
            int useDuration = getUseDuration(pStack) - pTimeLeft;
            double curPowerFromUse = getPowerForTime(pStack, useDuration);

            AnimationManager.playAnimation(pLevel, AnimationManager.AnimationsList.CLEAR);
            elementData.putBoolean(ANIMATION_BEGUN_KEY, false);

            if (curPowerFromUse >= 1.0D) {
                if (curPowerFromUse == 1.0F) {
                    CSEffect.createInstance(player, null, CSEffectTypes.RAINFALL_SHOOT, calculateXLook(player) * 2, 0.5 + (calculateYLook(player, 5) * 1), calculateZLook(player) * 2);
                    player.setDeltaMovement(player.getDeltaMovement().subtract(calculateXLook(player) * 0.5, 0, calculateZLook(player) * 0.5));
                }

                int amount = 50;
                float expansionMultiplier = 0.5F;

                for (int e = 0; e < amount; e++) {
                    RandomSource random = player.getRandom();
                    double angle = random.nextDouble() * 2 * Math.PI;
                    float offX = (float) Math.cos(angle) * expansionMultiplier;
                    float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
                    float offZ = (float) Math.sin(angle) * expansionMultiplier;

                    ParticleUtil.sendParticles(player.level, CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), player.getX(), player.getY(), player.getZ(), 0, offX, offY, offZ);
                }

                FloatArrayList angles = new FloatArrayList();
                angles.add(0.0F);

                if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pStack) > 0) {
                    if (pLevel.random.nextBoolean()) {
                        angles.add(-15.0F);
                        angles.add(15.0F);
                    } else {
                        angles.add(-30.0F);
                        angles.add(30.0F);
                    }

                    if (pLevel.random.nextInt(3) == 0) angles.add(-30 + (pLevel.random.nextFloat() * 60.0F));
                }
                for (float angle : angles) {
                    if (!pLevel.isClientSide) {
                        BlockPos originPos = new BlockPos(player.getX(), player.getY() + 1.5, player.getZ());
                        RainfallArrow rainfallArrow = new RainfallArrow(pLevel, player);

                        rainfallArrow = (RainfallArrow) customArrow(rainfallArrow);
                        rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        rainfallArrow.setOrigin(originPos);
                        rainfallArrow.setPierceLevel((byte) 3);
                        rainfallArrow.setBaseDamage(CSConfigManager.COMMON.rainfallSerenityArrowDmg.get() + (pLevel.random.nextDouble() * 3));
                        rainfallArrow.setImbueQuasar(true);

                        Vec3 vec31 = pEntityLiving.getUpVector(1.0F);
                        Vector3f wrappedVec = new Vector3f(vec31);

                        Quaternion quaternion = new Quaternion(wrappedVec, angle, true);
                        Vec3 viewVec = pEntityLiving.getViewVector(1.0F);
                        Vector3f wrappedViewVec = new Vector3f(viewVec);

                        wrappedViewVec.transform(quaternion);
                        rainfallArrow.shoot(wrappedViewVec.x(), wrappedViewVec.y(), wrappedViewVec.z(), (float) (curPowerFromUse * 3.0F), 0);

                        int powerEnchLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, pStack);
                        int piercingEnchLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, pStack);
                        int multishotEnchLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pStack);
                        int punchEnchLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, pStack);
                        int flameEnchLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, pStack);

                        if (curPowerFromUse == 1.0F) rainfallArrow.setStrong(true);
                        if (powerEnchLvl > 0) rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + powerEnchLvl);
                        if (piercingEnchLvl > 0) rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + (piercingEnchLvl * 4));
                        if (multishotEnchLvl > 0) rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() * 0.75F);
                        if (punchEnchLvl > 0) rainfallArrow.setKnockback(punchEnchLvl);
                        if (flameEnchLvl > 0) rainfallArrow.setFlaming(true);

                        pLevel.addFreshEntity(rainfallArrow);
                    }

                    pStack.hurtAndBreak(1, player, (livingOwner) -> livingOwner.broadcastBreakEvent(player.getUsedItemHand()));
                }

                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), CSSoundEvents.CS_LASER_SHOOT.get(), SoundSource.PLAYERS, (float) (0.7F * curPowerFromUse), (float) (1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + curPowerFromUse * 0.5F));
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
        List<Enchantment> enchantments = new ObjectArrayList<>();
        enchantments.add(Enchantments.POWER_ARROWS);
        enchantments.add(Enchantments.PUNCH_ARROWS);
        enchantments.add(Enchantments.FLAMING_ARROWS);
        enchantments.add(Enchantments.MULTISHOT);
        enchantments.add(Enchantments.PIERCING);

        if (enchantment == Enchantments.PIERCING) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack) > 0) return false;
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, stack) > 0) return false;
        }

        if (enchantment == Enchantments.MULTISHOT || enchantment == Enchantments.QUICK_CHARGE) {
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack) > 0) return false;
        }

        if (enchantments.contains(enchantment)) return true;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public float getDrawSpeed(ItemStack stack) {
        float piercingEnchLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, stack);
        float quickEnchLvl = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, stack);
        return (float) (CSConfigManager.COMMON.rainfallSerenityDrawSpeed.get() + (piercingEnchLvl * 10)) / ((quickEnchLvl + 1) * 0.6f);
    }

    public static float getPowerForTime(ItemStack stack, int pCharge) {
        float totalCharge = (float) pCharge / ((RainfallSerenityItem) stack.getItem()).getDrawSpeed(stack);
        totalCharge = (totalCharge * totalCharge + totalCharge * 2.0F) / 3.0F;

        if (totalCharge > 1.0F) totalCharge = 1.0F;

        return totalCharge;
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.NONE;
    }
}
