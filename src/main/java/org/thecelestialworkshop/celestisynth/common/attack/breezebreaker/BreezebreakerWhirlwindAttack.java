package org.thecelestialworkshop.celestisynth.common.attack.breezebreaker;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.common.entity.skillcast.SkillCastBreezebreakerTornado;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class BreezebreakerWhirlwindAttack extends BreezebreakerAttack {

    public BreezebreakerWhirlwindAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        if (player.getMainHandItem() == stack && player.getOffhandItem() != stack) return CSPlayerAnimations.ANIM_BREEZEBREAKER_SHIFT_RIGHT.get();
        else if (player.getOffhandItem() == stack && player.getMainHandItem() != stack) return CSPlayerAnimations.ANIM_BREEZEBREAKER_SHIFT_LEFT.get();
        else if (player.getOffhandItem() == stack && player.getMainHandItem() == stack) {
            boolean shouldShiftRight = player.getRandom().nextBoolean();

            return shouldShiftRight ? CSPlayerAnimations.ANIM_BREEZEBREAKER_SHIFT_RIGHT.get() : CSPlayerAnimations.ANIM_BREEZEBREAKER_SHIFT_LEFT.get();
        }
        return CSPlayerAnimations.CLEAR.get();
    }

    @Override
    public void startUsing() {
        super.startUsing();

        useAndDamageItem(stack, level, player, 3);
    }

    @Override
    public int getCooldown() {
        return buffStateModified(35);
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return player.isCrouching();
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 10) {
            player.playSound(CSSoundEvents.WIND_STRIKE.get());
            player.playSound(CSSoundEvents.WHIRLWIND.get());

            if (!level.isClientSide()) {
                SkillCastBreezebreakerTornado tornadoSkillCast = CSEntityTypes.BREEZEBREAKER_TORNADO.get().create(level);

                tornadoSkillCast.setOwnerUUID(player.getUUID());
                tornadoSkillCast.setAngleX((float) calculateXLook(player));
                tornadoSkillCast.setAngleY((float) calculateYLook(player));
                tornadoSkillCast.setAngleZ((float) calculateZLook(player));
                tornadoSkillCast.setAddAngleX((float) calculateXLook(player));
                tornadoSkillCast.setAddAngleY((float) calculateYLook(player));
                tornadoSkillCast.setAddAngleZ((float) calculateZLook(player));
                tornadoSkillCast.moveTo(player.getX(), player.getY() + 1, player.getZ());
                tornadoSkillCast.damage = this.calculateAttributeDependentDamage(player, stack, 0.1F);

                level.addFreshEntity(tornadoSkillCast);
            }
        }
    }

    @Override
    public void stopUsing() {

    }
}
