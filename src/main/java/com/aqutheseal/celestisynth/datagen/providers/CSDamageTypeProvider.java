package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.common.registry.CSDamageTypes;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class CSDamageTypeProvider {

    public static void bootstrap(BootstapContext<DamageType> ctx) {
        ctx.register(CSDamageTypes.BASIC_PLAYER_ATTACK, new DamageType("basic_player_attack", DamageScaling.ALWAYS, 1.0f));
        ctx.register(CSDamageTypes.RAPID_PLAYER_ATTACK, new DamageType("rapid_player_attack", DamageScaling.ALWAYS, 1.0f));
        ctx.register(CSDamageTypes.BASIC_PLAYER_ATTACK_NOKB, new DamageType("basic_player_attack_nokb", DamageScaling.ALWAYS, 1.0f));
        ctx.register(CSDamageTypes.RAPID_PLAYER_ATTACK_NOKB, new DamageType("rapid_player_attack_nokb", DamageScaling.ALWAYS, 1.0f));
    }
}
