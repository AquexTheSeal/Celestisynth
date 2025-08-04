package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.ModifiableStructureInfo;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.common.world.StructureSettingsBuilder;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Function;

public class CSStructureModifiers {
    public static final Codec<HolderSet<Structure>> LIST_CODEC = RegistryCodecs.homogeneousList(Registries.STRUCTURE, Structure.DIRECT_CODEC);

    public static final DeferredRegister<Codec<? extends StructureModifier>> STRUCTURE_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, Celestisynth.MODID);

    public static final RegistryObject<Codec<AddSpawnsStructureModifier>> ADD_SPAWNS_STRUCTURE_CODEC = STRUCTURE_MODIFIER_SERIALIZERS.register("add_spawns", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    LIST_CODEC.fieldOf("structure").forGetter(AddSpawnsStructureModifier::structures),
                    new ExtraCodecs.EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
                            either -> either.map(Function.identity(), List::of),
                            list -> list.size() == 1 ? Either.right(list.get(0)) : Either.left(list)
                            ).fieldOf("spawners").forGetter(AddSpawnsStructureModifier::spawners)
            ).apply(builder, AddSpawnsStructureModifier::new))
    );

    public static final RegistryObject<Codec<AddSpawnsStructureTagModifier>> ADD_SPAWNS_STRUCTURE_TAG_CODEC = STRUCTURE_MODIFIER_SERIALIZERS.register("add_spawns_tag", () ->
            RecordCodecBuilder.create(builder -> builder.group(
                    TagKey.codec(Registries.STRUCTURE).fieldOf("structure_tags").forGetter(AddSpawnsStructureTagModifier::structureTag),
                    new ExtraCodecs.EitherCodec<>(MobSpawnSettings.SpawnerData.CODEC.listOf(), MobSpawnSettings.SpawnerData.CODEC).xmap(
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
        public Codec<? extends StructureModifier> codec() {
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
        public Codec<? extends StructureModifier> codec() {
            return CSStructureModifiers.ADD_SPAWNS_STRUCTURE_TAG_CODEC.get();
        }
    }
}
