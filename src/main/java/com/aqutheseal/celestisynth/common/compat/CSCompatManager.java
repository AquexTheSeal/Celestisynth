package com.aqutheseal.celestisynth.common.compat;

import net.minecraftforge.fml.ModList;

public class CSCompatManager {

    public static boolean checkIronsSpellbooks() {
        return ModList.get().isLoaded("irons_spellbooks");
    }
}
