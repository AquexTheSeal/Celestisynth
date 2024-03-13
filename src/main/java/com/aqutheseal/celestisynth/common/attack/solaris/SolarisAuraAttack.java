package com.aqutheseal.celestisynth.common.attack.solaris;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.entity.projectile.SolarisBomb;
import com.aqutheseal.celestisynth.common.registry.CSEntityTypes;
import com.aqutheseal.celestisynth.util.ParticleUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class SolarisAuraAttack extends WeaponAttackInstance {
    public SolarisAuraAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.CLEAR;
    }

    @Override
    public int getCooldown() {
        return 0;
    }

    @Override
    public int getAttackStopTime() {
        return 0;
    }

    @Override
    public boolean getCondition() {
        return !player.onGround() && !player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
        SolarisBomb bomb = new SolarisBomb(CSEntityTypes.SOLARIS_BOMB.get(), player, level);
        bomb.setOwner(player);
        level.addFreshEntity(bomb);
        for (int i = 0; i < 180; i++) {
            double xI = Mth.sin(i * 2) * 3;
            double zI = Mth.cos(i * 2) * 3;
            ParticleUtil.sendParticles(level, ParticleTypes.FLAME, player.getX(), player.getY() + 0.2, player.getZ(), 1, xI / 8, 0, zI / 8);
        }
    }
}
