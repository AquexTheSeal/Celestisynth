package com.aqutheseal.celestisynth.mixin;

import com.aqutheseal.celestisynth.LivingMixinSupport;
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
    public final String
            PHANTOM_TAG_BY = "cs.phantomTagByID",
            QUASAR_IMBUED_BY = "cs.quasarImbuedByID";
    public int tagTimer;
    public int quasarTimer;

    public LivingEntityMixin(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void tick(CallbackInfo ci) {
        if (tagTimer > 0) {
            tagTimer--;
        } else {
            setPhantomTagger(null);
        }
        if (quasarTimer > 0) {
            quasarTimer--;
        } else {
            setQuasarImbued(null);
        }
    }

    public @Nullable Player getPhantomTagger() {
        if (level.getEntity(getPhantomTagFrom()) instanceof Player) {
            return (Player) level.getEntity(getPhantomTagFrom());
        } else {
            return null;
        }
    }

    public void setPhantomTagger(@Nullable Player tagger) {
        if (tagger != null) {
            setPhantomTagFrom(tagger.getId());
        } else {
            setPhantomTagFrom(0);
        }
    }

    public void setPhantomTagFrom(int phantomTagFrom) {
        this.getPersistentData().putInt(PHANTOM_TAG_BY, phantomTagFrom);
        tagTimer = 100;
    }

    public int getPhantomTagFrom() {
        return this.getPersistentData().getInt(PHANTOM_TAG_BY);
    }

    public @Nullable Player getQuasarImbued() {
        if (level.getEntity(getQuasarImbuedFrom()) instanceof Player) {
            return (Player) level.getEntity(getQuasarImbuedFrom());
        } else {
            return null;
        }
    }

    public void setQuasarImbued(@Nullable Player imbuedTo) {
        if (imbuedTo != null) {
            setQuasarImbuedFrom(imbuedTo.getId());
        } else {
            setQuasarImbuedFrom(0);
        }
    }

    public void setQuasarImbuedFrom(int imbuedFrom) {
        this.getPersistentData().putInt(QUASAR_IMBUED_BY, imbuedFrom);
        quasarTimer = 100;
    }

    public int getQuasarImbuedFrom() {
        return this.getPersistentData().getInt(QUASAR_IMBUED_BY);
    }
}
