package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AquafloraFlowersAwayAttack extends AquafloraAttack {
    public AquafloraFlowersAwayAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_POLTERGEIST_RETREAT;
    }

    @Override
    public int getCooldown() {
        return CSConfig.COMMON.aquafloraBloomShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 0;
    }

    @Override
    public boolean getCondition() {
        return getTagController().getBoolean(CHECK_PASSIVE) && player.isCrouching();
    }

    @Override
    public void startUsing() {
        sendExpandingParticles(getPlayer().level, ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 55, 1.2F);
        CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_FLOWER, 0, -1, 0);
        List<Entity> entities = iterateEntities(getPlayer().level, createAABB(player.blockPosition(), 12));
        player.playSound(CSSoundRegistry.CS_BLING.get(), 0.4F, 0.5F);
        for (Entity target : entities) {
            if (target instanceof LivingEntity lt && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                CSEffect.createInstance(player, target, CSEffectTypes.AQUAFLORA_FLOWER_BIND);
                hurtNoKB(player, lt, (float) (double) CSConfig.COMMON.aquafloraBloomShiftSkillDmg.get());
                target.setDeltaMovement((player.getX() - target.getX()) * 0.35, (player.getY() - target.getY()) * 0.35, (player.getZ() - target.getZ()) * 0.35);
            }
        }
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(CHECK_PASSIVE, false);
    }
}
