package com.aqutheseal.celestisynth.common.attack.aquaflora;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.api.item.AttackHurtTypes;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.item.weapons.AquafloraItem;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import java.util.List;
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
        return CSConfigManager.COMMON.aquafloraBloomShiftSkillCD.get();
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
        getTagController().putFloat(INITIAL_VIEW_ANGLE, player.getXRot());
    }

    @Override
    public void tickAttack() {
        player.setXRot(90);
        setCameraAngle(player, 1);

        if (getTimerProgress() >= 15 && getTimerProgress() % (checkDualWield(player, AquafloraItem.class) ? 1 : 5) == 0) {
            Predicate<Entity> filter = (e) -> e != player && e instanceof LivingEntity le && (player.hasLineOfSight(le) || le.hasLineOfSight(player)) &&  le.isAlive() && !player.isAlliedTo(le);
            List<LivingEntity> entities = iterateEntities(level, createAABB(player.blockPosition(), 12)).stream().filter(filter).map(LivingEntity.class::cast).toList();
            LivingEntity target = entities.size() > 0 ? entities.get(level.random.nextInt(entities.size())) : null;

            if (target == player || target == null) {
                player.displayClientMessage(Component.translatable("item.celestisynth.aquaflora.skill_3.notice"), true);
                player.playSound(CSSoundEvents.CS_BLING.get(), 0.25F, 1.5F);
                CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_DASH.get(), 0, 0.55, 0);
                baseStop();
                return;
            }

            double offsetX = -4 + level.random.nextInt(8);
            double offsetZ = -4 + level.random.nextInt(8);

            if (level.isClientSide()) {
                double dx = target.getX() - (player.getX() + offsetX);
                double dz = target.getZ() - (player.getZ() + offsetZ);
                double yaw = -Math.atan2(dx, dz);

                yaw = yaw * (180.0 / Math.PI);
                yaw = yaw + (yaw < 0 ? 360 : 0);

                player.setYRot((float) yaw);
            }

            CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_DASH.get(), 0, 0.55, 0);
            BlockPos toPos = target.blockPosition().offset((int) offsetX, 1, (int) offsetZ);
            player.setDeltaMovement((toPos.getX() - player.getX()) * 0.25, (toPos.getY() - player.getY()) * 0.25, (toPos.getZ() - player.getZ()) * 0.25);
            //player.moveTo(target.blockPosition().offset((int) offsetX, 1, (int) offsetZ), player.getYRot(), player.getXRot());
            CSEffectEntity.createInstance(player, target, CSVisualTypes.AQUAFLORA_ASSASSINATE.get(), 0, -0.2, 0);
            player.playSound(CSSoundEvents.CS_WIND_STRIKE.get(), 0.15F, 0.5F);

            double dualWieldMultiplier = checkDualWield(player, AquafloraItem.class) ? 0.52 : 1;

            initiateAbilityAttack(player, target, (float) (CSConfigManager.COMMON.aquafloraBloomSkillDmg.get() * dualWieldMultiplier) + getSharpnessValue(getStack(), (float) (0.65 * dualWieldMultiplier)), AttackHurtTypes.RAPID_PIERCE);
            createAquafloraFirework(getStack(), level, player, target.getX(), target.getY() + 1, target.getZ());
        }
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(ATTACK_ONGOING, false);
        player.setXRot( getTagController().getFloat(INITIAL_VIEW_ANGLE));
        setCameraAngle(player, getTagController().getInt(INITIAL_PERSPECTIVE));
    }

    public static void createAquafloraFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z) {
        ItemStack fireworkStarStack = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag starExplosionDataTag = fireworkStarStack.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        DyeColor[] allowedColors = new DyeColor[]{DyeColor.PINK, DyeColor.MAGENTA, DyeColor.WHITE};

        list.add(allowedColors[level.random.nextInt(allowedColors.length)].getFireworkColor());
        starExplosionDataTag.putIntArray("Colors", list);
        starExplosionDataTag.putByte("Type", (byte)(FireworkRocketItem.Shape.SMALL_BALL.getId()));

        CompoundTag fireworkDataTag = itemStack.getOrCreateTagElement("Fireworks");
        ListTag starDataListTag = new ListTag();
        CompoundTag explosionDataTag = fireworkStarStack.getTagElement("Explosion");

        if (explosionDataTag != null) starDataListTag.add(explosionDataTag);

        fireworkDataTag.putByte("Flight", (byte) 3);

        if (!starDataListTag.isEmpty()) fireworkDataTag.put("Explosions", starDataListTag);

        level.createFireworks(x, y, z, 0.01, 0.01, 0.01, fireworkDataTag);
    }
}
