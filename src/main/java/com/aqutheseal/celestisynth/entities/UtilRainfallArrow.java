package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.network.NetworkHooks;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class UtilRainfallArrow extends AbstractArrow implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public UtilRainfallArrow(EntityType<? extends UtilRainfallArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public UtilRainfallArrow(Level pLevel, double pX, double pY, double pZ) {
        super(CSEntityRegistry.RAINFALL_ARROW.get(), pX, pY, pZ, pLevel);
    }

    public UtilRainfallArrow(Level pLevel, LivingEntity pShooter) {
        super(CSEntityRegistry.RAINFALL_ARROW.get(), pShooter, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        double xt = -0.15 + random.nextDouble() * 0.3;
        double yt = -0.15 + random.nextDouble() * 0.3;
        double zt = -0.15 + random.nextDouble() * 0.3;
        CSUtilityFunctions.sendParticles(level, ParticleTypes.SOUL_FIRE_FLAME, getX(), getY(), getZ(), 3, xt, yt, zt);
    }

    @Override
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected ItemStack getPickupItem() {
        return null;
    }

    @Override
    protected float getWaterInertia() {
        return 1.0F;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        //event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rainfall_arrow.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.STOP;
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
