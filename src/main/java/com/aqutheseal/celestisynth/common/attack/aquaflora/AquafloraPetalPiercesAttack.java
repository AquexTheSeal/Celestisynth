package com.aqutheseal.celestisynth.common.attack.aquaflora;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class AquafloraPetalPiercesAttack extends AquafloraAttack {
    public AquafloraPetalPiercesAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        
        if (player.getMainHandItem() == stack && getPlayer().getOffhandItem() == stack) return getPlayer().level().random.nextBoolean() ? AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT : AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT;
        else return getPlayer().getMainHandItem() == stack ? AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_RIGHT : AnimationManager.AnimationsList.ANIM_AQUAFLORA_PIERCE_LEFT;
    }

    @Override
    public int getCooldown() {
        return CSConfigManager.COMMON.aquafloraSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return !getTagController().getBoolean(CHECK_PASSIVE) && !player.isCrouching();
    }

    @Override
    public void startUsing() {
        CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_PIERCE_START.get(), calculateXLook(player) * 3, 1.2 + calculateYLook(player) * 3, calculateZLook(player) * 3);
        getPlayer().playSound(CSSoundEvents.CS_BLING.get(), 0.15F, 0.5F);

        if (getPlayer().level().isClientSide()) shakeScreens(player, 15, 5, 0.02F);
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() >= 0 && getTimerProgress() <= 15) {
            getPlayer().playSound(CSSoundEvents.CS_AIR_SWING.get(), 0.25F, 1.3F + getPlayer().level().random.nextFloat());
            CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_STAB.get(), -0.5 + getPlayer().level().random.nextDouble() + calculateXLook(player) * 3, (-0.5 + getPlayer().level().random.nextDouble()) + (2 + calculateYLook(player) * 3), -0.5 + getPlayer().level().random.nextDouble() + calculateZLook(player) * 3);

            List<Entity> entities = iterateEntities(getPlayer().level(), createAABB(player.blockPosition().offset(calculateXLook(player) * 4.5, 1 + (calculateYLook(player) * 4.5), calculateZLook(player) * 4.5), 2));
            entities.addAll(iterateEntities(getPlayer().level(), createAABB(player.blockPosition().offset(calculateXLook(player) * 3, 1 + (calculateYLook(player) * 3), calculateZLook(player) * 3), 2)));
            entities.addAll(iterateEntities(getPlayer().level(), createAABB(player.blockPosition().offset(calculateXLook(player) * 1.5, 1 + (calculateYLook(player) * 1.5), calculateZLook(player) * 1.5), 2)));

            if (entities.size() > 0) getPlayer().playSound(CSSoundEvents.CS_BLING.get(), 0.15F, 1F + getPlayer().level().random.nextFloat());

            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target)) {
                        hurtNoKB(player, target, (float) (double) CSConfigManager.COMMON.aquafloraSkillDmg.get() + getSharpnessValue(getStack(), 0.15F));
                        createHitEffect(getStack(), getPlayer().level(), player, target);
                    }
                }
            }
        }
    }

    @Override
    public void stopUsing() {
    }
}
