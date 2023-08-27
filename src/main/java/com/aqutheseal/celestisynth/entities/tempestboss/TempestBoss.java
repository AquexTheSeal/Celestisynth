package com.aqutheseal.celestisynth.entities.tempestboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TempestBoss extends Monster implements IAnimatable {
    private static final EntityDataAccessor<Integer> BATTLE_PHASE = SynchedEntityData.defineId(TempestBoss.class, EntityDataSerializers.INT);    public void setPhase(TempestPhases phase) {
        entityData.set(BATTLE_PHASE, phase.ordinal());
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(BATTLE_PHASE, 0);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    protected TempestBoss(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        return PlayState.CONTINUE;
    }

    public TempestPhases getPhase() {
        return TempestPhases.values()[entityData.get(BATTLE_PHASE)];
    }



    @Override
    public void readAdditionalSaveData(CompoundTag compoundNBT) {
        setPhase(TempestPhases.values()[compoundNBT.getInt("battlePhase_tempest")]);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundNBT) {
        compoundNBT.putInt("battlePhase_tempest", getPhase().ordinal());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
