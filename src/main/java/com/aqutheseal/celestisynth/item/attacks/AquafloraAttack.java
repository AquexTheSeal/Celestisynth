package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.item.helpers.WeaponAttackInstance;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public abstract class AquafloraAttack extends WeaponAttackInstance {
    public static final String CHECK_PASSIVE = "cs.checkPassiveIfBlooming";

    public AquafloraAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    public AquafloraAttack(Player player, ItemStack stack) {
        super(player, stack);
    }

    public void createHitEffect(ItemStack itemStack, Level level, Player player, LivingEntity target) {
        Random random = new Random();
        sendExpandingParticles(level, ParticleTypes.END_ROD, target.getX(), target.getY(), target.getZ(), 25, 0.5F);
        player.playSound(CSSoundRegistry.CS_BLING.get(), 0.2F, 1F + random.nextFloat());
    }
}
