package com.aqutheseal.celestisynth.common.compat.spellbooks;

import com.aqutheseal.celestisynth.Celestisynth;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.item.spell_books.SimpleAttributeSpellBook;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ISSCompatItemRegistry {
    public static final DeferredRegister<Item> SPELLBOOKS_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Celestisynth.MODID);

    public static final RegistryObject<Item> CELESTIAL_SPELLBOOK = SPELLBOOKS_ITEMS.register("celestial_spell_book", () ->
            new SimpleAttributeSpellBook(10, SpellRarity.LEGENDARY, ISSItemUtil.createCelestialSpellbookAttributes())
    );
}
