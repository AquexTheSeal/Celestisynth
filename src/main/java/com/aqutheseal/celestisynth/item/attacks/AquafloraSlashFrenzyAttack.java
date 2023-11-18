package com.aqutheseal.celestisynth.item.attacks;

import com.aqutheseal.celestisynth.PlayerMixinSupport;
import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.config.CSConfig;
import com.aqutheseal.celestisynth.entities.CSEffect;
import com.aqutheseal.celestisynth.entities.helper.CSEffectTypes;
import com.aqutheseal.celestisynth.item.weapons.AquafloraItem;
import com.aqutheseal.celestisynth.registry.CSSoundRegistry;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class AquafloraSlashFrenzyAttack extends AquafloraAttack {
    public static final String ATTACK_ONGOING = "cs.atkOngoing";
    public static final String INITIAL_PERSPECTIVE = "cs.initPerspective";
    public static final String INITIAL_VIEW_ANGLE = "cs.initViewAngle";
    
    public AquafloraSlashFrenzyAttack(Player player, ItemStack stack, int heldDuration) {
        super(player, stack, heldDuration);
    }

    @Override
    public AnimationManager.AnimationsList getAnimation() {
        return AnimationManager.AnimationsList.ANIM_AQUAFLORA_ASSASSINATE;
    }

    @Override
    public int getCooldown() {
        return CSConfig.COMMON.aquafloraBloomShiftSkillCD.get();
    }

    @Override
    public int getAttackStopTime() {
        return 120;
    }

    @Override
    public boolean getCondition() {
        return getTagController().getBoolean(CHECK_PASSIVE) && !player.isCrouching();
    }

    @Override
    public void startUsing() {
        getTagController().putBoolean(ATTACK_ONGOING, true);
        if (player instanceof PlayerMixinSupport pms) {
            if (player.level.isClientSide()) {
                pms.setCameraAngleOrdinal(Minecraft.getInstance().options.getCameraType().ordinal());
            }
        }
        getTagController().putFloat(INITIAL_VIEW_ANGLE, player.getXRot());
    }

    @Override
    public void tickAttack() {
        player.setXRot(90);
        setCameraAngle(player, 1);

        if (getTimerProgress() >= 15 && getTimerProgress() % (checkDualWield(player, AquafloraItem.class) ? 2 : 5) == 0) {
            Predicate<Entity> filter = (e) -> e != player && e instanceof LivingEntity le && (player.hasLineOfSight(le) || le.hasLineOfSight(player)) &&  le.isAlive() && !player.isAlliedTo(le);
            List<LivingEntity> entities = iterateEntities(getPlayer().level, createAABB(player.blockPosition(), 12)).stream().filter(filter).map(LivingEntity.class::cast).toList();
            LivingEntity target = entities.size() > 0 ? entities.get(getPlayer().level.random.nextInt(entities.size())) : null;
            if (target == player || target == null) {
                AnimationManager.playAnimation(getPlayer().level, AnimationManager.AnimationsList.CLEAR);
                getTagController().putInt(ANIMATION_TIMER_KEY, 0);
                getTagController().putBoolean(ANIMATION_BEGUN_KEY, false);
                player.setXRot( getTagController().getFloat(INITIAL_VIEW_ANGLE));
                setCameraAngle(player,  getTagController().getInt(INITIAL_PERSPECTIVE));
                return;
            }

            double oX = -4 + getPlayer().level.random.nextInt(8);
            double oZ = -4 + getPlayer().level.random.nextInt(8);

            if (getPlayer().level.isClientSide()) {
                double dx = target.getX() - (player.getX() + oX);
                double dz = target.getZ() - (player.getZ() + oZ);
                double yaw = -Math.atan2(dx, dz);
                yaw = yaw * (180.0 / Math.PI);
                yaw = yaw + (yaw < 0 ? 360 : 0);
                player.setYRot((float) yaw);
            }

            CSEffect.createInstance(player, null, CSEffectTypes.AQUAFLORA_DASH, 0, 0.55, 0);
            player.moveTo(target.blockPosition().offset(oX, 1, oZ), player.getYRot(), player.getXRot());
            CSEffect.createInstance(player, target, CSEffectTypes.AQUAFLORA_ASSASSINATE, 0, -0.2, 0);
            player.playSound(CSSoundRegistry.CS_BLING.get(), 0.15F, 0.5F);
            double dualWieldMultiplier = checkDualWield(player, AquafloraItem.class) ? 0.52 : 1;
            hurtNoKB(player, target, (float) (CSConfig.COMMON.aquafloraBloomSkillDmg.get() * dualWieldMultiplier) + getSharpnessValue(getStack(), (float) (0.65 * dualWieldMultiplier)));
            createAquafloraFirework(getStack(), getPlayer().level, player, target.getX(), target.getY() + 1, target.getZ());
        }
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(ATTACK_ONGOING, false);
        player.setXRot( getTagController().getFloat(INITIAL_VIEW_ANGLE));
        setCameraAngle(player, getTagController().getInt(INITIAL_PERSPECTIVE));
    }

    public static void createAquafloraFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z) {
        Random random = new Random();
        ItemStack star = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag compoundtag = star.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        DyeColor[] allowedColors = new DyeColor[]{DyeColor.PINK, DyeColor.GREEN, DyeColor.WHITE};
        list.add(allowedColors[random.nextInt(allowedColors.length)].getFireworkColor());
        compoundtag.putIntArray("Colors", list);
        compoundtag.putByte("Type", (byte)(FireworkRocketItem.Shape.SMALL_BALL.getId()));
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
        player.level.createFireworks(x, y, z, 0.01, 0.01, 0.01, itemCompound);
    }
}
