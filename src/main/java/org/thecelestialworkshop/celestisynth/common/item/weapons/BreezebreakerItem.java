package org.thecelestialworkshop.celestisynth.common.item.weapons;

import org.thecelestialworkshop.celestisynth.api.item.CSGeoItem;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.attack.breezebreaker.*;
import org.thecelestialworkshop.celestisynth.common.item.base.SkilledSwordItem;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class BreezebreakerItem extends SkilledSwordItem implements CSGeoItem {
    public static final String BB_COMBO_POINTS = "cs.bbCombo";
    public static final String AT_BUFF_STATE = "cs.bbBuffState";
    public static final String BUFF_STATE_LIMITER = "cs.bbBuffStateLimiter";

    public BreezebreakerItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "breezebreaker";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration) {
        return ImmutableList.of(
                new BreezebreakerGalestormAttack(player, stack, useDuration),
                new BreezebreakerDualGalestormAttack(player, stack, useDuration),
                new BreezebreakerWheelAttack(player, stack, useDuration),
                new BreezebreakerWhirlwindAttack(player, stack, useDuration),
                new BreezebreakerWindRoarAttack(player, stack, useDuration)
        );
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
    public void inventoryTick(ItemStack itemStack, Level level, Entity owner, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, level, owner, itemSlot, isSelected);

        CompoundTag extrasData = org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.getOrCreateLiveTag(itemStack, org.thecelestialworkshop.celestisynth.common.registry.CSDataComponents.CS_EXTRAS);

        if (owner instanceof Player playerOwner && (isSelected || playerOwner.getOffhandItem().getItem() instanceof BreezebreakerItem)) sendExpandingParticles(level, ParticleTypes.END_ROD, owner.getX(), owner.getY(), owner.getZ(), 1, 0.1F);
        if (extrasData.getBoolean(AT_BUFF_STATE)) {
            if (owner instanceof Player player) {
                player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SPEED, 2, 1));
                double radius = 1.5 + player.getBbWidth();
                double speed = 0.5;
                double offX = radius * Math.sin(speed * player.tickCount);
                double offY = player.getBbHeight() / 2;
                double offZ = radius * Math.cos(speed * player.tickCount);

                ParticleUtil.sendParticle(level, CSParticleTypes.BREEZEBROKEN.get(), player.getX() + offX, player.getY() + offY, player.getZ() + offZ);
            }

            extrasData.putInt(BUFF_STATE_LIMITER, extrasData.getInt(BUFF_STATE_LIMITER) + 1);
            if (extrasData.getInt(BUFF_STATE_LIMITER) >= 200) {
                extrasData.putBoolean(AT_BUFF_STATE, false);
                extrasData.putInt(BB_COMBO_POINTS, 0);
                extrasData.putInt(BUFF_STATE_LIMITER, 0);
            }
        }
    }

    @Override
    public void onPlayerHurt(LivingIncomingDamageEvent event, ItemStack stack) {
        if (event.getSource() == event.getEntity().damageSources().fall()) {
            event.setCanceled(true);
        } else {
            event.setAmount(event.getAmount() * 1.65F);
        }
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, net.minecraft.world.entity.LivingEntity useEntity) {
        return 72000;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.BOW;
    }
}
