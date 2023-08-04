package com.aqutheseal.celestisynth.item.helpers;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.item.weapons.AquafloraItem;
import com.aqutheseal.celestisynth.network.CSNetwork;
import com.aqutheseal.celestisynth.network.util.ShakeScreenServerPacket;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
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

    // I recommend utilizing this method, replacing the usage of the "inventoryTick" method, as it occasionally encounters server-client conflicts.
    default void forceTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
    }

    default void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = BASE_WEAPON_EFFECTS[new Random().nextInt(length)];
        entity.playSound(randomSound, 0.35F, 0.5F + new Random().nextFloat());
    }

    default void hurtNoKB(Player holder, LivingEntity target, float damage, boolean isBlockable) {
        if (damage == 0) return;
        double preAttribute = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
        target.invulnerableTime = 0;
        if (!(isBlockable && target.getUseItem().getItem() instanceof ShieldItem)) {
            target.hurt(DamageSource.playerAttack(holder), damage);
        } else {
            useAndDamageItem(target.getUseItem(), target.level, target, (int) (damage / 3));
        }
        target.doEnchantDamageEffects(holder, target);
        target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(preAttribute);
    }

    default void hurtNoKB(Player holder, LivingEntity target, float damage) {
        hurtNoKB(holder, target, damage, false);
    }

    default void setDeltaPlayer(Player player, double x, double y, double z) {
        player.hurtMarked = true;
        player.setDeltaMovement(x, y, z);
    }

    default void setDeltaPlayer(Player player, Vec3 vec) {
        player.hurtMarked = true;
        player.setDeltaMovement(vec);
    }

    default float getSharpnessValue(ItemStack stack, float multiplier) {
        return EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, stack) * multiplier;
    }

    default void useAndDamageItem(ItemStack pStack, Level pLevel, LivingEntity player, int damageAmount) {
        if (!pLevel.isClientSide) {
            pStack.hurtAndBreak(damageAmount, player, (p_43388_) -> {
                p_43388_.broadcastBreakEvent(player.getUsedItemHand());
            });
        }
        if (player instanceof Player playerE) {
            playerE.awardStat(Stats.ITEM_USED.get(pStack.getItem()));
        }
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

    default AABB createAABB(BlockPos pos, double range) {
        return createAABB(pos.getX(), pos.getY(), pos.getZ(), range);
    }

    default AABB createAABB(double x, double y, double z, double range) {
        return new AABB(x + range, y + range, z + range, x - range, y - range, z - range);
    }

    default List<Entity> iterateEntities(Level level, AABB aabb) {
        return level.getEntitiesOfClass(Entity.class, aabb);
    }

    default boolean checkDualWield(Player player, Class<? extends CSWeapon> weapon) {
        return weapon.isInstance(player.getMainHandItem().getItem()) && (weapon.isInstance(player.getOffhandItem().getItem()));
    }

    static void disableRunningWeapon(Entity entity) {
        if (entity instanceof Player player) {
            AnimationManager.playAnimation(entity.level, AnimationManager.AnimationsList.CLEAR);
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (player.getItemBySlot(slot).getItem() instanceof CSWeapon) {
                    CompoundTag data = player.getItemBySlot(slot).getOrCreateTagElement(CSWeapon.CS_CONTROLLER_TAG_ELEMENT);
                    CompoundTag dataAlt = player.getItemBySlot(slot).getOrCreateTagElement(CSWeapon.CS_EXTRAS_ELEMENT);
                    data.getAllKeys().clear();
                    dataAlt.getAllKeys().clear();
                    if (player.getItemBySlot(slot).getItem() instanceof AquafloraItem) {
                        if (player.level.isClientSide()) {
                            player.setXRot(data.getFloat(AquafloraItem.INITIAL_VIEW_ANGLE));
                            Minecraft.getInstance().options.setCameraType(CameraType.values()[data.getInt(AquafloraItem.INITIAL_PERSPECTIVE)]);
                        }
                    }
                }
            }
        }
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

    default void shakeScreensForNearbyPlayers(Player holder, Level level, double range, int maxDuration, int startFadingOut, float maxIntensity) {
        if (level.isClientSide()) {
            List<Player> entities = level.getEntitiesOfClass(Player.class, holder.getBoundingBox().inflate(range, range, range));
            for (Player entity : entities) {
                shakeScreens(entity, maxDuration, startFadingOut, Math.max(0, maxIntensity - (float) (entity.distanceToSqr(holder) * 0.0001)));
            }
        }
    }

    default void shakeScreens(Player target, int duration, int startFadingOut, float intensity) {
        if (target != null) {
            CSNetwork.sendToServer(new ShakeScreenServerPacket(target.getUUID(), duration, startFadingOut, intensity));
        }
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
