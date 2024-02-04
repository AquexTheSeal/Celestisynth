package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.common.registry.CSDamageTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageType;

public class CSDamageTypeProvider {

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        ctx.register(CSDamageTypes.RAPID_PLAYER_ATTACK, new DamageType("rapid_player_attack", 1.0f));
    }
}
