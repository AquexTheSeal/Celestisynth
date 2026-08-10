package org.thecelestialworkshop.celestisynth.datagen.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.BuiltinStructures;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import org.thecelestialworkshop.celestisynth.common.registry.CSDamageTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import org.thecelestialworkshop.celestisynth.common.registry.CSTags;
import org.thecelestialworkshop.celestisynth.manager.CSIntegrationManager;

import java.util.concurrent.CompletableFuture;

public class CSTagsProvider {
    public static class BlockHandler extends BlockTagsProvider {

        public BlockHandler(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, Celestisynth.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(BlockTags.MINEABLE_WITH_PICKAXE)
                    .add(CSBlocks.SOLAR_CRYSTAL.get())
                    .add(CSBlocks.LUNAR_STONE.get())
                    .add(CSBlocks.ZEPHYR_DEPOSIT.get())
                    .add(CSBlocks.WINTEREIS.get())
                    .add(CSBlocks.CELESTIAL_CRAFTING_TABLE.get())
                    .add(CSBlocks.STARLIT_FACTORY.get())
            ;
            tag(BlockTags.NEEDS_DIAMOND_TOOL)
                    .add(CSBlocks.SOLAR_CRYSTAL.get())
                    .add(CSBlocks.LUNAR_STONE.get())
                    .add(CSBlocks.WINTEREIS.get())
                    .add(CSBlocks.CELESTIAL_CRAFTING_TABLE.get())
                    .add(CSBlocks.STARLIT_FACTORY.get())
            ;
            tag(Tags.Blocks.NEEDS_NETHERITE_TOOL)
                    .add(CSBlocks.ZEPHYR_DEPOSIT.get())
            ;
        }
    }

    public static class ItemHandler extends ItemTagsProvider {

        public ItemHandler(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Block>> pBlockTags, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, pBlockTags, Celestisynth.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(CSTags.Items.CELESTIAL_CORE_BASES)
                    .add(Items.HEART_OF_THE_SEA)
                    .add(Items.NETHER_STAR)
                    .add(Items.END_CRYSTAL)
            ;

            tag(CSTags.Items.BLOOD_RUNE_ACTIVATOR)
                    .add(CSItems.CRISMSON_PIECE.get())
                    .add(Items.NETHER_WART)
            ;
        }
    }

        public static class EntityTypeHandler extends EntityTypeTagsProvider {

        public EntityTypeHandler(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, Celestisynth.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(CSTags.EntityTypes.FROSTBOUND_SENSITIVE)
                    .add(EntityType.BLAZE, EntityType.GHAST, EntityType.MAGMA_CUBE)
                    .addOptional(ResourceLocation.fromNamespaceAndPath("cataclysm", "ignited_revenant"))
                    .addOptional(ResourceLocation.fromNamespaceAndPath("cataclysm", "ignis"))
                    .addOptional(ResourceLocation.fromNamespaceAndPath("iceandfire", "fire_dragon"))
            ;
        }
    }

    public static class BiomeHandler extends BiomeTagsProvider {

        public BiomeHandler(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, Celestisynth.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(CSTags.Biomes.HAS_WINTEREIS_CLUSTER)
                    .addTag(BiomeTags.IS_END)
            ;
        }
    }

    public static class StructureHandler extends StructureTagsProvider {

        public StructureHandler(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pProvider, Celestisynth.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(CSTags.Structures.NETHER_MONOLITH_SPAWN)
                    .add(BuiltinStructures.FORTRESS)
                    .addOptionalTag(ResourceLocation.fromNamespaceAndPath(CSIntegrationManager.YUNGS_BETTER_FORTRESSES_MODID, "better_fortresses"))
            ;
        }
    }

    public static class DamageTypeHandler extends DamageTypeTagsProvider {

        public DamageTypeHandler(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
            super(pOutput, pLookupProvider, Celestisynth.MODID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider pProvider) {
            tag(CSTags.DamageTypes.IS_CELESTIAL_ATTACK)
                    .add(CSDamageTypes.BASIC_PLAYER_ATTACK)
                    .add(CSDamageTypes.RAPID_PLAYER_ATTACK)
                    .add(CSDamageTypes.BASIC_PLAYER_ATTACK_NOKB)
                    .add(CSDamageTypes.RAPID_PLAYER_ATTACK_NOKB)
            ;

            tag(CSTags.DamageTypes.PIERCES_THROUGH_ALL)
                    .add(CSDamageTypes.ERASURE)
            ;

            tag(DamageTypeTags.BYPASSES_COOLDOWN)
                    .add(CSDamageTypes.RAPID_PLAYER_ATTACK)
                    .add(CSDamageTypes.PULSATION)
            ;

            tag(DamageTypeTags.BYPASSES_ARMOR)
                    .add(CSDamageTypes.ERASURE)
                    .addTag(CSTags.DamageTypes.PIERCES_THROUGH_ALL)
            ;

            tag(DamageTypeTags.BYPASSES_SHIELD)
                    .add(CSDamageTypes.ERASURE)
                    .addTag(CSTags.DamageTypes.PIERCES_THROUGH_ALL)
            ;

            tag(DamageTypeTags.BYPASSES_INVULNERABILITY)
                    .add(CSDamageTypes.ERASURE)
                    .addTag(CSTags.DamageTypes.PIERCES_THROUGH_ALL)
            ;
        }
    }
}
