package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;

public class CSDamageTypes {
    public static ResourceKey<DamageType> RAPID_PLAYER_ATTACK = ResourceKey.create(Registries.DAMAGE_TYPE,
            Celestisynth.prefix("rapid_player_attack")
    );
}
