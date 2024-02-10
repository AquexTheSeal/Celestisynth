package com.aqutheseal.celestisynth.common.entity.skill;

import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.base.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.item.weapons.CrescentiaItem;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SkillCastCrescentiaRanged extends EffectControllerEntity {

    public SkillCastCrescentiaRanged(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItems.CRESCENTIA.get();
    }

    @Override
    public void tick() {
        super.tick();

        UUID ownerUuid = this.getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : this.level().getPlayerByUUID(ownerUuid);

        setAngleX(getAngleX() + getAddAngleX());
        setAngleY(getAngleY() + getAddAngleY());
        setAngleZ(getAngleZ() + getAddAngleZ());

        double newX = getX() + getAngleX();
        double newY = getY() + getAngleY();
        double newZ = getZ() + getAngleZ();
        BlockPos newPos = new BlockPos((int) newX, (int) newY, (int) newZ);

        double range = 7.0;
        List<Entity> entities = level().getEntitiesOfClass(Entity.class, new AABB(newX + range, newY + range, newZ + range, newX - range, newY - range, newZ - range));
        ItemStack fireworkStack = new ItemStack(Items.FIREWORK_ROCKET);

        for (Entity entityBatch : entities) {
            if (entityBatch instanceof LivingEntity target) {
                if (target != ownerPlayer && target.isAlive()) {
                    fromInterfaceWeapon().initiateAbilityAttack(ownerPlayer, target, (float) (double) CSConfigManager.COMMON.crescentiaShiftSkillDmg.get(), AttackHurtTypes.RAPID_PIERCE);
                    target.hurt(damageSources().playerAttack(ownerPlayer), (float) ((double) CSConfigManager.COMMON.crescentiaShiftSkillDmg.get()));
                    target.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                }
            }
            if (entityBatch instanceof Projectile projectile) {
                CrescentiaItem.createCrescentiaFirework(fireworkStack, level(), ownerPlayer, projectile.getX(), projectile.getY(), projectile.getZ(), true);
                projectile.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F);
                projectile.remove(RemovalReason.DISCARDED);
            }
        }

        if (tickCount % 3 == 0) {
            float offX = (random.nextFloat() * 20) - 10;
            float offY = (random.nextFloat() * 20) - 10;
            float offZ = (random.nextFloat() * 20) - 10;

            CrescentiaItem.createCrescentiaFirework(fireworkStack, level(), ownerPlayer, (getX() + getAngleX()) + offX, (getY() + getAngleY()) + offY,  (getZ() + getAngleZ()) + offZ, false);
            CrescentiaItem.createCrescentiaFirework(fireworkStack, level(), ownerPlayer, getAngleX() + offZ, (getAngleY() - 1.5) + offX,  getAngleZ() + offY, false);
        }

        if (random.nextBoolean()) CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.CRESCENTIA_THROW.get(), getAngleX(), getAngleY() - 1.5, getAngleZ());
        else CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.CRESCENTIA_THROW_INVERTED.get(), getAngleX(), getAngleY() - 1.5, getAngleZ());

        CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.SOLARIS_AIR.get(), getAngleX(), getAngleY(), getAngleZ());
        playRandomBladeSound(BASE_WEAPON_EFFECTS.length, newX, newY, newZ);

        int radius = 2;
        for (int sx = -radius; sx <= radius; sx++) {
            for (int sy = -radius; sy <= radius; sy++) {
                for (int sz = -radius; sz <= radius; sz++) {
                    if (level().getBlockState(newPos.offset(sx, sy, sz)).canBeReplaced()) level().destroyBlock(newPos.offset(sx, sy, sz), false, ownerPlayer);
                }
            }
        }

        if (tickCount == 100 || !level().getBlockState(newPos).isAir()) {
            level().explode(ownerPlayer, newX, newY, newZ, 3.0F, Level.ExplosionInteraction.TNT);
            CrescentiaItem.createCrescentiaFirework(fireworkStack, level(), ownerPlayer, newX, newY, newZ, true);
            remove(RemovalReason.DISCARDED);
        }
    }

    public void playRandomBladeSound(int length, double x, double y, double z) {
        SoundEvent randomSound = BASE_WEAPON_EFFECTS[random.nextInt(length)];
        level().playSound(level().getPlayerByUUID(getOwnerUuid()), x, y, z, randomSound, SoundSource.HOSTILE, 0.10F, 0.5F + new Random().nextFloat());
    }
}
