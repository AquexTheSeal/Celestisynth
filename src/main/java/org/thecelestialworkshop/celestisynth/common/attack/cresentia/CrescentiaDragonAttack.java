package org.thecelestialworkshop.celestisynth.common.attack.cresentia;

import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.CrescentiaDragon;
import org.thecelestialworkshop.celestisynth.common.registry.CSEntityTypes;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import software.bernie.geckolib.core.object.Color;

public class CrescentiaDragonAttack extends WeaponAttackInstance {

    public CrescentiaDragonAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    @Override
    public PlayerAnimationContainer getAnimation() {
        return CSPlayerAnimations.ANIM_CRESCENTIA_THROW.get();
    }

    @Override
    public int getCooldown() {
        return 60;
    }

    @Override
    public int getAttackStopTime() {
        return 30;
    }

    @Override
    public boolean getCondition() {
        return player.isShiftKeyDown();
    }

    @Override
    public void startUsing() {
        useAndDamageItem(getStack(), level, player, 5);
        this.chantMessage(player, "crescentia2", 30, Color.WHITE.argbInt());
    }

    @Override
    public void tickAttack() {
        if (getTimerProgress() <= 20) {
            setDeltaPlayer(player, 0, 0, 0);
        }

        if (getTimerProgress() == 20) {
            this.chantMessage(player, "crescentia3", 20, Color.PINK.argbInt());

            FloatArrayList angles = new FloatArrayList();
            angles.add(0.0F);
            angles.add(-30.0F);
            angles.add(30.0F);
            if (stack.getEnchantmentLevel(Enchantments.MULTISHOT) > 0) {
                angles.add(-15.0F);
                angles.add(15.0F);
            }

            for (float i : angles) {
                CrescentiaDragon dragon = new CrescentiaDragon(CSEntityTypes.CRESCENTIA_DRAGON.get(), player, level);
                dragon.moveTo(player.getX(), dragon.getY(), player.getZ());
                Vec3 vec31 = player.getUpVector(1.0F);
                Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(i * ((float) Math.PI / 180F), vec31.x, vec31.y, vec31.z);
                Vec3 vec3 = player.getViewVector(1.0F);
                Vector3f shootAngle = vec3.toVector3f().rotate(quaternionf);
                dragon.shoot(shootAngle.x, shootAngle.y, shootAngle.z, 1.5F, 0);
                dragon.damage = this.calculateAttributeDependentDamage(player, stack, 0.08F);
                level.addFreshEntity(dragon);
            }

            player.playSound(SoundEvents.ELDER_GUARDIAN_CURSE, 1F, 2F + (float) (level.random.nextGaussian() * 0.25F));
            player.playSound(SoundEvents.ENDER_DRAGON_HURT, 1F, 0.5F + (float) (level.random.nextGaussian() * 0.25F));
            player.playSound(CSSoundEvents.WHIRLWIND.get(), 0.2F, 0.5F + (float) (level.random.nextGaussian() * 0.25F));
        }
    }

    @Override
    public void stopUsing() {
    }
}
