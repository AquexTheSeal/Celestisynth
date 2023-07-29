package com.aqutheseal.celestisynth.entities;

import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.weapons.CrescentiaItem;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSItemRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SkillCastCrescentiaRanged extends EffectControllerEntity {

    public SkillCastCrescentiaRanged(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItemRegistry.CRESCENTIA.get();
    }

    @Override
    public void tick() {
        super.tick();
        UUID uuid = this.getOwnerUuid();
        Player player = uuid == null ? null : this.getLevel().getPlayerByUUID(uuid);

        setAngleX(getAngleX() + getAddAngleX());
        setAngleY(getAngleY() + getAddAngleY());
        setAngleZ(getAngleZ() + getAddAngleZ());

        double newX = getX() + getAngleX();
        double newY = getY() + getAngleY();
        double newZ = getZ() + getAngleZ();
        BlockPos newPos = new BlockPos((int) newX, (int) newY, (int) newZ);

        double range = 7.0;
        List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(newX + range, newY + range, newZ + range, newX - range, newY - range, newZ - range));
        ItemStack stack = new ItemStack(Items.FIREWORK_ROCKET);
        for (Entity entityBatch : entities) {
            if (entityBatch instanceof LivingEntity target) {
                if (target != player && target.isAlive()) {
                    fromInterfaceWeapon().hurtNoKB(player, target, (float) (double) CSConfig.COMMON.crescentiaShiftSkillDmg.get());
                    target.hurt(DamageSource.playerAttack(player), (float) ((double) CSConfig.COMMON.crescentiaShiftSkillDmg.get()));
                    target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                }
            }
            if (entityBatch instanceof Projectile projectile) {
                CrescentiaItem.createCrescentiaFirework(stack, level, player, projectile.getX(), projectile.getY(), projectile.getZ(), true);
                projectile.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F);
                projectile.remove(RemovalReason.DISCARDED);
            }
        }

        if (tickCount % 3 == 0) {
            Random random = new Random();
            float offX = (random.nextFloat() * 20) - 10;
            float offY = (random.nextFloat() * 20) - 10;
            float offZ = (random.nextFloat() * 20) - 10;
            CrescentiaItem.createCrescentiaFirework(stack, level, player, (getX() + getAngleX()) + offX, (getY() + getAngleY()) + offY,  (getZ() + getAngleZ()) + offZ, false);
            CrescentiaItem.createCrescentiaFirework(stack, level, player, getAngleX() + offZ, (getAngleY() - 1.5) + offX,  getAngleZ() + offY, false);
        }

        if (new Random().nextBoolean()) {
            CSEffect.createInstance(player, this, CSEffectTypes.CRESCENTIA_THROW, getAngleX(), getAngleY() - 1.5, getAngleZ());
        } else {
            CSEffect.createInstance(player, this, CSEffectTypes.CRESCENTIA_THROW_INVERTED, getAngleX(), getAngleY() - 1.5, getAngleZ());
        }
        CSEffect.createInstance(player, this, CSEffectTypes.SOLARIS_AIR, getAngleX(), getAngleY(), getAngleZ());
        playRandomBladeSound(CSWeapon.BASE_WEAPON_EFFECTS.length, newX, newY, newZ);

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
            level.explode(player, newX, newY, newZ, 3.0F, Explosion.BlockInteraction.DESTROY);
            CrescentiaItem.createCrescentiaFirework(stack, level, player, newX, newY, newZ, true);
            this.remove(RemovalReason.DISCARDED);
        }
    }

    public void playRandomBladeSound(int length, double x, double y, double z) {
        SoundEvent randomSound = CrescentiaItem.BASE_WEAPON_EFFECTS[new Random().nextInt(length)];
        level.playSound(level.getPlayerByUUID(getOwnerUuid()), x, y, z, randomSound, SoundSource.HOSTILE, 0.10F, 0.5F + new Random().nextFloat());
    }
}
