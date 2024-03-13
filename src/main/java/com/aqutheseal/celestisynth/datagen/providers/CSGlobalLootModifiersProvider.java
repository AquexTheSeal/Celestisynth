package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.registry.CSLootTables;
import com.aqutheseal.celestisynth.datagen.helpers.MergeLootTablesModifier;
import net.minecraft.advancements.critereon.LocationPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.ArrayList;

public class CSGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public CSGlobalLootModifiersProvider(PackOutput output) {
        super(output, Celestisynth.MODID);
    }

    @Override
    protected void start() {
        add("underground_dungeons_modifier", new MergeLootTablesModifier(
                new LootItemCondition[]{
                        LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.OVERWORLD)).build(),
                        AnyOfCondition.anyOf(allValidDungeonTables()).build(),
                },
                CSLootTables.UNDERGROUND_DUNGEONS.location
        ));
        add("nether_structures_modifier", new MergeLootTablesModifier(
                new LootItemCondition[]{
                        LocationCheck.checkLocation(LocationPredicate.Builder.location().setDimension(Level.NETHER)).build(),
                        AnyOfCondition.anyOf(allValidNetherStructureTables()).build(),
                },
                CSLootTables.VANILLA_NETHER_STRUCTURES.location
        ));
    }

    public LootTableIdCondition.Builder[] allValidDungeonTables() {
        ArrayList<LootTableIdCondition.Builder> builderList = new ArrayList<>();
        builderList.add(lootId("chests/ancient_city"));
        builderList.add(lootId("chests/abandoned_mineshaft"));
        String modid = "betterdungeons";
        builderList.add(lootId(modid, "zombie_dungeon/chests/common"));
        builderList.add(lootId(modid, "skeleton_dungeon/chests/common"));
        builderList.add(lootId(modid, "spider_dungeon/chests/egg_room"));
        return builderList.toArray(new LootTableIdCondition.Builder[0]);
    }

    public LootTableIdCondition.Builder[] allValidNetherStructureTables() {
        ArrayList<LootTableIdCondition.Builder> builderList = new ArrayList<>();
        builderList.add(lootId("chests/nether_bridge"));
        builderList.add(lootId("chests/ruined_portal"));
        builderList.add(lootId("chests/bastion_bridge"));
        builderList.add(lootId("chests/bastion_hoglin_stable"));
        builderList.add(lootId("chests/bastion_other"));
        builderList.add(lootId("chests/bastion_treasure"));
        String ignoredModid = "";
        return builderList.toArray(new LootTableIdCondition.Builder[0]);
    }

    public LootTableIdCondition.Builder lootId(String location) {
        return LootTableIdCondition.builder(new ResourceLocation(location));
    }

    public LootTableIdCondition.Builder lootId(String modid, String location) {
        return LootTableIdCondition.builder(new ResourceLocation(modid, location));
    }
}
