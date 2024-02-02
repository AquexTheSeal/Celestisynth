package com.aqutheseal.celestisynth.common.entity.tempestboss;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TempestBoss extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Integer> BATTLE_PHASE = SynchedEntityData.defineId(TempestBoss.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ATTACK_STATE = SynchedEntityData.defineId(TempestBoss.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

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
                .add(Attributes.ARMOR, 15.0D)
                .add(Attributes.FLYING_SPEED, 1.0F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new AITempestPhaseChangeGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(TempestBoss.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (tickCount == 1) {
            cyclePhase();
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        cyclePhase();
        if (pSource.getDirectEntity() instanceof Player player) {
            if (level().isClientSide()) {
                player.displayClientMessage(Component.literal("New Phase: " + getPhase().ordinal() + " - " + getPhase().name()), false);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    public void cyclePhase() {
        int newPhase = getPhase().ordinal() + 1;
        if (newPhase < TempestPhases.values().length) {
            this.modifyPhaseOnCycle(newPhase);
            this.setPhase(TempestPhases.values()[newPhase]);
        } else {
            this.setPhase(TempestPhases.values()[TempestPhases.values().length - 1]);
        }
    }

    public void modifyPhaseOnCycle(int newPhase) {
        switch (TempestPhases.values()[newPhase]) {
            case PHASE_1 -> modifyAttribute(Attributes.MOVEMENT_SPEED, 0.45);
            case PHASE_2 -> modifyAttribute(Attributes.MOVEMENT_SPEED, 0.65);
        }
    }

    public void modifyAttribute(Attribute attribute, double value) {
        var mod = getAttribute(attribute);
        assert mod != null;
        mod.setBaseValue(value);
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

    public boolean isControlledByLocalInstance() {
        return true;
    }

    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, (state) -> {
            switch (getPhase()) {
                case PHASE_1 -> {
                    return state.setAndContinue(RawAnimation.begin().thenLoop("animation.tempest.run"));
                }
                default -> {
                    return state.setAndContinue(RawAnimation.begin().thenLoop("animation.tempest.walk_slow"));
                }
            }
        }));
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
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
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}