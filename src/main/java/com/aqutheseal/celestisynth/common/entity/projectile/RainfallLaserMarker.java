package com.aqutheseal.celestisynth.common.entity.projectile;

import com.aqutheseal.celestisynth.common.entity.base.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public class RainfallLaserMarker extends EffectControllerEntity {
    private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(RainfallLaserMarker.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> IS_QUASAR = SynchedEntityData.defineId(RainfallLaserMarker.class, EntityDataSerializers.BOOLEAN);

    public RainfallLaserMarker(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ORIGIN, BlockPos.ZERO);
        this.entityData.define(IS_QUASAR, true);
    }

    @Override
    public void tick() {
        this.baseTick();
        if (tickCount >= 5) {
            this.discard();
        }
    }

    public BlockPos getOrigin() {
        return this.entityData.get(ORIGIN);
    }

    public void setOrigin(BlockPos origin) {
        this.entityData.set(ORIGIN, origin);
    }

    public boolean isQuasar() {
        return this.entityData.get(IS_QUASAR);
    }

    public void setQuasar(boolean quasar) {
        this.entityData.set(IS_QUASAR, quasar);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItems.RAINFALL_SERENITY.get();
    }
}
