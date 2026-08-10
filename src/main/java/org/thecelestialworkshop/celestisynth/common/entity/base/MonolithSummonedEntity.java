package org.thecelestialworkshop.celestisynth.common.entity.base;

import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.StarMonolith;

import org.jetbrains.annotations.Nullable;

public interface MonolithSummonedEntity {

    void setMonolith(@Nullable StarMonolith monolith);

    @Nullable
    StarMonolith getMonolith();
}
