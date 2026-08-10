package org.thecelestialworkshop.celestisynth.common.attack.keres;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresRend;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.util.Color;

public class KeresRendAttack extends WeaponAttackInstance {
    public KeresRendAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_KERES_REND.get();
    }

    @Override
    public int getCooldown() {
        return 100;
    }

    @Override
    public int getAttackStopTime() {
        return 40;
    }

    @Override
    public boolean getCondition() {
        return heldDuration >= 200;
    }

    @Override
    public void startUsing() {
        player.playSound(CSSoundEvents.BASS_DROP.get(), 0.3F, 1F);
        this.chantMessage(player, "keres", 20, Color.WHITE.getColor());
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() == 13) {
            player.playSound(CSSoundEvents.STEP.get(), 0.3F, 0.5F);
            this.chantMessage(player, "keres1", 20, Color.ofRGB(233, 116, 81).getColor());
        }

        if (getTimerProgress() == 22) {
            player.playSound(CSSoundEvents.BASS_PULSE.get(), 0.75F, 1F);
        }

        if (getTimerProgress() == 25) {
            this.chantMessage(player, "keres2", 40, Color.RED.getColor());

            if (!level.isClientSide) {
                Vec3 lookAngleNormalized = player.getLookAngle().normalize();
                KeresRend rend = new KeresRend(CSEntityTypes.KERES_REND.get(), player, level);
                rend.moveTo(player.position().add(-lookAngleNormalized.x() * 9, -3, -lookAngleNormalized.z() * 9));
                Vec3 vec31 = player.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = player.getViewVector(1.0F);
                Vector3f shootAngle = vec3.toVector3f().rotate(quaternionf);
                rend.baseDamage = this.calculateAttributeDependentDamage(player, stack, 1.5F);
                rend.shoot(shootAngle.x, 0, shootAngle.z, 1F, 0);
                level.addFreshEntity(rend);
            }

            this.flashScreen(player, 255, 15);
        }
    }

    @Override
    public void stopUsing() {
    }
}
