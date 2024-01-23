package com.aqutheseal.celestisynth.common.capabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface CSCapabilityHelper {
    default @Nullable LivingEntity getLivingFromWorld(Level level, int id) {
        Entity entity = level.getEntity(id);
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        } else {
            return null;
        }
    }
}
