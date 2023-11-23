package com.aqutheseal.celestisynth.datagen.providers;

import com.aqutheseal.celestisynth.Celestisynth;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class CSLanguageProvider extends LanguageProvider {
    public CSLanguageProvider(DataGenerator gen) {
        super(gen, Celestisynth.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {

    }
}
