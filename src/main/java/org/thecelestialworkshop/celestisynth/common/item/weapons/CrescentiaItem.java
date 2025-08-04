package org.thecelestialworkshop.celestisynth.common.item.weapons;

import org.thecelestialworkshop.celestisynth.api.item.CSGeoItem;
import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.attack.cresentia.CrescentiaBarrageAttack;
import org.thecelestialworkshop.celestisynth.common.attack.cresentia.CrescentiaDragonAttack;
import org.thecelestialworkshop.celestisynth.common.item.base.SkilledSwordItem;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;

public class CrescentiaItem extends SkilledSwordItem implements CSGeoItem {
    public CrescentiaItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "crescentia";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new CrescentiaBarrageAttack(player, stack),
                new CrescentiaDragonAttack(player, stack)
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
        entity.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));

        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof CrescentiaItem)) player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.DAMAGE_RESISTANCE, 2, 0));
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
    }

    public static void createCrescentiaFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z, boolean isBig) {
        RandomSource random = level.random;
        ParticleType<?>[] particleTypes = new ParticleType<?>[]{
                CSParticleTypes.CRESCENTIA_FIREWORK_PURPLE.get(),
                CSParticleTypes.CRESCENTIA_FIREWORK_PINK.get(),
                CSParticleTypes.CRESCENTIA_FIREWORK_BLUE.get()
        };
        ParticleType<?> firework = particleTypes[random.nextInt(particleTypes.length)];
        for (int i = 0; i < 45; i++) {
            double xx = random.nextGaussian() * (isBig ? 0.15 : 0.05);
            double yy = random.nextGaussian() * (isBig ? 0.15 : 0.05);
            double zz = random.nextGaussian() * (isBig ? 0.15 : 0.05);
            ParticleUtil.sendParticle(level, firework, x, y, z, xx, yy, zz);
        }
        ParticleUtil.sendParticle(level, ParticleTypes.FLASH, x, y, z);
        player.playSound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 1.0F, 0.5F + random.nextFloat());
    }

    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        List<Enchantment> enchantments = new ObjectArrayList<>();
        enchantments.add(Enchantments.MULTISHOT);

        if (enchantments.contains(enchantment)) return true;

        return super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
