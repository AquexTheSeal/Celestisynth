package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class CSLanguageProvider extends LanguageProvider {
    public CSLanguageProvider(PackOutput output) {
        super(output, Celestisynth.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

    }
}
