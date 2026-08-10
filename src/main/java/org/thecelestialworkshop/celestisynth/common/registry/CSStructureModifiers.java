package org.thecelestialworkshop.celestisynth.common.registry;

import java.util.function.Supplier;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.neoforged.neoforge.common.world.ModifiableStructureInfo;
import net.neoforged.neoforge.common.world.StructureModifier;
import net.neoforged.neoforge.common.world.StructureSettingsBuilder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;
import java.util.function.Function;

public class CSStructureModifiers {
    public static final Codec<HolderSet<Structure>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.STRUCTURE, Structure.DIRECT_CODEC);

    public static final DeferredRegister<MapCodec<? extends StructureModifier>> STRUCTURE_MODIFIER_SERIALIZERS = DeferredRegister.create(NeoForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, Celestisynth.MODID);

    public static final Supplier<MapCodec<AddSpawnsStructureModifier>> ADD_SPAWNS_STRUCTURE_CODEC = STRUCTURE_MODIFIER_SERIALIZERS.register("add_spawns", () ->
            RecordCodecBuilder.mapCodec(builder -> builder.group(
                    LIST_CODEC.fieldOf("structure").forGetter(AddSpawnsStructureModifier::structures),
                    Codec.either(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of),
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
                            ).fieldOf("spawners").forGetter(AddSpawnsStructureModifier::spawners)
            ).apply(builder, AddSpawnsStructureModifier::new))
    );

    public static final Supplier<MapCodec<AddSpawnsStructureTagModifier>> ADD_SPAWNS_STRUCTURE_TAG_CODEC = STRUCTURE_MODIFIER_SERIALIZERS.register("add_spawns_tag", () ->
            RecordCodecBuilder.mapCodec(builder -> builder.group(
                    TagKey.codec(Registries.STRUCTURE).fieldOf("structure_tags").forGetter(AddSpawnsStructureTagModifier::structureTag),
                    Codec.either(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of),
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
                    ).fieldOf("spawners").forGetter(AddSpawnsStructureTagModifier::spawners)
            ).apply(builder, AddSpawnsStructureTagModifier::new))
    );


    public record AddSpawnsStructureModifier(HolderSet<Structure> structures, List<MobSpawnSettings.SpawnerData> spawners) implements StructureModifier {
        @Override
        public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
            if(phase == Phase.ADD && this.structures.contains(structure)) {
                StructureSettingsBuilder structureSettings = builder.getStructureSettings();
                for (MobSpawnSettings.SpawnerData spawner : this.spawners) {
                    EntityType<?> type = spawner.type;
                    structureSettings.getOrAddSpawnOverrides(type.getCategory()).addSpawn(spawner);
                }
            }
        }

        @Override
        public MapCodec<? extends StructureModifier> codec() {
            return CSStructureModifiers.ADD_SPAWNS_STRUCTURE_CODEC.get();
        }
    }

    public record AddSpawnsStructureTagModifier(TagKey<Structure> structureTag, List<MobSpawnSettings.SpawnerData> spawners) implements StructureModifier {
        @Override
        public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
            if(phase == Phase.ADD && structure.is(structureTag)) {
                StructureSettingsBuilder structureSettings = builder.getStructureSettings();
                for (MobSpawnSettings.SpawnerData spawner : this.spawners) {
                    EntityType<?> type = spawner.type;
                    structureSettings.getOrAddSpawnOverrides(type.getCategory()).addSpawn(spawner);
                }
            }
        }

        @Override
        public MapCodec<? extends StructureModifier> codec() {
            return CSStructureModifiers.ADD_SPAWNS_STRUCTURE_TAG_CODEC.get();
        }
    }
}
