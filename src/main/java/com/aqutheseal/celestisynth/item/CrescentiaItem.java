package com.aqutheseal.celestisynth.item;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.CrescentiaRanged;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.List;
import java.util.Random;

public class CrescentiaItem extends SwordItem {
    public static final String ANIMATION_TIMER_KEY = "cs.animationTimer";
    public static final String ANIMATION_BEGUN_KEY = "cs.hasAnimationBegun";
    public static final String IS_RANGED_KEY = "cs.isRangedAttack";
    public static final SoundEvent[] CRESENTIA_SOUNDS = {
            CSSoundRegistry.SOLARIS_1.get(),
            CSSoundRegistry.SOLARIS_2.get(),
            CSSoundRegistry.SOLARIS_3.get(),
            CSSoundRegistry.SOLARIS_5.get(),
            CSSoundRegistry.SOLARIS_6.get(),
            CSSoundRegistry.SOLARIS_4.get()
    };

    public CrescentiaItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        CompoundTag itemTag = itemstack.getOrCreateTagElement("csController");

        if (!player.getCooldowns().isOnCooldown(itemstack.getItem()) && !itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            itemTag.putBoolean(IS_RANGED_KEY, player.isShiftKeyDown());
            if (level.isClientSide()) {
                if (itemTag.getBoolean(IS_RANGED_KEY)) {
                    AnimationManager.playAnimation(AnimationManager.AnimationsList.ANIM_CRESCENTIA_THROW);
                } else {
                    AnimationManager.playAnimation(AnimationManager.AnimationsList.ANIM_CRESCENTIA_STRIKE);
                }
            }
            itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
            player.getCooldowns().addCooldown(itemstack.getItem(), itemTag.getBoolean(IS_RANGED_KEY) ? 40 : 120);
        }
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public boolean onDroppedByPlayer(ItemStack item, Player player) {
        CompoundTag itemTag = item.getOrCreateTagElement("csController");
        if (itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            return false;
        }
        return super.onDroppedByPlayer(item, player);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, Level level, Entity entity, int itemSlot, boolean isSelected) {
        CompoundTag data = itemStack.getOrCreateTagElement("csController");
        if (data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (entity instanceof Player player) {
                player.getInventory().selected = itemSlot;
                if (level.isClientSide()) {
                    if (Minecraft.getInstance().screen != null) {
                        Minecraft.getInstance().screen = null;
                    }
                }
            }
        }
        if (data.getBoolean(IS_RANGED_KEY)) {
            tickRanged(itemStack, level, entity);
        } else {
            tickMelee(itemStack, level, entity);
        }
    }

    public void tickRanged(ItemStack itemStack, Level level, Entity entity) {
        CompoundTag data = itemStack.getOrCreateTagElement("csController");

        if (entity instanceof Player player && data.getBoolean(ANIMATION_BEGUN_KEY)) {
            int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
            data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);

            if (animationTimer <= 20) {
                player.setDeltaMovement(0, 0, 0);
                player.hurtMarked = true;
            }
            if (animationTimer == 20) {
                CrescentiaRanged projectile = CSEntityRegistry.CRESCENTIA_RANGED.get().create(level);
                double d0 = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
                double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
                double d2 = -Mth.sin(player.getXRot() * ((float) Math.PI / 180F));

                projectile.setOwnerUuid(player.getUUID());
                projectile.setAngleX((float) d0);
                projectile.setAngleY((float) d2);
                projectile.setAngleZ((float) d1);
                projectile.setAddAngleX((float) d0 / 2);
                projectile.setAddAngleY((float) d2 / 2);
                projectile.setAddAngleZ((float) d1 / 2);
                projectile.moveTo(player.getX(), player.getY() + 1, player.getZ());
                level.addFreshEntity(projectile);

                for (int i = 0; i < 10; i++) {
                    Random random = new Random();
                    float offX = random.nextFloat() * 12 - 6;
                    float offY = random.nextFloat() * 12 - 6;
                    float offZ = random.nextFloat() * 12 - 6;
                    createCrescentiaFirework(itemStack, level, player, player.getX() + offX, player.getY() + offY, player.getZ() + offZ, true, animationTimer);
                    entity.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
                }
            }
            if (animationTimer >= 30) {
                data.putInt(ANIMATION_TIMER_KEY, 0);
                data.putBoolean(ANIMATION_BEGUN_KEY, false);
            }
        }
    }

    public void tickMelee(ItemStack itemStack, Level level, Entity entity) {
        CompoundTag data = itemStack.getOrCreateTagElement("csController");

        if (entity instanceof Player player && data.getBoolean(ANIMATION_BEGUN_KEY)) {
            int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
            data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
            //player.setDeltaMovement(0, 0, 0);
            if (animationTimer >= 27 && animationTimer <= 70) {
                double d0 = -Mth.sin(player.getYRot() * ((float) Math.PI / 180F));
                double d1 = Mth.cos(player.getYRot() * ((float) Math.PI / 180F));
                if (new Random().nextBoolean()) {
                    CSEffect.createInstance(player, CSEffectTypes.CRESCENTIA_STRIKE, d0, 0, d1);
                } else {
                    CSEffect.createInstance(player, CSEffectTypes.CRESCENTIA_STRIKE_INVERTED, d0, 0, d1);
                }
                CSEffect.createInstance(player, CSEffectTypes.SOLARIS_AIR);
                playRandomBladeSound(player, CRESENTIA_SOUNDS.length);
                double range = 7.0;
                double rangeSq = Mth.square(range);
                List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, range).move(d0, 0, d1));
                for (Entity entityBatch : entities) {
                    if (entityBatch instanceof LivingEntity target) {
                        if (target != player && target.isAlive() && target.distanceToSqr(player) < rangeSq) {
                            double preAttribute = target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getValue();
                            target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(100);
                            target.invulnerableTime = 0;
                            target.hurt(player.damageSources().playerAttack(player), 1.3f + ((float) EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, itemStack) / 2.2F));
                            target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                            target.getAttribute(Attributes.KNOCKBACK_RESISTANCE).setBaseValue(preAttribute);
                        }
                    }
                    if (entityBatch instanceof Projectile projectile) {
                        createCrescentiaFirework(itemStack, level, player, projectile.getX(), projectile.getY(), projectile.getZ(), true, animationTimer);
                        player.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F);
                        projectile.remove(Entity.RemovalReason.DISCARDED);
                    }
                }

                Random random = new Random();
                float offX = random.nextFloat() * 12 - 6;
                float offY = random.nextFloat() * 12 - 6;
                float offZ = random.nextFloat() * 12 - 6;
                createCrescentiaFirework(itemStack, level, player, player.getX() + offX, player.getY() + offY, player.getZ() + offZ, false, animationTimer);
            }
            if (animationTimer >= 80) {
                data.putInt(ANIMATION_TIMER_KEY, 0);
                data.putBoolean(ANIMATION_BEGUN_KEY, false);
            }
        }
    }

    public static void createCrescentiaFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z, boolean isBig, int animationTimer) {
        Random random = new Random();
        if (animationTimer % 2 == 0) {
            ItemStack star = new ItemStack(Items.FIREWORK_STAR);
            CompoundTag compoundtag = star.getOrCreateTagElement("Explosion");
            List<Integer> list = Lists.newArrayList();
            DyeColor[] allowedColors = new DyeColor[]{DyeColor.LIGHT_BLUE, DyeColor.WHITE, DyeColor.BLUE, DyeColor.MAGENTA};
            list.add(allowedColors[random.nextInt(allowedColors.length)].getFireworkColor());
            compoundtag.putIntArray("Colors", list);
            compoundtag.putByte("Type", (byte)(isBig ? FireworkRocketItem.Shape.LARGE_BALL.getId() : FireworkRocketItem.Shape.SMALL_BALL.getId()));
            CompoundTag itemCompound = itemStack.getOrCreateTagElement("Fireworks");
            ListTag listtag = new ListTag();
            CompoundTag starCompound = star.getTagElement("Explosion");
            if (starCompound != null) {
                listtag.add(starCompound);
            }
            itemCompound.putByte("Flight", (byte) 3);
            if (!listtag.isEmpty()) {
                itemCompound.put("Explosions", listtag);
            }
            level.createFireworks(x, y, z, 0.1, 0.1, 0.1, itemCompound);
            player.playSound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 1.0F, 0.5F + random.nextFloat());
        }
    }

    public static void doCrescentiaDamageReduction(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        CompoundTag tagR = entity.getMainHandItem().getItem().getShareTag(entity.getMainHandItem());
        CompoundTag tagL = entity.getOffhandItem().getItem().getShareTag(entity.getOffhandItem());
        if ((tagR != null && (tagR.getBoolean(ANIMATION_BEGUN_KEY)) || (tagL != null && (tagL.getBoolean(ANIMATION_BEGUN_KEY))))) {
            event.setAmount(event.getAmount() * 0.7F);
        }
    }

    public static void playRandomBladeSound(Entity entity, int length) {
        SoundEvent randomSound = CRESENTIA_SOUNDS[new Random().nextInt(length)];
        entity.playSound(randomSound, 0.55F, 0.5F + new Random().nextFloat());
    }
}
