package org.thecelestialworkshop.celestisynth.common.attack.aquaflora;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.api.item.AttackHurtTypes;
import org.thecelestialworkshop.celestisynth.common.entity.base.CSEffectEntity;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import org.thecelestialworkshop.celestisynth.common.registry.CSVisualTypes;
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
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_POLTERGEIST_RETREAT.get();
    }

    @Override
    public int getCooldown() {
        return 30;
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
        sendExpandingParticles(level, ParticleTypes.END_ROD, player.getX(), player.getY(), player.getZ(), 55, 1.2F);
        CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_FLOWER.get(), 0, -1, 0);

        List<Entity> entities = iterateEntities(level, createAABB(player.blockPosition(), 12));

        player.playSound(CSSoundEvents.BLING.get(), 0.4F, 0.5F);

        for (Entity target : entities) {
            if (target instanceof LivingEntity lt && target != player && target.isAlive() && !player.isAlliedTo(target)) {
                CSEffectEntity.createInstance(player, target, CSVisualTypes.AQUAFLORA_FLOWER_BIND.get());
                this.attributeDependentAttack(player, lt, stack, 0.5F, AttackHurtTypes.NO_KB);
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
