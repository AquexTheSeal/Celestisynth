package com.aqutheseal.celestisynth.entities.tempestboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class TempestBoss extends Monster implements IAnimatable {
    private static final EntityDataAccessor<Integer> BATTLE_PHASE = SynchedEntityData.defineId(TempestBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(TempestBoss.class, EntityDataSerializers.INT);

    public static int
            NONE = 0,
            PHASE_TRANSITION_DASH_1 = 1;

    public TempestBoss(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 500.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 12.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.FLYING_SPEED, 1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(TempestBoss.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public void cyclePhase() {
        int newPhase = getPhase().ordinal() + 1;
        if (newPhase < TempestPhases.values().length) {
            this.setPhase(TempestPhases.values()[newPhase]);
        } else {
            this.setPhase(TempestPhases.values()[TempestPhases.values().length - 1]);
        }
    }

    public void setPhase(TempestPhases phase) {
        entityData.set(BATTLE_PHASE, phase.ordinal());
    }

    public TempestPhases getPhase() {
        return TempestPhases.values()[entityData.get(BATTLE_PHASE)];
    }

    public void setAttackState(int state) {
        entityData.set(ATTACK_STATE, state);
    }

    public int getAttackState() {
        return entityData.get(ATTACK_STATE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(BATTLE_PHASE, 1);
        this.entityData.define(ATTACK_STATE, 0);
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

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (getPhase() == TempestPhases.APPROACH_SLOW) {
            loopAnim(event.getController(), "walk_slow");
        }
        loopAnim(event.getController(), "walk_slow");
        return PlayState.CONTINUE;
    }

    public <E extends IAnimatable> void loopAnim(AnimationController<E> controller, String name) {
        controller.setAnimation(new AnimationBuilder().addAnimation("animation.tempest." + name, ILoopType.EDefaultLoopTypes.LOOP));
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