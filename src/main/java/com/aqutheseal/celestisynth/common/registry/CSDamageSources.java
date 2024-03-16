package com.aqutheseal.celestisynth.common.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CSDamageSources {
    private final Registry<DamageType> damageTypes;

    public CSDamageSources(RegistryAccess pRegistry) {
        this.damageTypes = pRegistry.registryOrThrow(Registries.DAMAGE_TYPE);
    }

    public static CSDamageSources instance(Level level) {
        return new CSDamageSources(level.registryAccess());
    }

    private DamageSource source(ResourceKey<DamageType> pDamageTypeKey) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(pDamageTypeKey));
    }

    private DamageSource source(ResourceKey<DamageType> pDamageTypeKey, @Nullable Entity pEntity) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(pDamageTypeKey), pEntity);
    }

    private DamageSource source(ResourceKey<DamageType> pDamageTypeKey, @Nullable Entity pCausingEntity, @Nullable Entity pDirectEntity) {
        return new DamageSource(this.damageTypes.getHolderOrThrow(pDamageTypeKey), pCausingEntity, pDirectEntity);
    }

    public DamageSource basicPlayerAttack(LivingEntity pPlayer) {
        return this.source(CSDamageTypes.BASIC_PLAYER_ATTACK, pPlayer);
    }

    public DamageSource rapidPlayerAttack() {
        return this.source(CSDamageTypes.RAPID_PLAYER_ATTACK);
    }

    public DamageSource rapidPlayerAttack(LivingEntity pPlayer) {
        return this.source(CSDamageTypes.RAPID_PLAYER_ATTACK, pPlayer);
    }

    public DamageSource basicPlayerAttackWithoutKB(LivingEntity pPlayer) {
        return this.source(CSDamageTypes.BASIC_PLAYER_ATTACK_NOKB, pPlayer);
    }

    public DamageSource rapidPlayerAttackWithoutKB(LivingEntity pPlayer) {
        return this.source(CSDamageTypes.RAPID_PLAYER_ATTACK_NOKB, pPlayer);
    }

}
