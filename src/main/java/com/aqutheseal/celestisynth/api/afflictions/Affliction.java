package com.aqutheseal.celestisynth.api.afflictions;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@Deprecated
public abstract class Affliction {
    public final LivingEntity entity;
    public final LivingEntity source;
    public final Level level;
    public int stackCount;
    public int tickCount;

    public Affliction(LivingEntity entity, LivingEntity inflictor) {
        this.entity = entity;
        this.source = inflictor;
        this.level = entity.level();
    }

    public abstract void tick();

    public abstract int getSpan();

    public abstract Type getAfflictionType();

    public int maxStackCount() {
        return 1;
    }

    private void baseTick() {
        tickCount++;
        if (tickCount > getSpan()) {
        }
        this.tick();
    }

    public void setStackCount(int value) {
        stackCount = Mth.clamp(value, 0, this.maxStackCount());
    }

    public enum Type {
        BASIC,
        STACKABLE
    }
}
