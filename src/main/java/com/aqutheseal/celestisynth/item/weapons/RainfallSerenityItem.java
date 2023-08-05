package com.aqutheseal.celestisynth.item.weapons;

import com.aqutheseal.celestisynth.entities.UtilRainfallArrow;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class RainfallSerenityItem extends ProjectileWeaponItem {
    public static float SPEED = 7.5F;
    public RainfallSerenityItem(Properties pProperties) {
        super(pProperties);
    }

    public static float getPowerForTime(int pCharge) {
        float f = (float)pCharge / SPEED;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        InteractionResultHolder<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemstack, pLevel, pPlayer, pHand, true);
        if (ret != null) return ret;

        pPlayer.startUsingItem(pHand);
        return InteractionResultHolder.consume(itemstack);
    }

    public void releaseUsing(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft) {
        if (pEntityLiving instanceof Player player) {
            int i = this.getUseDuration(pStack) - pTimeLeft;
            float f = getPowerForTime(i);
            if (!((double)f < 0.1D)) {
                if (!pLevel.isClientSide) {
                    List<Float> angles = new ArrayList<>();
                    angles.add(0.0F);
                    if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, pStack) > 0) {
                        angles.add(-10.0F);
                        angles.add(10.0F);
                    }
                    for (float angle : angles) {
                        AbstractArrow rainfallArrow = new UtilRainfallArrow(pLevel, player);
                        rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

                        Vec3 vec31 = pEntityLiving.getUpVector(1.0F);
                        Quaternion quaternion = new Quaternion(new Vector3f(vec31), angle, true);
                        Vec3 vec3 = pEntityLiving.getViewVector(1.0F);
                        Vector3f vector3f = new Vector3f(vec3);
                        vector3f.transform(quaternion);

                        rainfallArrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), f * 9.0F, 1);

                        int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, pStack);
                        if (j > 0) {
                            rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, pStack);
                        if (k > 0) {
                            rainfallArrow.setKnockback(k);
                        }

                        pLevel.addFreshEntity(rainfallArrow);
                    }
                }

                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), CSSoundRegistry.CS_LASER_SHOOT.get(), SoundSource.PLAYERS, 1.0F, 1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                player.awardStat(Stats.ITEM_USED.get(this));
            }
        }
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
         if (enchantment == Enchantments.FLAMING_ARROWS) {
            return false;
        }
        if (enchantment == Enchantments.MULTISHOT) {
            return true;
        }
        return super.canApplyAtEnchantingTable(stack, enchantment);
    }

    public int getUseDuration(ItemStack pStack) {
        return 72000;
    }

    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.BOW;
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
