package com.aqutheseal.celestisynth.common.entity.projectile;

import com.aqutheseal.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import com.aqutheseal.celestisynth.common.entity.skill.SkillCastRainfallRain;
import com.aqutheseal.celestisynth.common.item.weapons.RainfallSerenityItem;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSParticleTypes;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

public class RainfallArrow extends AbstractArrow {
    private static final EntityDataAccessor<Boolean> IS_STRONG = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FLAMING = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<BlockPos> ORIGIN = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.BLOCK_POS);
    private static final EntityDataAccessor<Boolean> SHOULD_IMBUE_QUASAR = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.BOOLEAN);

    private final RainfallSerenityItem rawRainfallItem = (RainfallSerenityItem) CSItems.RAINFALL_SERENITY.get();

    public RainfallArrow(EntityType<? extends RainfallArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RainfallArrow(Level pLevel, double pX, double pY, double pZ) {
        super(CSEntityTypes.RAINFALL_ARROW.get(), pX, pY, pZ, pLevel);
    }

    public RainfallArrow(Level pLevel, LivingEntity pShooter) {
        super(CSEntityTypes.RAINFALL_ARROW.get(), pShooter, pLevel);
    }

    public void createLaser(Vec3 from, Vec3 to, boolean isMainArrow, boolean strictLoading) {
        /**
        double distance = from.distanceTo(to);
        Vec3 direction = to.subtract(from).normalize();

        for (double i = 0; i <= distance; i += 0.1) {
            Vec3 particlePos = from.add(direction.scale(i));
            BlockPos particleBlockPos = new BlockPos((int) particlePos.x(), (int) particlePos.y(), (int) particlePos.z());

            if (strictLoading && !level().isLoaded(particleBlockPos)) return;

            SimpleParticleType curParticle = isMainArrow ? CSParticleTypes.RAINFALL_BEAM.get() : CSParticleTypes.RAINFALL_BEAM_QUASAR.get();

            ParticleUtil.sendParticles(level(), curParticle, particlePos.x, particlePos.y, particlePos.z, 1, 0, 0, 0);
        }
         **/
    }

    @Override
    public void tick() {
        super.tick();

        if (isStrong()) {
            if (tickCount == 2) {
                Vec3 from = new Vec3(getOrigin().getX(), getOrigin().getY(), getOrigin().getZ());
                Vec3 to = new Vec3(getX(), getY(), getZ());
                createLaser(from, to, true, true);
            }
        }

        if (tickCount > 2) {
            markForLaser(getX(), getY(), getZ());
            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        setDeltaMovement(getDeltaMovement().scale(30));
    }

    public void hitEffect(HitResult pResult, BlockPos hitPos) {
        if (getOwner() instanceof Player) {
            if (isStrong()) {
                if (pResult instanceof BlockHitResult || (pResult instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity)) {
                    for (Entity potentialTarget : rawRainfallItem.iterateEntities(level(), rawRainfallItem.createAABB(hitPos, 4))) {
                        if (potentialTarget instanceof LivingEntity target && potentialTarget != getOwner()) {
                            if (isFlaming()) target.setSecondsOnFire(2);

                            target.hurt(damageSources().indirectMagic(this, getOwner() != null ? getOwner() : null), 2);
                            target.invulnerableTime = 0;
                        }
                    }
                }

                if (pResult instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity target) {
                    setPierceLevel((byte) (getPierceLevel() + 1));

                    CSEntityCapabilityProvider.get(target).ifPresent(data -> {
                        if (getOwner() instanceof Player player && data.getQuasarImbueSource() == player) {
                            SkillCastRainfallRain projectile = CSEntityTypes.RAINFALL_RAIN.get().create(player.level());
                            projectile.targetPos = new BlockPos(ehr.getEntity().blockPosition());
                            projectile.setOwnerUuid(player.getUUID());
                            projectile.moveTo(ehr.getEntity().getX(), ehr.getEntity().getY() + 15, ehr.getEntity().getZ());
                            player.level().addFreshEntity(projectile);
                        }
                    });

                    if (random.nextInt(3) == 1) {
                        for (Entity imbueSource : rawRainfallItem.iterateEntities(level(), rawRainfallItem.createAABB(hitPos, 24))) {
                            if (imbueSource instanceof LivingEntity livingSource) {
                                CSEntityCapabilityProvider.get(livingSource).ifPresent(data -> {
                                    if (imbueSource != ehr.getEntity() && getOwner() instanceof Player player && data.getQuasarImbueSource() == player) {
                                        ehr.getEntity().invulnerableTime = 0;

                                        Vec3 from = new Vec3(imbueSource.getX(), imbueSource.getY() + 1.5, imbueSource.getZ());
                                        Vec3 to = new Vec3(ehr.getEntity().getX(), ehr.getEntity().getY() + 1.5F, ehr.getEntity().getZ());
                                        createLaser(from, to, false, false);

                                        if (!level().isClientSide()) {
                                            BlockPos imbuePos = new BlockPos((int) imbueSource.getX(), (int) (imbueSource.getY() + 1.5D), (int) imbueSource.getZ());
                                            RainfallArrow rainfallArrow = new RainfallArrow(level(), player);

                                            rainfallArrow.setOwner(player);
                                            rainfallArrow.moveTo(imbueSource.position());
                                            rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                            rainfallArrow.setOrigin(imbuePos);
                                            rainfallArrow.setPierceLevel((byte) 3);
                                            rainfallArrow.setBaseDamage(CSConfigManager.COMMON.rainfallSerenityQuasarArrowDmg.get());
                                            rainfallArrow.setImbueQuasar(false);

                                            double offsetHitResultY = ehr.getEntity().getY() + 1.5D;
                                            double finalDistX = ehr.getEntity().getX() - imbueSource.getX();
                                            double offsetDistY = offsetHitResultY - imbueSource.getY() + 1.5F;
                                            double finalDistZ = ehr.getEntity().getZ() - imbueSource.getZ();

                                            rainfallArrow.shoot(finalDistX, offsetDistY, finalDistZ, 3.0F, 0);
                                            level().addFreshEntity(rainfallArrow);
                                        }

                                        level().playSound(null, player.getX(), player.getY(), player.getZ(), CSSoundEvents.LASER_SHOOT.get(), SoundSource.PLAYERS, 0.7f, 2.0F);
                                        data.clearQuasarImbue();
                                    }
                                });
                            }
                        }
                    }


                    if (ehr.getEntity() instanceof LivingEntity lt) {
                        CSEntityCapabilityProvider.get(lt).ifPresent(data -> {
                            if (isImbueQuasar() && getOwner() instanceof LivingEntity living) {
                                data.setQuasarImbue(living, 200);
                            }
                        });
                    }
                }

                playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F + random.nextFloat());

                int amount = 60;
                float expansionMultiplier = 0.65F;

                for (int e = 0; e < amount; e++) {
                    double targetAngle = random.nextDouble() * 2 * Math.PI;
                    double offsetPi = random.nextDouble() * Math.PI;
                    double offX = (Math.sin(offsetPi) * Math.cos(targetAngle)) * expansionMultiplier;
                    double offY = (Math.sin(offsetPi) * Math.sin(targetAngle)) * expansionMultiplier;
                    double offZ = Math.cos(offsetPi) * expansionMultiplier;

                    ParticleUtil.sendParticles(level(), CSParticleTypes.RAINFALL_ENERGY.get(), hitPos.getX(), hitPos.getY(), hitPos.getZ(), 0, offX, offY, offZ);
                }
            }
        }
    }

    public void markForLaser(double x, double y, double z) {
        if (!level().isClientSide) {
            RainfallLaserMarker marker = CSEntityTypes.RAINFALL_LASER_MARKER.get().create(level());
            marker.moveTo(x, y, z);
            marker.setYRot(getYRot());
            marker.setXRot(getXRot());
            marker.yRotO = yRotO;
            marker.xRotO = xRotO;
            marker.setOrigin(getOrigin());
            level().addFreshEntity(marker);
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
        return ItemStack.EMPTY;
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
        this.entityData.define(SHOULD_IMBUE_QUASAR, false);
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

    public boolean isImbueQuasar() {
        return this.entityData.get(SHOULD_IMBUE_QUASAR);
    }

    public void setImbueQuasar(boolean imbueAllow) {
        this.entityData.set(SHOULD_IMBUE_QUASAR, imbueAllow);
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
