package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.common.compat.bettercombat.SwingParticleContainer;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.UUID;

public class KeresItem extends SkilledSwordItem implements CSGeoItem {
    public static final String ATTACK_SPEED_STACK = "cs.keresStack";

    public KeresItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "keres";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public void addExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> map) {
        if (CSCompatManager.checkIronsSpellbooks()) {
            map.put(AttributeRegistry.BLOOD_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Item blood spell power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
            map.put(AttributeRegistry.BLOOD_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Item blood resist", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
        );
    }

    @Override
    public @Nullable SwingParticleContainer getSwingContainer() {
        return new SwingParticleContainer(CSParticleTypes.KERES_ASH.get(), 2.8F);
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
        boolean flag = super.hurtEnemy(itemStack, entity, source);
        if (flag) {
            source.heal((float) (getDamage() * 0.1));
            this.attackController(itemStack).putInt(ATTACK_SPEED_STACK, this.attackController(itemStack).getInt(ATTACK_SPEED_STACK) + 1);
            int speedAdd = this.attackController(itemStack).getInt(ATTACK_SPEED_STACK);
        }
        return flag;
    }
}
