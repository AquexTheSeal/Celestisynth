package com.aqutheseal.celestisynth.item;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.CrescentiaRanged;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import com.aqutheseal.celestisynth.registry.CSEntityRegistry;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class CrescentiaItem extends SwordItem implements CSWeapon {

    public static final String IS_RANGED_KEY = "cs.isRangedAttack";

    public CrescentiaItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }

    @Override
    public int getSkillsAmount() {
        return 2;
    }

    @Override
    public boolean hasPassive() {
        return true;
    }

    @Override
    public int getPassiveAmount() {
        return 1;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand interactionHand) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        CompoundTag itemTag = itemstack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (!player.getCooldowns().isOnCooldown(itemstack.getItem()) && !itemTag.getBoolean(ANIMATION_BEGUN_KEY)) {
            itemTag.putBoolean(ANIMATION_BEGUN_KEY, true);
            itemTag.putBoolean(IS_RANGED_KEY, player.isShiftKeyDown());
            if (level.isClientSide()) {
                if (itemTag.getBoolean(IS_RANGED_KEY)) {
                    AnimationManager.playAnimation(AnimationManager.AnimationsList.ANIM_CRESCENTIA_THROW);
                } else {
                    AnimationManager.playAnimation(AnimationManager.AnimationsList.ANIM_CRESCENTIA_STRIKE);
                }
            }
            player.getCooldowns().addCooldown(itemstack.getItem(), itemTag.getBoolean(IS_RANGED_KEY) ? 40 : 100);
        }
        return InteractionResultHolder.success(itemstack);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));
        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof CrescentiaItem)) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2, 0));
        }

        if (data.getBoolean(ANIMATION_BEGUN_KEY)) {
            if (entity instanceof Player player) {
                if (player.getMainHandItem() != itemStack) {
                    player.getInventory().selected = itemSlot;
                }
                if (level.isClientSide()) {
                    if (Minecraft.getInstance().screen != null) {
                        Minecraft.getInstance().screen = null;
                    }
                }
                int animationTimer = data.getInt(ANIMATION_TIMER_KEY);
                data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
                if (!data.getBoolean(IS_RANGED_KEY)) {
                    tickMelee(itemStack, level, player, animationTimer);
                } else {
                    tickRanged(itemStack, level, player, animationTimer);
                }
            }
        }
    }

    public void tickRanged(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement(CS_CONTROLLER_TAG_ELEMENT);

        if (animationTimer <= 20) {
            setDeltaPlayer(player, 0, 0, 0);
        }
        if (animationTimer == 20) {
            if (!level.isClientSide()) {
                CrescentiaRanged projectile = CSEntityRegistry.CRESCENTIA_RANGED.get().create(level);
                projectile.setOwnerUuid(player.getUUID());
                projectile.setAngleX((float) calculateXLook(player));
                projectile.setAngleY((float) calculateYLook(player));
                projectile.setAngleZ((float) calculateZLook(player));
                projectile.setAddAngleX((float) calculateXLook(player) / 2);
                projectile.setAddAngleY((float) calculateYLook(player) / 2);
                projectile.setAddAngleZ((float) calculateZLook(player) / 2);
                projectile.moveTo(player.getX(), player.getY() + 1, player.getZ());
                level.addFreshEntity(projectile);
            }

            for (int i = 0; i < 10; i++) {
                Random random = new Random();
                float offX = random.nextFloat() * 12 - 6;
                float offY = random.nextFloat() * 12 - 6;
                float offZ = random.nextFloat() * 12 - 6;
                createCrescentiaFirework(itemStack, level, player, player.getX() + offX, player.getY() + offY, player.getZ() + offZ, true, animationTimer);
                player.playSound(SoundEvents.GENERIC_EXPLODE, 1.0F, 1.5F);
            }
        }
        if (animationTimer >= 30) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
        }
    }

    public void tickMelee(ItemStack itemStack, Level level, Player player, int animationTimer) {
        CompoundTag data = itemStack.getOrCreateTagElement("csController");

        data.putInt(ANIMATION_TIMER_KEY, animationTimer + 1);
        if (animationTimer >= 15 && animationTimer <= 60) {
            double range = 7.0;
            double rangeSq = Mth.square(range);
            List<Entity> entities = level.getEntitiesOfClass(Entity.class, player.getBoundingBox().inflate(range, range, range).move(calculateXLook(player), 0, calculateZLook(player)));
            for (Entity entityBatch : entities) {
                if (entityBatch instanceof LivingEntity target) {
                    if (target != player && target.isAlive() && !player.isAlliedTo(target) && target.distanceToSqr(player) < rangeSq) {
                        constantAttack(player, target, CSConfig.COMMON.crescentiaSkillDmg.get() + ((float) EnchantmentHelper.getTagEnchantmentLevel(Enchantments.SHARPNESS, itemStack) / 2.2F));
                        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 2));
                    }
                }
                if (entityBatch instanceof Projectile projectile) {
                    createCrescentiaFirework(itemStack, level, player, projectile.getX(), projectile.getY(), projectile.getZ(), true, animationTimer);
                    player.playSound(SoundEvents.FIREWORK_ROCKET_LAUNCH, 1.0F, 1.0F);
                    projectile.remove(Entity.RemovalReason.DISCARDED);
                }
            }

            if (new Random().nextBoolean()) {
                CSEffect.createInstance(player, null, CSEffectTypes.CRESCENTIA_STRIKE, calculateXLook(player), 0, calculateZLook(player));
            } else {
                CSEffect.createInstance(player, null, CSEffectTypes.CRESCENTIA_STRIKE_INVERTED, calculateXLook(player), 0, calculateZLook(player));
            }
            CSEffect.createInstance(player, null, CSEffectTypes.SOLARIS_AIR);
            playRandomBladeSound(player, BASE_WEAPON_EFFECTS.length);

            Random random = new Random();
            float offX = random.nextFloat() * 12 - 6;
            float offY = random.nextFloat() * 12 - 6;
            float offZ = random.nextFloat() * 12 - 6;
            createCrescentiaFirework(itemStack, level, player, player.getX() + offX, player.getY() + offY, player.getZ() + offZ, false, animationTimer);
        }
        if (animationTimer >= 70) {
            data.putInt(ANIMATION_TIMER_KEY, 0);
            data.putBoolean(ANIMATION_BEGUN_KEY, false);
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

    public void onPlayerHurt(LivingHurtEvent event, ItemStack mainHandItem, ItemStack offHandItem) {
        LivingEntity entity = event.getEntity();
        CompoundTag tagR = mainHandItem.getItem().getShareTag(entity.getMainHandItem());
        CompoundTag tagL = offHandItem.getItem().getShareTag(entity.getOffhandItem());
        if ((tagR != null && (tagR.getBoolean(ANIMATION_BEGUN_KEY)) || (tagL != null && (tagL.getBoolean(ANIMATION_BEGUN_KEY))))) {
            event.setAmount(event.getAmount() * 0.7F);
        }
    }
}
