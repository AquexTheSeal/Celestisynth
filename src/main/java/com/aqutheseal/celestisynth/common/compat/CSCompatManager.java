package com.aqutheseal.celestisynth.common.compat;

import net.minecraftforge.fml.ModList;

public class CSCompatManager {

    public static boolean checkBetterCombat() {
        return ModList.get().isLoaded("bettercombat");
    }

    public static boolean checkIronsSpellbooks() {
        return ModList.get().isLoaded("irons_spellbooks");
    }
}
