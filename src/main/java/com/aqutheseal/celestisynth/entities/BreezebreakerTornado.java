package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class BreezebreakerTornado extends Entity {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(BreezebreakerTornado.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Float> ANGLE_X = SynchedEntityData.defineId(BreezebreakerTornado.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_Y = SynchedEntityData.defineId(BreezebreakerTornado.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_Z = SynchedEntityData.defineId(BreezebreakerTornado.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_X = SynchedEntityData.defineId(BreezebreakerTornado.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_Y = SynchedEntityData.defineId(BreezebreakerTornado.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> ANGLE_ADD_Z = SynchedEntityData.defineId(BreezebreakerTornado.class, EntityDataSerializers.FLOAT);

    public BreezebreakerTornado(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public void tick() {
        super.tick();
        UUID uuid = this.getOwnerUuid();
        Player player = uuid == null ? null : this.getLevel().getPlayerByUUID(uuid);

        if (player == null || player.isDeadOrDying()) {
            this.remove(RemovalReason.DISCARDED);
        }

        setAngleX(getAngleX() + getAddAngleX());
        setAngleY(getAngleY() + getAddAngleY());
        setAngleZ(getAngleZ() + getAddAngleZ());

        double newX = getX() + getAngleX();
        double newY = getY() + getAngleY();
        double newZ = getZ() + getAngleZ();
        BlockPos newPos = new BlockPos((int) newX, (int) newY, (int) newZ);

        double range = 6.0;
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(newX + range, newY + (range * 2), newZ + range, newX - range, newY - range, newZ - range));
        for (Entity entityBatch : entities) {
            if (entityBatch instanceof LivingEntity target) {
                if (target != player && target.isAlive()) {
                    target.invulnerableTime = 0;
                    target.hurtMarked = true;
                    target.hurt(DamageSource.playerAttack(player), CSConfig.COMMON.breezebreakerShiftSkillDmg.get());
                    target.setDeltaMovement(target.getDeltaMovement().add(0, 0.05 - (target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue() * 0.001), 0));
                }
            }
            if (entityBatch instanceof Projectile projectile) {
                projectile.remove(RemovalReason.DISCARDED);
            }
        }

        for (int yLevel = -1; yLevel < 6; yLevel++) {
            if (yLevel == -1 || yLevel == 0 || yLevel == 1) {
                CSEffect.createInstance(player, this, CSEffectTypes.SOLARIS_AIR_FLAT, getAngleX(), getAngleY() + yLevel, getAngleZ());
            }
            if (yLevel == 2 || yLevel == 3) {
                CSEffect.createInstance(player, this, CSEffectTypes.SOLARIS_AIR_MEDIUM_FLAT, getAngleX(), getAngleY() + yLevel, getAngleZ());
            }
            if (yLevel == 4 || yLevel == 5) {
                CSEffect.createInstance(player, this, CSEffectTypes.SOLARIS_AIR_LARGE_FLAT, getAngleX(), getAngleY() + yLevel, getAngleZ());
            }
        }

        if (tickCount % 20 == 0) {
            level.playSound(level.getPlayerByUUID(getOwnerUuid()), getAngleX(), getAngleY(), getAngleZ(), CSSoundRegistry.CS_WHIRLWIND.get(), SoundSource.HOSTILE, 0.10F, 0.5F + new Random().nextFloat());
        }

        int radius = 2;
        for (int sx = -radius; sx <= radius; sx++) {
            for (int sy = -radius; sy <= radius; sy++) {
                for (int sz = -radius; sz <= radius; sz++) {
                    if (getLevel().getBlockState(newPos.offset(sx, sy, sz)).is(BlockTags.REPLACEABLE_PLANTS)) {
                        getLevel().destroyBlock(newPos.offset(sx, sy, sz), false, player);
                    }
                }
            }
        }

        if (tickCount == 100 || !getLevel().getBlockState(newPos).isAir()) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(OWNER_UUID, Optional.empty());
        this.entityData.define(ANGLE_X, 0F);
        this.entityData.define(ANGLE_Y, 0F);
        this.entityData.define(ANGLE_Z, 0F);
        this.entityData.define(ANGLE_ADD_X, 0F);
        this.entityData.define(ANGLE_ADD_Y, 0F);
        this.entityData.define(ANGLE_ADD_Z, 0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        UUID uuid;
        if (tag.hasUUID("Owner")) {
            uuid = tag.getUUID("Owner");
        } else {
            String s = tag.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(this.getServer(), s);
        }
        if (uuid != null) {
            try {
                this.setOwnerUuid(uuid);
            } catch (Throwable throwable) {
                throw new IllegalStateException("...Crescentia-Ranged got no goddamn owner.");
            }
        }
        setAngleX(tag.getFloat("cs.angleX"));
        setAngleY(tag.getFloat("cs.angleY"));
        setAngleZ(tag.getFloat("cs.angleZ"));
        setAddAngleX(tag.getFloat("cs.angleAddX"));
        setAddAngleY(tag.getFloat("cs.angleAddY"));
        setAddAngleZ(tag.getFloat("cs.angleAddZ"));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (this.getOwnerUuid() != null) {
            tag.putUUID("Owner", getOwnerUuid());
        }
        tag.putFloat("cs.angleX", getAngleX());
        tag.putFloat("cs.angleY", getAngleY());
        tag.putFloat("cs.angleZ", getAngleZ());
        tag.putFloat("cs.angleAddX", getAddAngleX());
        tag.putFloat("cs.angleAddY", getAddAngleY());
        tag.putFloat("cs.angleAddZ", getAddAngleZ());
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public void setOwnerUuid(@Nullable UUID ownerUuid) {
        this.entityData.set(OWNER_UUID, Optional.ofNullable(ownerUuid));
    }

    @Nullable
    public UUID getOwnerUuid() {
        return this.entityData.get(OWNER_UUID).orElse(null);
    }

    public void setAngleX(float angleX) {
        this.entityData.set(ANGLE_X, angleX);
    }

    public float getAngleX() {
        return this.entityData.get(ANGLE_X);
    }

    public void setAngleY(float angleY) {
        this.entityData.set(ANGLE_Y, angleY);
    }

    public float getAngleY() {
        return this.entityData.get(ANGLE_Y);
    }

    public void setAngleZ(float angleZ) {
        this.entityData.set(ANGLE_Z, angleZ);
    }

    public float getAngleZ() {
        return this.entityData.get(ANGLE_Z);
    }

    public void setAddAngleX(float angleX) {
        this.entityData.set(ANGLE_ADD_X, angleX);
    }

    public float getAddAngleX() {
        return this.entityData.get(ANGLE_ADD_X);
    }

    public void setAddAngleY(float angleY) {
        this.entityData.set(ANGLE_ADD_Y, angleY);
    }

    public float getAddAngleY() {
        return this.entityData.get(ANGLE_ADD_Y);
    }

    public void setAddAngleZ(float angleZ) {
        this.entityData.set(ANGLE_ADD_Z, angleZ);
    }

    public float getAddAngleZ() {
        return this.entityData.get(ANGLE_ADD_Z);
    }
}
