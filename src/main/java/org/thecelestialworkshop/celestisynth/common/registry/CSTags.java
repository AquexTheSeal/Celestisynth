package org.thecelestialworkshop.celestisynth.common.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.thecelestialworkshop.celestisynth.Celestisynth;

public class CSTags {
    public static void init() {
        Biomes.init();
        Blocks.init();
        Items.init();
        EntityTypes.init();
        DamageTypes.init();
        Structures.init();
    }

    public static class Items {
        private static void init() {}

        public static final TagKey<Item> CELESTIAL_CORE_BASES = tag("celestial_core_bases");
        public static final TagKey<Item> BLOOD_RUNE_ACTIVATOR = tag("blood_rune_activator");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

    public static class Blocks {
        private static void init() {}

        public static final TagKey<Block> NEEDS_CELESTIAL_TOOL = tag("needs_celestial_tool");

        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registries.BLOCK, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

    public static class EntityTypes {
        private static void init() {}

        public static final TagKey<EntityType<?>> FROSTBOUND_SENSITIVE = tag("frostbound_sensitive");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

    public static class Biomes {
        private static void init() {}

        public static final TagKey<Biome> HAS_WINTEREIS_CLUSTER = tag("has_structure/has_wintereis_cluster");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

    public static class Structures {
        private static void init() {}

        public static final TagKey<Structure> NETHER_MONOLITH_SPAWN = tag("nether_monolith_spawn");

        private static TagKey<Structure> tag(String name) {
            return TagKey.create(Registries.STRUCTURE, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

    public static class DamageTypes {
        private static void init() {}

        public static final TagKey<DamageType> IS_CELESTIAL_ATTACK = tag("is_celestial_attack");
        public static final TagKey<DamageType> PIERCES_THROUGH_ALL = tag("pierces_through_all");

        private static TagKey<DamageType> tag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

}
