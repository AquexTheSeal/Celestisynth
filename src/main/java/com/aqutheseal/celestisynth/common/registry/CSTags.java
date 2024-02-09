package com.aqutheseal.celestisynth.common.registry;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class CSTags {
    public static void init() {
        Biomes.init();
        Items.init();
    }

    public static class Items {
        private static void init() {}

        public static final TagKey<Item> CELESTIAL_CORE_BASES = tag("celestial_core_bases");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registries.ITEM, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

    public static class Biomes {
        private static void init() {}

        public static final TagKey<Biome> HAS_WINTEREIS_CLUSTER = tag("has_structure/has_wintereis_cluster");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registries.BIOME, new ResourceLocation(Celestisynth.MODID, name));
        }
    }
    public static class DamageTypes {
        private static void init() {}

        public static final TagKey<DamageType> IS_CELESTIAL_ATTACK = tag("is_celestial_attack");

        private static TagKey<DamageType> tag(String name) {
            return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(Celestisynth.MODID, name));
        }
    }

}
