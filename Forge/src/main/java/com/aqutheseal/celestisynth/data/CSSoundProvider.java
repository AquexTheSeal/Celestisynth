package com.aqutheseal.celestisynth.data;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.common.sound.CSSounds;
import com.aqutheseal.celestisynth.reg.RegistryObject;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class CSSoundProvider extends SoundDefinitionsProvider {

    public CSSoundProvider(PackOutput output, String modId, ExistingFileHelper helper) {
        super(output, modId, helper);
    }

    @Override
    public void registerSounds() {
        for (RegistryObject<SoundEvent> sounds : CSSounds.SOUND_EVENTS.getEntries()) {
            this.add(sounds.get(), definition()
                    .subtitle("sound." + Celestisynth.MODID + "." + sounds.getId().getPath())
                    .with(sound(sounds.getId().getPath()).stream())
            );
        }
    }
}
