package com.aqutheseal.celestisynth.common.attack.frostbound;

import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.projectile.FrostboundShard;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public abstract class FrostboundAttack extends WeaponAttackInstance {
    public FrostboundAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    public FrostboundAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    public void shootShard(int amount) {
        for (int i = 0; i < amount; i++) {
            double xx = level.random.nextGaussian() * 1.5;
            double yy = level.random.nextGaussian() * 1.5;
            double zz = level.random.nextGaussian() * 1.5;
            shootShard(xx, yy, zz);
        }
        player.playSound(SoundEvents.BLAZE_SHOOT);
    }

    public void shootShard(double xx, double yy, double zz) {
        LivingEntity target = null;
        List<Entity> list = iterateEntities(level, createAABB(player.blockPosition().above(), 32)).stream().filter(entity -> entity instanceof LivingEntity && entity != player).toList();
        if (!list.isEmpty()) {
            int indexLucky = level.random.nextInt(list.size());
            if (list.get(indexLucky) instanceof LivingEntity indexLuckyLiving) {
                target = indexLuckyLiving;
            }
        }
        if (target != null) {
            FrostboundShard shard = new FrostboundShard(CSEntityTypes.FROSTBOUND_SHARD.get(), player, level);
            double d0 = target.getX() - (player.getX() + xx);
            double d1 = target.getY((double) 1 / 3) - (shard.getY() + yy);
            double d2 = target.getZ() - (player.getZ() + zz);
            double d3 = Math.sqrt(d0 * d0 + d2 * d2);
            shard.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, 2);
            level.addFreshEntity(shard);
        }
    }
}
