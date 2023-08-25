package com.aqutheseal.celestisynth.registry.datagen;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public class CSSoundProvider extends SoundDefinitionsProvider {

    public CSSoundProvider(DataGenerator generator, String modId, ExistingFileHelper helper) {
        super(generator, modId, helper);
    }

    @Override
    public void registerSounds() {
        for (RegistryObject<SoundEvent> sounds : CSSoundRegistry.SOUND_EVENTS.getEntries()) {
            this.add(sounds.get(), definition()
                    .subtitle("sound." + Celestisynth.MODID + "." + sounds.getId().getPath())
                    .with(sound(sounds.getId().getPath()).stream())
            );
        }
    }
}
