package org.thecelestialworkshop.celestisynth.common.attack.aquaflora;

import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.registry.CSSoundEvents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public abstract class AquafloraAttack extends WeaponAttackInstance {
    public static final String CHECK_PASSIVE = "cs.checkPassiveIfBlooming";

    public AquafloraAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    public AquafloraAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    public void createHitEffect(ItemStack itemStack, Level level, Player player, LivingEntity target) {
        RandomSource random = level.getRandom();
        sendExpandingParticles(level, ParticleTypes.END_ROD, target.getX(), target.getY(), target.getZ(), 10, 0.1F);
        player.playSound(CSSoundEvents.BLING.get(), 0.1F, 1F + random.nextFloat());
    }
}
