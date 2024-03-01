package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.frostbound.FrostboundCryogenesisAttack;
import com.aqutheseal.celestisynth.common.attack.frostbound.FrostboundDanceAttack;
import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSVisualAnimation;
import com.aqutheseal.celestisynth.common.entity.projectile.FrostboundShard;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.util.SkinUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;
import java.util.function.Predicate;

public class FrostboundItem extends SkilledSwordItem implements CSGeoItem {
    public static CSVisualAnimation SPECIAL_ICE_CAST = new CSVisualAnimation("animation.cs_effect.special_ice_cast", 40);

    public FrostboundItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "frostbound";
    }

    @Override
    public String texture(ItemStack stack) {
        if (SkinUtil.getSkinIndex(stack) == 1) {
            return "skin/frostbound_seabreeze";
        } else {
            return CSGeoItem.super.texture(stack);
        }
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new FrostboundDanceAttack(player, stack),
                new FrostboundCryogenesisAttack(player, stack)
        );
    }

    @Override
    public int getSkillsAmount() {
        return 2;
    }

    @Override
    public int getPassiveAmount() {
        return 1;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        CSEntityCapabilityProvider.get(entity).ifPresent(data -> {
            data.setFrostbound(100);
        });
        entity.playSound(SoundEvents.PLAYER_HURT_FREEZE);
        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        int dur = this.getUseDuration(pStack) - pRemainingUseDuration;
        if (dur % 5 == 0) {
            for (int i = 0; i <= pLevel.random.nextInt(2); i++) {
                if (pLivingEntity instanceof Player player) {
                    double xx = pLevel.random.nextGaussian() * 3;
                    double yy = pLevel.random.nextDouble() * 2;
                    double zz = pLevel.random.nextGaussian() * 3;
                    this.shootShard(player, xx, yy, zz);
                }
            }
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.TOOT_HORN;
    }

    public void shootShard(Player player, double xx, double yy, double zz) {
        FrostboundItem.shootShard(this, player, player.level(), xx, yy, zz);
        if (getShard(player) != ItemStack.EMPTY) {
            player.playSound(SoundEvents.BLAZE_SHOOT);
        }
    }

    public static void shootShard(CSWeaponUtil util, Player player, Level level, double xx, double yy, double zz) {
        ItemStack shardStack = getShard(player);
        if (shardStack != ItemStack.EMPTY) {
            LivingEntity target = null;
            List<Entity> list = util.iterateEntities(level, util.createAABB(player.blockPosition().above(), 36)).stream().filter(entity -> entity instanceof LivingEntity && entity != player && player.hasLineOfSight(entity)).toList();
            if (!list.isEmpty()) {
                int indexLucky = level.random.nextInt(list.size());
                if (list.get(indexLucky) instanceof LivingEntity indexLuckyLiving) {
                    target = indexLuckyLiving;
                }
            }
            if (target != null) {
                FrostboundShard shard = new FrostboundShard(CSEntityTypes.FROSTBOUND_SHARD.get(), player, level);
                shard.moveTo(player.getX() + xx, shard.getY() + yy, player.getZ() + zz);
                CSEffectEntity.createInstance(player, null, CSVisualTypes.FROSTBOUND_SHARD_PULSE.get(), xx, yy + 3, zz);
                double d0 = target.getX() - (player.getX() + xx);
                double d1 = target.getY((double) 1 / 3) - (shard.getY() + yy);
                double d2 = target.getZ() - (player.getZ() + zz);
                double d3 = Math.sqrt(d0 * d0 + d2 * d2);
                shard.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, 5F);
                level.addFreshEntity(shard);
                if (!player.getAbilities().instabuild) {
                    shardStack.shrink(1);
                    if (shardStack.isEmpty()) {
                        player.getInventory().removeItem(shardStack);
                    }
                }
            }
        }
    }

    public static ItemStack getShard(Player player) {
        Predicate<ItemStack> predicate = (p) -> p.getItem() == CSItems.WINTEREIS_SHARD.get();
        ItemStack fromHandStack = ProjectileWeaponItem.getHeldProjectile(player, predicate);
        if (!fromHandStack.isEmpty()) {
            return fromHandStack;
        } else {
            for(int i = 0; i < player.getInventory().getContainerSize(); ++i) {
                ItemStack fromInventoryStack = player.getInventory().getItem(i);
                if (predicate.test(fromInventoryStack)) {
                    return fromInventoryStack;
                }
            }

            return player.getAbilities().instabuild ? new ItemStack(CSItems.WINTEREIS_SHARD.get()) : ItemStack.EMPTY;
        }
    }
}
