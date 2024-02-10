package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.api.item.CSWeapon;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualAnimation;
import com.aqutheseal.celestisynth.common.entity.projectile.RainfallArrow;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
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
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

public class RainfallSerenityItem extends BowItem implements CSWeapon, CSGeoItem {
    public static CSVisualAnimation SPECIAL_RAINFALL = new CSVisualAnimation("animation.cs_effect.special_rainfall", 50);

    public static final String PULL = "cs.pull";
    public static final String PULLING = "cs.pulling";

    public RainfallSerenityItem(Properties pProperties) {
        super(pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "rainfall_serenity";
    }

    @Override
    public String texture(ItemStack stack) {
        float pull = attackExtras(stack).getFloat(PULL);
        float pulling = attackExtras(stack).getFloat(PULLING);
        if (pulling == 1 && pull >= 1) {
            return "rainfall_serenity_pulling_2";
        } else if (pulling == 1 && pull >= 0.5) {
            return "rainfall_serenity_pulling_1";
        } else if (pulling >= 1) {
            return "rainfall_serenity_pulling_0";
        } else  {
            return "rainfall_serenity";
        }
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
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
                AnimationManager.playAnimation(pLevel, AnimationManager.AnimationsList.ANIM_RAINFALL_AIM_RIGHT);
            } else {
                AnimationManager.playAnimation(pLevel, AnimationManager.AnimationsList.ANIM_RAINFALL_AIM_LEFT);
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
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);

        if (pEntity instanceof LivingEntity living) {
            if (living.getUseItem() != pStack) {
                attackExtras(pStack).putFloat(PULL, 0);
                attackExtras(pStack).putFloat(PULLING, 0);
            } else {
                attackExtras(pStack).putFloat(PULL, (pStack.getUseDuration() - living.getUseItemRemainingTicks()) / ((RainfallSerenityItem) pStack.getItem()).getDrawSpeed(pStack));
                attackExtras(pStack).putFloat(PULLING, 1);
            }
        }
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        pLivingEntity.setYBodyRot(pLivingEntity.getYRot());
    }

    public void shiftSkill(Level level, Player player) {
        CSEffectEntity.createInstance(player, null, CSVisualTypes.RAINFALL_VANISH.get(), calculateXLook(player) * 3, 1, calculateZLook(player) * 3);
        CSEffectEntity.createInstance(player, null, CSVisualTypes.RAINFALL_VANISH_CIRCLE.get(), 0, -1.5, 0);
        player.playSound(CSSoundEvents.VANISH.get());
        player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SPEED, 100, 3));
        player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.INVISIBILITY, 100, 0));
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
                    CSEffectEntity.createInstance(player, null, CSVisualTypes.RAINFALL_SHOOT.get(), calculateXLook(player) * 2, 0.5 + (calculateYLook(player, 5) * 1), calculateZLook(player) * 2);
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

                    ParticleUtil.sendParticles(pLevel, CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), player.getX(), player.getY(), player.getZ(), 0, offX, offY, offZ);
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
                        BlockPos originPos = new BlockPos((int) player.getX(), (int) (player.getY() + 1.5), (int) player.getZ());
                        RainfallArrow rainfallArrow = new RainfallArrow(pLevel, player);

                        rainfallArrow = (RainfallArrow) customArrow(rainfallArrow);
                        rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                        rainfallArrow.setOrigin(originPos);
                        rainfallArrow.setPierceLevel((byte) 3);
                        rainfallArrow.setBaseDamage(CSConfigManager.COMMON.rainfallSerenityArrowDmg.get() + (pLevel.random.nextDouble() * 3));
                        rainfallArrow.setImbueQuasar(true);

                        Vec3 vec31 = pEntityLiving.getUpVector(1.0F);
                        Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(angle * ((float)Math.PI / 180F), vec31.x, vec31.y, vec31.z);
                        Vec3 vec3 =  pEntityLiving.getViewVector(1.0F);
                        Vector3f vector3f = vec3.toVector3f().rotate(quaternionf);

                        rainfallArrow.shoot(vector3f.x(), vector3f.y(), vector3f.z(), (float) (curPowerFromUse * 3.0F), 0);

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

                pLevel.playSound(null, player.getX(), player.getY(), player.getZ(), CSSoundEvents.LASER_SHOOT.get(), SoundSource.PLAYERS, (float) (0.7F * curPowerFromUse), (float) (1.0F / (pLevel.getRandom().nextFloat() * 0.4F + 1.2F) + curPowerFromUse * 0.5F));
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
