package com.aqutheseal.celestisynth.api.mixin;

import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

public interface LivingMixinSupport {
    @Nullable Player getPhantomTagger();
    void setPhantomTagger(@Nullable Player tagger);

    @Nullable Player getQuasarImbued();
    void setQuasarImbued(@Nullable Player imbuedSource);
}
