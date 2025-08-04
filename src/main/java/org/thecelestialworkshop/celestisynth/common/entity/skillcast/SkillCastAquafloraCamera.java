package org.thecelestialworkshop.celestisynth.common.entity.skillcast;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class SkillCastAquafloraCamera extends Projectile {
    public SkillCastAquafloraCamera(EntityType<?> pEntityType, Level pLevel) {
        super((EntityType<? extends Projectile>) pEntityType, pLevel);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();
        if (!isRemoved() && this.getOwner() != null) {
            this.moveTo(this.position().add(0, 0.1, 0));
            //this.lookAt(EntityAnchorArgument.Anchor.FEET, getOwner().getEyePosition());
        }

        if (tickCount >= 120) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void defineSynchedData() {
    }
}
