package org.thecelestialworkshop.celestisynth.common.entity.projectile;

import org.thecelestialworkshop.celestisynth.api.item.CSWeaponUtil;
import org.thecelestialworkshop.celestisynth.common.capabilities.CSEntityCapabilityProvider;
import org.thecelestialworkshop.celestisynth.common.entity.mob.misc.RainfallTurret;
import org.thecelestialworkshop.celestisynth.common.entity.skillcast.SkillCastRainfallRain;
import org.thecelestialworkshop.celestisynth.common.item.weapons.RainfallSerenityItem;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSItems;
import org.thecelestialworkshop.celestisynth.common.registry.CSParticleTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import org.joml.Vector3f;

public class RainfallArrow extends AbstractArrow implements CSWeaponUtil {
    private static final EntityDataAccessor<Boolean> IS_STRONG = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_FLAMING = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Vector3f> ORIGIN = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> SHOULD_IMBUE_QUASAR = SynchedEntityData.defineId(RainfallArrow.class, EntityDataSerializers.BOOLEAN);

    private final RainfallSerenityItem rawRainfallItem = (RainfallSerenityItem) CSItems.RAINFALL_SERENITY.get();
    public boolean isMultishot = false;
    public RainfallTurret turretSource = null;

    public RainfallArrow(EntityType<? extends RainfallArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    public RainfallArrow(Level pLevel, double pX, double pY, double pZ) {
        super(CSEntityTypes.RAINFALL_ARROW.get(), pX, pY, pZ, pLevel, new ItemStack(net.minecraft.world.item.Items.ARROW), null);
    }

    public RainfallArrow(Level pLevel, LivingEntity pShooter) {
        super(CSEntityTypes.RAINFALL_ARROW.get(), pShooter, pLevel, new ItemStack(net.minecraft.world.item.Items.ARROW), null);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(net.minecraft.world.item.Items.ARROW);
    }

    public RainfallArrow(AbstractArrow arrow) {
        this(arrow.level(), (LivingEntity) arrow.getOwner());
    }

    @Override
    public void tick() {
        super.tick();

//        if (isStrong()) {
//            if (tickCount == 2) {
//                Vec3 from = new Vec3(getOrigin().getX(), getOrigin().getY(), getOrigin().getZ());
//                Vec3 to = new Vec3(getX(), getY(), getZ());
//                createLaser(from, to, true, true);
//            }
//        }

        if (tickCount > 5) {
            //markForLaser();
            remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void shoot(double pX, double pY, double pZ, float pVelocity, float pInaccuracy) {
        super.shoot(pX, pY, pZ, pVelocity, pInaccuracy);
        setDeltaMovement(getDeltaMovement().scale(30));
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void hitEffect(HitResult pResult, BlockPos hitPos) {
        if (isStrong()) {
            if (pResult instanceof BlockHitResult || (pResult instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity)) {
                for (Entity potentialTarget : rawRainfallItem.iterateEntities(level(), rawRainfallItem.createAABB(hitPos, 4))) {
                    if (potentialTarget instanceof LivingEntity target && potentialTarget != getOwner()) {
                        if (isFlaming()) target.igniteForSeconds(2);
                        target.hurt(damageSources().indirectMagic(this, getOwner() != null ? getOwner() : null), 2);
                        target.invulnerableTime = 0;
                    }
                }
            }

            if (pResult instanceof EntityHitResult ehr && ehr.getEntity() instanceof LivingEntity target) {
                setPierceLevel((byte) (getPierceLevel() + 1));

                if (turretSource != null) {
                    target.setLastHurtByMob(turretSource);
                }

                CSEntityCapabilityProvider.get(target).ifPresent(data -> {
                    if (getOwner() instanceof LivingEntity player && data.getQuasarImbueSource() == player) {
                        SkillCastRainfallRain projectile = CSEntityTypes.RAINFALL_RAIN.get().create(player.level());
                        projectile.targetPos = new BlockPos(ehr.getEntity().blockPosition());
                        projectile.setOwnerUUID(player.getUUID());
                        projectile.moveTo(ehr.getEntity().getX(), ehr.getEntity().getY() + 15, ehr.getEntity().getZ());
                        projectile.baseDamage = this.getBaseDamage() * 0.25;
                        projectile.isMultishot = this.isMultishot;
                        player.level().addFreshEntity(projectile);
                    }
                });

                if (random.nextInt(3) == 1) {
                    for (Entity imbueSource : rawRainfallItem.iterateEntities(level(), rawRainfallItem.createAABB(hitPos, 24))) {
                        if (imbueSource instanceof LivingEntity livingSource) {
                            CSEntityCapabilityProvider.get(livingSource).ifPresent(data -> {
                                if (imbueSource != ehr.getEntity() && getOwner() instanceof Player player && data.getQuasarImbueSource() == player) {
                                    ehr.getEntity().invulnerableTime = 0;

                                    if (!level().isClientSide()) {
                                        RainfallArrow rainfallArrow = new RainfallArrow(level(), player);
                                        rainfallArrow.setOwner(player);
                                        rainfallArrow.moveTo(imbueSource.position());
                                        rainfallArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
                                        rainfallArrow.setOrigin(imbueSource.getEyePosition());
                                        rainfallArrow.setPierceLevel((byte) 3);
                                        rainfallArrow.setBaseDamage(this.getBaseDamage() * 0.35);
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
        }

        playSound(SoundEvents.ENDER_EYE_DEATH, 1.0F, 1.0F + random.nextFloat());

        int amount = 60;
        for (int e = 0; e < amount; e++) {
            ParticleUtil.sendParticle(level(), CSParticleTypes.RAINFALL_ENERGY_SMALL.get(), Vec3.atCenterOf(hitPos), Vec3.ZERO.add(random.nextGaussian() * 0.2, random.nextGaussian() * 0.2, random.nextGaussian() * 0.2));
        }
    }

    @Override
    public void remove(RemovalReason pReason) {
        //this.markForLaser();
        super.remove(pReason);
    }

    public void markForLaser() {
//        if (!level().isClientSide) {
//            RainfallLaserMarker marker = CSEntityTypes.RAINFALL_LASER_MARKER.get().create(level());
//            marker.moveTo(this.position().add(0, -3, 0));
//            marker.setYRot(getYRot());
//            marker.setXRot(getXRot());
//            marker.yRotO = yRotO;
//            marker.xRotO = xRotO;
//            marker.setOrigin(getOrigin());
//            marker.setQuasar(this.isImbueQuasar());
//            level().addFreshEntity(marker);
//        }
    }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (level().getBlockState(pResult.getBlockPos()).isCollisionShapeFullBlock(level(), pResult.getBlockPos())) {
            hitEffect(pResult, pResult.getBlockPos());
        }
        this.remove(RemovalReason.DISCARDED);
    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (pResult.getEntity() instanceof RainfallTurret turret) {
            if (turret.getOwner() != this.getOwner()) {
                hitEffect(pResult, pResult.getEntity().blockPosition());
            }
        } else {
            hitEffect(pResult, pResult.getEntity().blockPosition());
        }
        pResult.getEntity().invulnerableTime = 0;
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        return false;
    }

    @Override
    public boolean isInvulnerable() {
        return true;
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
    protected void defineSynchedData(net.minecraft.network.syncher.SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(IS_STRONG, false);
        builder.define(IS_FLAMING, false);
        builder.define(ORIGIN, new Vector3f(0, 0, 0));
        builder.define(SHOULD_IMBUE_QUASAR, true);
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

    public Vec3 getOrigin() {
        return new Vec3(this.entityData.get(ORIGIN));
    }

    public void setOrigin(Vec3 origin) {
        this.entityData.set(ORIGIN, origin.toVector3f());
    }

    public boolean isImbueQuasar() {
        return this.entityData.get(SHOULD_IMBUE_QUASAR);
    }

    public void setImbueQuasar(boolean imbueAllow) {
        this.entityData.set(SHOULD_IMBUE_QUASAR, imbueAllow);
    }

    }
