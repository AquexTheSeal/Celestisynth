package com.aqutheseal.celestisynth.common.attack.aquaflora;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
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
        return CSConfigManager.COMMON.aquafloraBloomShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 0;
    }

    @Override
    public boolean getCondition() {
        return getTagController().getBoolean(CHECK_PASSIVE) && getPlayer().isCrouching();
    }

    @Override
    public void startUsing() {
        sendExpandingParticles(getPlayer().level, ParticleTypes.END_ROD, getPlayer().getX(), getPlayer().getY(), getPlayer().getZ(), 55, 1.2F);
        CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_FLOWER.get(), 0, -1, 0);

        List<Entity> entities = iterateEntities(getPlayer().level, createAABB(player.blockPosition(), 12));

        getPlayer().playSound(CSSoundEvents.CS_BLING.get(), 0.4F, 0.5F);

        for (Entity target : entities) {
            if (target instanceof LivingEntity lt && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                CSEffectEntity.createInstance(player, target, CSVisualTypes.AQUAFLORA_FLOWER_BIND.get());
                hurtNoKB(player, lt, (float) (double) CSConfigManager.COMMON.aquafloraBloomShiftSkillDmg.get());
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