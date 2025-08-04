package org.thecelestialworkshop.celestisynth.datagen.providers;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class CSSoundProvider extends SoundDefinitionsProvider {

    public CSSoundProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, Celestisynth.MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.add(CSSoundEvents.STEP, definition().with(this.simpleSound("step")));
    }

    private SoundDefinition.Sound simpleSound(String name) {
        return sound(new ResourceLocation(Celestisynth.MODID, name), SoundDefinition.SoundType.SOUND);
    }
}
