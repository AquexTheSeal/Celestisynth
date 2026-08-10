package org.thecelestialworkshop.celestisynth.common.item.weapons;

import org.thecelestialworkshop.celestisynth.api.animation.player.AnimationManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.LayerManager;
import org.thecelestialworkshop.celestisynth.api.item.CSGeoItem;
import org.thecelestialworkshop.celestisynth.common.attack.base.WeaponAttackInstance;
import org.thecelestialworkshop.celestisynth.common.attack.keres.KeresRendAttack;
import org.thecelestialworkshop.celestisynth.common.attack.keres.KeresSlashAttack;
import org.thecelestialworkshop.celestisynth.common.attack.keres.KeresSmashAttack;
import org.thecelestialworkshop.celestisynth.api.item.SwingParticleContainer;
import org.thecelestialworkshop.celestisynth.common.entity.projectile.KeresShadow;
import org.thecelestialworkshop.celestisynth.common.item.base.SkilledSwordItem;
import org.thecelestialworkshop.celestisynth.common.registry.*;
import org.thecelestialworkshop.celestisynth.util.ParticleUtil;
import com.google.common.collect.ImmutableList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.GeoAnimatable;

public class KeresItem extends SkilledSwordItem implements CSGeoItem {
    public static final String PASSIVE_STACK = "cs.keresStack";

    public KeresItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "keres";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new KeresSlashAttack(player, stack, dur),
                new KeresRendAttack(player, stack, dur),
                new KeresSmashAttack(player, stack, dur)
        );
    }

    @Override
    public @Nullable SwingParticleContainer getSwingContainer(LivingEntity holder, ItemStack stack) {
        if (holder.hasEffect(CSMobEffects.HELLBANE) && holder.getRandom().nextInt(5) == 1) {
            return new SwingParticleContainer(CSParticleTypes.KERES_OMEN.get(), 2.8F);
        }
        return new SwingParticleContainer(CSParticleTypes.KERES_ASH.get(), 2.8F);
    }

    @Override
    public int getSkillsAmount() {
        return 3;
    }

    @Override
    public int getPassiveAmount() {
        return 3;
    }

    @Override
    public void startUsing(Level level, Player player, InteractionHand interactionHand) {
        super.startUsing(level, player, interactionHand);
        int layer = interactionHand == InteractionHand.OFF_HAND ? LayerManager.MAIN_LAYER : LayerManager.MIRRORED_LAYER;
        AnimationManager.playAnimation(level, CSPlayerAnimations.ANIM_KERES_CHARGE.get(), layer);
    }

    @Override
    public void onUseTick(Level pLevel, LivingEntity pEntity, ItemStack pStack, int pRemainingUseDuration) {
        super.onUseTick(pLevel, pEntity, pStack, pRemainingUseDuration);
        int dur = this.getUseDuration(pStack, pEntity) - pRemainingUseDuration;

        int durThreshold = dur >= 200 ? 15 : 30;
        if (dur % durThreshold == 0) {
            float sacrificeThreshold = 1f;
            if ((pEntity.getHealth() - sacrificeThreshold) > 0 && !isCreativeOrSpectator(pEntity)) {
                pEntity.setHealth(pEntity.getHealth() - sacrificeThreshold);
                if (pEntity instanceof Player player) {
                    player.getFoodData().setSaturation(player.getFoodData().getSaturationLevel() + 1);
                }
                this.sacrificeEffect(pLevel, pEntity, dur);
            } else {
                if (!isCreativeOrSpectator(pEntity)) {
                    if (pEntity instanceof Player player) {
                        player.getCooldowns().addCooldown(this, 20);
                        AnimationManager.playAnimation(pLevel, CSPlayerAnimations.CLEAR.get());
                        if (pLevel.isClientSide) {
                            player.displayClientMessage(Component.translatable("item.celestisynth.keres.notice").withStyle(ChatFormatting.RED), true);
                        }
                    }
                    pEntity.stopUsingItem();
                } else {
                    this.sacrificeEffect(pLevel, pEntity, dur);
                }
            }
        }

        if (pEntity instanceof Player player) {
            if (player.isShiftKeyDown() && dur < 200) {
                for (int i = 0; i < 22.5; i++) {
                    ParticleUtil.sendParticle(pLevel, CSParticleTypes.KERES_OMEN.get(), player.getX() + (calculateXLook(player) * ((double) dur / 3)) + (Mth.sin(i) * 5), player.getY(), player.getZ() + (calculateZLook(player) * ((double) dur / 3)) + (Mth.cos(i) * 5), -Mth.sin(i) * 0.3 , 0.5, -Mth.cos(i) * 0.3);
                }
            }
        }

        if (dur >= 200) {
            if (pEntity instanceof Player player) {
                shakeScreensForNearbyPlayers(player, pLevel, 12, 30, 15,  0.01F);
            }
            ParticleUtil.sendParticle(pLevel, ParticleTypes.FLASH, pEntity.getX(), pEntity.getY() + 4, pEntity.getZ());
        }
    }

    public void sacrificeEffect(Level pLevel, LivingEntity pEntity, int dur) {
        pEntity.playSound(CSSoundEvents.HEARTBEAT.get(), 1f, (float) (1.0 + (pLevel.random.nextGaussian() * 0.2)));
        if (dur > 0) {
            this.pulseImageOnUI(pEntity, "keres_carnage_" + pLevel.random.nextInt(3), 0);
        }
        if (pEntity instanceof Player player) {
            player.displayClientMessage(Component.translatable("item.celestisynth.keres.notice1").withStyle(ChatFormatting.RED), true);
        }
        for (int i = 0; i < 10; i++) {
            var xr = pLevel.random.nextFloat();
            var zr = pLevel.random.nextFloat();
            ParticleUtil.sendParticle(pLevel, ParticleTypes.SMOKE, pEntity.position().add(xr, 0, zr), Vec3.ZERO.add(0, 0.5 * pLevel.random.nextFloat(), 0));
        }
    }

    public boolean isCreativeOrSpectator(Entity target) {
        if (target instanceof Player player) {
            return player.isCreative() || player.isSpectator();
        }
        return false;
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);

        if (isSelected) {
            if (entity instanceof LivingEntity source) {
                if (source.hasEffect(CSMobEffects.HELLBANE)) {
                    double xSin = Mth.sin((float) (0.2 * source.tickCount)) * 3;
                    double zCos = Mth.cos((float) (0.2 * source.tickCount)) * 3;
                    ParticleUtil.sendParticle(level, CSParticleTypes.KERES_ASH.get(), entity.getX() + xSin, entity.getY() + 1, entity.getZ() + zCos);
                    ParticleUtil.sendParticle(level, CSParticleTypes.KERES_ASH.get(), entity.getX() - xSin, entity.getY() + 1, entity.getZ() - zCos);

                    if (source.tickCount % 10 == 0) {
                        if (!level.isClientSide()) {
                            KeresShadow shadow = new KeresShadow(CSEntityTypes.KERES_SHADOW.get(), source, level);
                            shadow.moveTo(source.getX(), shadow.getY() - 1, source.getZ());

                            Entity lookAtTarget = getLookedAtEntity(source, 128);
                            LivingEntity observedLivingTarget = lookAtTarget instanceof LivingEntity observedLiving ? observedLiving : null;
                            if (observedLivingTarget != null) {
                                shadow.setHomingTarget(observedLivingTarget);
                            }

                            shadow.damage = this.calculateAttributeDependentDamage(source, itemStack, 0.2F);
                            shadow.shootFromRotation(source, (float) (source.getRandom().nextGaussian() * 180), -15 - (float) (source.getRandom().nextDouble() * 75), 0, 1F, 0);
                            shadow.setDeltaMovement(level.random.nextGaussian() * 0.25, 0.4, level.random.nextGaussian() * 0.25);
                            level.addFreshEntity(shadow);
                        }
                        source.playSound(SoundEvents.WITHER_SHOOT, 0.1F, 1.5F + (source.getRandom().nextFloat() * 0.5F));
                    }
                }
            }
        } else {
            this.attackController(itemStack).putInt(PASSIVE_STACK, 0);
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        boolean flag = super.hurtEnemy(itemStack, entity, source);
        if (flag) {
            double lifesteal = 0.35;
            if (source.hasEffect(CSMobEffects.HELLBANE)) {
                lifesteal = 0.85;
            }
            source.heal((float) lifesteal);
            this.attackController(itemStack).putInt(PASSIVE_STACK, this.attackController(itemStack).getInt(PASSIVE_STACK) + 1);
            if (this.attackController(itemStack).getInt(PASSIVE_STACK) >= 5) {
                if (source instanceof Player player) {
                    player.getCooldowns().removeCooldown(this);
                }
                source.addEffect(new MobEffectInstance(CSMobEffects.HELLBANE, 100, 0));
                this.attackController(itemStack).putInt(PASSIVE_STACK, 0);
            }
        }
        return flag;
    }

    @Override
    public boolean supportsEnchantment(ItemStack stack, net.minecraft.core.Holder<Enchantment> enchantment) {
        if (enchantment.is(Enchantments.MULTISHOT)) {
            return true;
        }
        return super.supportsEnchantment(stack, enchantment);
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack, net.minecraft.world.entity.LivingEntity useEntity) {
        return 72000;
    }
}
