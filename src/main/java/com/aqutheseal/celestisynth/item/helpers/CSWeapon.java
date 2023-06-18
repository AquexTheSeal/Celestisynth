package com.aqutheseal.celestisynth.item.helpers;

import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.Random;

public interface CSWeapon {

    String CS_CONTROLLER_TAG_ELEMENT = "csController";
    String CS_EXTRAS_ELEMENT = "csExtras";

    String ANIMATION_TIMER_KEY = "cs.animationTimer";
    String ANIMATION_BEGUN_KEY = "cs.hasAnimationBegun";

    SoundEvent[] BASE_WEAPON_EFFECTS = {
            CSSoundRegistry.CS_SWORD_SWING.get(),
            CSSoundRegistry.CS_SWORD_SWING_FIRE.get(),
            CSSoundRegistry.CS_AIR_SWING.get(),
            CSSoundRegistry.CS_SWORD_CLASH.get(),
            CSSoundRegistry.CS_FIRE_SHOOT.get(),
            CSSoundRegistry.CS_IMPACT_HIT.get()
    };

    int getSkillsAmount();

    default int getPassiveAmount() {
        return 1;
    }

    default boolean hasPassive() {
        return false;
    }

    default void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
    }

    default void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = BASE_WEAPON_EFFECTS[new Random().nextInt(length)];
        entity.playSound(randomSound, 0.55F, 0.5F + new Random().nextFloat());
    }

    default void constantAttack(Player holder, LivingEntity target, float damage) {
        double preAttribute = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
        target.invulnerableTime = 0;
        target.hurt(DamageSource.playerAttack(holder), damage);
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(preAttribute);
    }

    default void setDeltaPlayer(Player player, double x, double y, double z) {
        player.hurtMarked = true;
        player.setDeltaMovement(x, y, z);
    }

    default void setDeltaPlayer(Player player, Vec3 vec) {
        player.hurtMarked = true;
        player.setDeltaMovement(vec);
    }

    default void useAndDamageItem(ItemStack pStack, Level pLevel, Player player, int damageAmount) {
        if (!pLevel.isClientSide) {
            pStack.hurtAndBreak(damageAmount, player, (p_43388_) -> {
                p_43388_.broadcastBreakEvent(player.getUsedItemHand());
            });
        }
        player.awardStat(Stats.ITEM_USED.get(pStack.getItem()));
    }

    default void sendExpandingParticles(Level level, ParticleType<?> particleType, double x, double y, double z, int amount, float expansionMultiplier) {
        for (int i = 0; i < amount; i++) {
            Random random = new Random();
            float offX = (-0.5f + random.nextFloat()) * expansionMultiplier;
            float offY = (-0.5f + random.nextFloat()) * expansionMultiplier;
            float offZ = (-0.5f + random.nextFloat()) * expansionMultiplier;
            CSUtilityFunctions.sendParticles(level, particleType, x, y, z, 0, offX, offY, offZ);
        }
    }

    default void sendExpandingParticles(Level level, ParticleType<?> particleType, BlockPos origin, int amount, float expansionMultiplier) {
        sendExpandingParticles(level, particleType, origin.getX(), origin.getY(), origin.getZ(), amount, expansionMultiplier);
    }

    default Entity getLookAtEntity(Player player, double range) {
        double distance = range * range;
        Vec3 vec = player.getEyePosition(1);
        Vec3 vec1 = player.getViewVector(1);
        Vec3 targetVec = vec.add(vec1.x * range, vec1.y * range, vec1.z * range);
        AABB aabb = player.getBoundingBox().expandTowards(vec1.scale(range)).inflate(4.0D, 4.0D, 4.0D);
        EntityHitResult result = ProjectileUtil.getEntityHitResult(player, vec, targetVec, aabb,(entity) -> !entity.isSpectator(), distance);
        return result != null ? result.getEntity() : null;
    }

    default double calculateXLook(Player player) {
        return -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
    }

    default double calculateYLook(Player player) {
        return -Mth.sin(player.getXRot() * ((float) Math.PI / 180F));
    }

    default double calculateZLook(Player player) {
        return Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
    }
}
