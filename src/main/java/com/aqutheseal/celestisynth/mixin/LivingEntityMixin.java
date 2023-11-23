package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.api.mixin.LivingMixinSupport;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements LivingMixinSupport {
    private static final String PHANTOM_TAG_BY = "cs.phantomTagByID",
            QUASAR_IMBUED_BY = "cs.quasarImbuedByID";
    private int tagTimer;
    private int quasarTimer;

    private LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void celestisynth$tick(CallbackInfo ci) {
        if (tagTimer > 0) tagTimer--;
        else setPhantomTagger(null);

        if (quasarTimer > 0) quasarTimer--;
        else setQuasarImbued(null);
    }

    @Nullable
    @Override
    public Player getPhantomTagger() {
        return level.getEntity(getPhantomTagFrom()) instanceof Player ? (Player) level.getEntity(getPhantomTagFrom()) : null;
    }

    @Override
    public void setPhantomTagger(@Nullable Player tagger) {
        if (tagger != null) setPhantomTagFrom(tagger.getId());
        else setPhantomTagFrom(0);
    }

    private int getPhantomTagFrom() {
        return getPersistentData().getInt(PHANTOM_TAG_BY);
    }

    private void setPhantomTagFrom(int phantomTagFrom) {
        getPersistentData().putInt(PHANTOM_TAG_BY, phantomTagFrom);

        this.tagTimer = 100;
    }

    @Override
    public @Nullable Player getQuasarImbued() {
        return level.getEntity(getQuasarImbuedFrom()) instanceof Player ? (Player) level.getEntity(getQuasarImbuedFrom()) : null;
    }

    @Override
    public void setQuasarImbued(@Nullable Player imbuedTo) {
        if (imbuedTo != null) setQuasarImbuedFrom(imbuedTo.getId());
        else setQuasarImbuedFrom(0);
    }

    private int getQuasarImbuedFrom() {
        return this.getPersistentData().getInt(QUASAR_IMBUED_BY);
    }

    private void setQuasarImbuedFrom(int imbuedFrom) {
        getPersistentData().putInt(QUASAR_IMBUED_BY, imbuedFrom);

        this.quasarTimer = 100;
    }
}
