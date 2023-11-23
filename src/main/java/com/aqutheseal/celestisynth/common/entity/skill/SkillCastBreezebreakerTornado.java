package com.aqutheseal.celestisynth.common.entity.skill;

import com.aqutheseal.celestisynth.common.entity.base.CSEffect;
import com.aqutheseal.celestisynth.common.entity.base.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.entity.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.UUID;

public class SkillCastBreezebreakerTornado extends EffectControllerEntity {

    public SkillCastBreezebreakerTornado(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItems.BREEZEBREAKER.get();
    }

    @Override
    public void tick() {
        super.tick();

        UUID ownerUuid = getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : this.getLevel().getPlayerByUUID(ownerUuid);

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
                if (target != ownerPlayer && target.isAlive()) {
                    fromInterfaceWeapon().hurtNoKB(ownerPlayer, target, (float) (double) CSConfigManager.COMMON.breezebreakerShiftSkillDmg.get());
                    target.setDeltaMovement(target.getDeltaMovement().add(0, 0.05 - (target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue() * 0.001), 0));
                }
            }
            if (entityBatch instanceof Projectile projectile) projectile.remove(RemovalReason.DISCARDED);
        }

        for (int yLevel = -1; yLevel < 6; yLevel++) {
            if (yLevel == -1 || yLevel == 0 || yLevel == 1) CSEffect.createInstance(ownerPlayer, this, CSEffectTypes.SOLARIS_AIR_FLAT, getAngleX(), getAngleY() + yLevel, getAngleZ());
            if (yLevel == 2 || yLevel == 3) CSEffect.createInstance(ownerPlayer, this, CSEffectTypes.SOLARIS_AIR_MEDIUM_FLAT, getAngleX(), getAngleY() + yLevel, getAngleZ());
            if (yLevel == 4 || yLevel == 5) CSEffect.createInstance(ownerPlayer, this, CSEffectTypes.SOLARIS_AIR_LARGE_FLAT, getAngleX(), getAngleY() + yLevel, getAngleZ());
        }

        if (tickCount % 20 == 0) level.playSound(level.getPlayerByUUID(getOwnerUuid()), getAngleX(), getAngleY(), getAngleZ(), CSSoundEvents.CS_WHIRLWIND.get(), SoundSource.HOSTILE, 0.10F, 0.5F + random.nextFloat());

        int radius = 2;
        for (int sx = -radius; sx <= radius; sx++) {
            for (int sy = -radius; sy <= radius; sy++) {
                for (int sz = -radius; sz <= radius; sz++) {
                    if (getLevel().getBlockState(newPos.offset(sx, sy, sz)).is(BlockTags.REPLACEABLE_PLANTS)) getLevel().destroyBlock(newPos.offset(sx, sy, sz), false, ownerPlayer);
                }
            }
        }

        if (tickCount == 100 || !getLevel().getBlockState(newPos).isAir()) remove(RemovalReason.DISCARDED);
    }
}
