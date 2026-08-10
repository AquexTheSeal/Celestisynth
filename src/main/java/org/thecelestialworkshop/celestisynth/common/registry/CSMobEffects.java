package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.mobeffect.CSMobEffect;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.awt.*;

public class CSMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(Registries.MOB_EFFECT, Celestisynth.MODID);

    public static final DeferredHolder<MobEffect, MobEffect> HELLBANE = MOB_EFFECTS.register("hellbane", () -> (new CSMobEffect(MobEffectCategory.BENEFICIAL, Color.RED.getRGB()))
            .addAttributeModifier(Attributes.ATTACK_SPEED, Celestisynth.prefix("effect.hellbane"), 0.5F, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
    );

    public static final DeferredHolder<MobEffect, MobEffect> CURSEBANE = MOB_EFFECTS.register("cursebane", () -> (new CSMobEffect(MobEffectCategory.HARMFUL, Color.BLACK.getRGB()))
    );
}
