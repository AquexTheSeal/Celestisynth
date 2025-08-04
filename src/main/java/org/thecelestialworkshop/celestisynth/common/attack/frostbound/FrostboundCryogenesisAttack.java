package org.thecelestialworkshop.celestisynth.common.attack.frostbound;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.common.entity.skillcast.SkillCastFrostboundIceCast;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class FrostboundCryogenesisAttack extends FrostboundAttack {
    public FrostboundCryogenesisAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_FROSTBOUND_CRYOGENESIS.get();
    }

    @Override
    public int getCooldown() {
        return 100;
    }

    @Override
    public int getAttackStopTime() {
        return 20;
    }

    @Override
    public boolean getCondition() {
        return player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        if (!level.isClientSide()) {
            SkillCastFrostboundIceCast frostboundIceCast = CSEntityTypes.FROSTBOUND_ICE_CAST.get().create(level);
            double offX = calculateXLook(player) * 3;
            double offZ = calculateZLook(player) * 3;
            int floorPos = getFloorPositionUnderPlayerYLevel(level, player.blockPosition().offset((int) offX, 0, (int) offZ));
            frostboundIceCast.setOwnerUUID(player.getUUID());
            frostboundIceCast.setOriginItem(stack);
            frostboundIceCast.setCastLevel(5);
            frostboundIceCast.setAngleX((float) (calculateXLook(player) * 3));
            frostboundIceCast.setAngleZ((float) (calculateZLook(player) * 3));
            frostboundIceCast.moveTo(player.getX() + offX,  floorPos + 2, player.getZ() + offZ);
            frostboundIceCast.damage = this.calculateAttributeDependentDamage(player, stack, 1.2F);
            level.addFreshEntity(frostboundIceCast);
        }
    }

    @Override
    public void tickAttack() {
    }

    @Override
    public void stopUsing() {
    }
}
