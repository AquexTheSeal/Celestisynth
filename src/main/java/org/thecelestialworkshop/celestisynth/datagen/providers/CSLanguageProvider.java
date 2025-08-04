package org.thecelestialworkshop.celestisynth.datagen.providers;

import org.thecelestialworkshop.celestisynth.Celestisynth;
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
