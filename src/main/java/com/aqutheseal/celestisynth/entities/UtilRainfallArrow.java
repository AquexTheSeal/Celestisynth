package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.item.helpers.CSUtilityFunctions;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.item.weapons.RainfallSerenityItem;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import com.aqutheseal.celestisynth.registry.CSParticleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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

import java.util.Random;

public class UtilRainfallArrow extends AbstractArrow implements IAnimatable {
    private static final EntityDataAccessor<Boolean> IS_STRONG = SynchedEntityData.defineId(UtilRainfallArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FLAMING = SynchedEntityData.defineId(UtilRainfallArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(UtilRainfallArrow.class, EntityDataSerializers.BLOCK_POS);
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private final RainfallSerenityItem rawRainfallItem = (RainfallSerenityItem) CSItemRegistry.RAINFALL_SERENITY.get();

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

        if (isStrong()) {
            if (tickCount == 2) {
                Vec3 from = new Vec3(getOrigin().getX(), getOrigin().getY(), getOrigin().getZ());
                Vec3 to = new Vec3(getX(), getY(), getZ());
                double distance = from.distanceTo(to);
                Vec3 direction = to.subtract(from).normalize();
                for (double i = 0; i <= distance; i += 0.1) {
                    Vec3 particlePos = from.add(direction.scale(i));

                    if (!shouldRenderAtSqrDistance(distance)) return;

                    CSUtilityFunctions.sendParticles(level, CSParticleRegistry.RAINFALL_BEAM.get(), particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0);
                }
            }
        }

        if (tickCount > 2) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        this.setDeltaMovement(this.getDeltaMovement().scale(12));
    }

    public void hitEffect(HitResult pResult, BlockPos hitPos) {
        if (getOwner() instanceof Player owner) {
            if (isStrong()) {
                if (pResult instanceof BlockHitResult || (pResult instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity)) {
                    for (Entity entity : rawRainfallItem.iterateEntities(level, rawRainfallItem.createAABB(hitPos, 4))) {
                        if (entity instanceof LivingEntity target && entity != this.getOwner()) {
                            if (isFlaming()) {
                                target.setSecondsOnFire(2);
                            }
                            target.hurt(DamageSource.indirectMagic(this, this.getOwner() != null ? this.getOwner() : null), 2);
                        }
                    }
                }
                if (pResult instanceof EntityHitResult ehr) {
                    setPierceLevel((byte) (getPierceLevel() + 1));
                    CSWeapon.disableRunningWeapon(ehr.getEntity());
                }
                this.playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F + random.nextFloat());
                int amount = 90;
                float expansionMultiplier = 0.35F;
                for (int e = 0; e < amount; e++) {
                    Random random = new Random();
                    double theta = random.nextDouble() * 2 * Math.PI;
                    double phi = random.nextDouble() * Math.PI;
                    float offX = (float) (Math.sin(phi) * Math.cos(theta)) * expansionMultiplier;
                    float offY = (float) (Math.sin(phi) * Math.sin(theta)) * expansionMultiplier;
                    float offZ = (float) Math.cos(phi) * expansionMultiplier;
                    CSUtilityFunctions.sendParticles(level, CSParticleRegistry.RAINFALL_ENERGY.get(), hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0, offX, offY, offZ);
                    CSUtilityFunctions.sendParticles(level, ParticleTypes.CAMPFIRE_COSY_SMOKE, hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0, offX / 3, offY / 6, offZ / 3);
                }
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        hitEffect(pResult, pResult.getBlockPos());
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        hitEffect(pResult, pResult.getEntity().blockPosition());
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
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_STRONG, false);
        this.entityData.define(IS_FLAMING, false);
        this.entityData.define(ORIGIN, BlockPos.ZERO);
    }

    public boolean isStrong() {
        return this.entityData.get(IS_STRONG);
    }

    public void setStrong(boolean isStrong) {
        this.entityData.set(IS_STRONG, isStrong);
    }

    public boolean isFlaming() {
        return this.entityData.get(IS_FLAMING);
    }

    public void setFlaming(boolean isFlaming) {
        this.entityData.set(IS_FLAMING, isFlaming);
    }

    public BlockPos getOrigin() {
        return this.entityData.get(ORIGIN);
    }

    public void setOrigin(BlockPos origin) {
        this.entityData.set(ORIGIN, origin);
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
        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.rainfall_arrow.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
