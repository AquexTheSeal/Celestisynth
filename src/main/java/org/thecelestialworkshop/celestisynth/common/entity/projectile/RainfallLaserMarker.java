package org.thecelestialworkshop.celestisynth.common.entity.projectile;

import org.thecelestialworkshop.celestisynth.common.entity.base.EffectControllerEntity;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class RainfallLaserMarker extends EffectControllerEntity {
    private static final EntityDataAccessor<Vector3f> ORIGIN = SynchedEntityData.defineId(RainfallLaserMarker.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> IS_QUASAR = SynchedEntityData.defineId(RainfallLaserMarker.class, EntityDataSerializers.BOOLEAN);

    public RainfallLaserMarker(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ORIGIN, new Vector3f(0, 0, 0));
        this.entityData.define(IS_QUASAR, true);
    }

    @Override
    public void tick() {
        this.baseTick();
        if (tickCount >= 7) {
            this.discard();
        }
    }

    public Vec3 getOrigin() {
        Vector3f vec3f = this.entityData.get(ORIGIN);
        return new Vec3(vec3f);
    }

    public void setOrigin(Vec3 origin) {
        this.entityData.set(ORIGIN, origin.toVector3f());
    }

    public boolean isQuasar() {
        return this.entityData.get(IS_QUASAR);
    }

    public void setQuasar(boolean quasar) {
        this.entityData.set(IS_QUASAR, quasar);
    }

}
