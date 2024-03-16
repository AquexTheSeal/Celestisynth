package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.solaris.SolarisFullRoundAttack;
import com.aqutheseal.celestisynth.common.attack.solaris.SolarisSoulDashAttack;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.compat.bettercombat.SwingParticleContainer;
import com.aqutheseal.celestisynth.common.entity.projectile.SolarisBomb;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;
import java.util.UUID;

public class SolarisItem extends SkilledSwordItem implements CSGeoItem {
    public SolarisItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Item.Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "solaris";
    }

    @Override
    public String texture(ItemStack stack) {
        if (attackController(stack) != null && attackController(stack).getBoolean(SolarisSoulDashAttack.STARTED)) {
            return "solaris_soul";
        } else {
            return "solaris";
        }
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public void addExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> map) {
        if (CSCompatManager.checkIronsSpellbooks()) {
            map.put(AttributeRegistry.FIRE_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Item fire spell power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
            map.put(AttributeRegistry.FIRE_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Item fire resist", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new SolarisFullRoundAttack(player, stack),
                new SolarisSoulDashAttack(player, stack)
        );
    }

    @Override
    public @Nullable SwingParticleContainer getSwingContainer() {
        return new SwingParticleContainer(CSParticleTypes.SOLARIS_FLAME.get(), 0.8F);
    }

    @Override
    public int getSkillsAmount() {
        return 2;
    }

    @Override
    public int getPassiveAmount() {
        return 2;
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pLivingEntity, pStack, pRemainingUseDuration);
        int dur = this.getUseDuration(pStack) - pRemainingUseDuration;

        double xV = Mth.sin(dur) * 3;
        double zV = Mth.cos(dur) * 3;
        ParticleUtil.sendParticle(pLevel, ParticleTypes.POOF,
                pLivingEntity.getX() + xV, pLivingEntity.getY() + 1.5, pLivingEntity.getZ() + zV,
                xV * 0.05, 0, zV * 0.05
        );

        if (dur % 40 == 0) {
            pLivingEntity.playSound(CSSoundEvents.WHIRLWIND.get(), 0.2F, (float) (1.0F + (pLevel.random.nextGaussian() * 0.25)));
        }
        if (dur % 30 == 0) {
            if (pLevel instanceof ServerLevel server) {
                List<SolarisBomb> bombs = SolarisBomb.getAllBombsOwnedBy(pLivingEntity, server).filter(e -> e.distanceToSqr(pLivingEntity) <= 32).toList();
                if (bombs.size() < 5) {
                    SolarisBomb bomb = new SolarisBomb(CSEntityTypes.SOLARIS_BOMB.get(), pLivingEntity, pLevel);
                    bomb.setOwner(pLivingEntity);
                    bomb.playSound(SoundEvents.BLAZE_SHOOT);
                    pLevel.addFreshEntity(bomb);
                    for (int i = 0; i < 360; i++) {
                        double xI = Mth.sin(i) * 3;
                        double zI = Mth.cos(i) * 3;
                        ParticleUtil.sendParticle(pLevel, ParticleTypes.FLAME, pLivingEntity.getX(), pLivingEntity.getY() + 1.2, pLivingEntity.getZ(), xI * 0.05, 0, zI * 0.05);
                    }
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

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.setSecondsOnFire(5);
        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof SolarisItem)) player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.FIRE_RESISTANCE, 2, 0));
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
    }
}
