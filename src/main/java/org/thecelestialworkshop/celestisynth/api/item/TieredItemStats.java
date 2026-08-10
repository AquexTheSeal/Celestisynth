package org.thecelestialworkshop.celestisynth.api.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Map;

public record TieredItemStats(Map<Attribute, AttributeModifierCodecHolder> modifiers) {
    public static final Codec<TieredItemStats> CODEC = RecordCodecBuilder.create(inst -> inst.group(
            Codec.unboundedMap(BuiltInRegistries.ATTRIBUTE.byNameCodec(), AttributeModifierCodecHolder.CODEC).fieldOf("modifiers").forGetter(TieredItemStats::modifiers))
            .apply(inst, TieredItemStats::new));

    public record AttributeModifierCodecHolder(double modAmount, AttributeModifier.Operation modOperation) {
        public static final Codec<AttributeModifierCodecHolder> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                Codec.DOUBLE.fieldOf("modAmount").forGetter(AttributeModifierCodecHolder::modAmount),
                Codec.STRING.xmap(AttributeModifier.Operation::valueOf, Enum::name).fieldOf("modOperation").forGetter(AttributeModifierCodecHolder::modOperation))
                .apply(inst, AttributeModifierCodecHolder::new));
    }
}
