package com.aqutheseal.celestisynth.common.attack.aquaflora;

import com.aqutheseal.celestisynth.api.animation.player.AnimationManager;
import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.item.weapons.AquafloraItem;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.aqutheseal.celestisynth.manager.CSConfigManager;
import com.google.common.collect.Lists;
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
        getTagController().putFloat(INITIAL_VIEW_ANGLE, getPlayer().getXRot());
    }

    @Override
    public void tickAttack() {
        getPlayer().setXRot(90);
        setCameraAngle(player, 1);

        if (getTimerProgress() >= 15 && getTimerProgress() % (checkDualWield(player, AquafloraItem.class) ? 2 : 5) == 0) {
            Predicate<Entity> filter = (e) -> e != player && e instanceof LivingEntity le && (player.hasLineOfSight(le) || le.hasLineOfSight(player)) &&  le.isAlive() && !player.isAlliedTo(le);
            List<LivingEntity> entities = iterateEntities(getPlayer().level(), createAABB(player.blockPosition(), 12)).stream().filter(filter).map(LivingEntity.class::cast).toList();
            LivingEntity target = entities.size() > 0 ? entities.get(getPlayer().level().random.nextInt(entities.size())) : null;

            if (target == player || target == null) {
                player.displayClientMessage(Component.translatable("item.celestisynth.aquaflora.skill_3.notice"), true);
                getPlayer().playSound(CSSoundEvents.CS_BLING.get(), 0.25F, 1.5F);
                CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_DASH.get(), 0, 0.55, 0);
                baseStop();
                return;
            }

            double offsetX = -4 + getPlayer().level().random.nextInt(8);
            double offsetZ = -4 + getPlayer().level().random.nextInt(8);

            if (getPlayer().level().isClientSide()) {
                double dx = target.getX() - (player.getX() + offsetX);
                double dz = target.getZ() - (player.getZ() + offsetZ);
                double yaw = -Math.atan2(dx, dz);

                yaw = yaw * (180.0 / Math.PI);
                yaw = yaw + (yaw < 0 ? 360 : 0);

                getPlayer().setYRot((float) yaw);
            }

            CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_DASH.get(), 0, 0.55, 0);
            getPlayer().moveTo(target.blockPosition().offset((int) offsetX, 1, (int) offsetZ), getPlayer().getYRot(), getPlayer().getXRot());
            CSEffectEntity.createInstance(player, target, CSVisualTypes.AQUAFLORA_ASSASSINATE.get(), 0, -0.2, 0);
            getPlayer().playSound(CSSoundEvents.CS_BLING.get(), 0.15F, 0.5F);

            double dualWieldMultiplier = checkDualWield(player, AquafloraItem.class) ? 0.52 : 1;

            hurtNoKB(player, target, (float) (CSConfigManager.COMMON.aquafloraBloomSkillDmg.get() * dualWieldMultiplier) + getSharpnessValue(getStack(), (float) (0.65 * dualWieldMultiplier)));
            createAquafloraFirework(getStack(), getPlayer().level(), player, target.getX(), target.getY() + 1, target.getZ());
        }
    }

    @Override
    public void stopUsing() {
        getTagController().putBoolean(ATTACK_ONGOING, false);
        getPlayer().setXRot( getTagController().getFloat(INITIAL_VIEW_ANGLE));
        setCameraAngle(player, getTagController().getInt(INITIAL_PERSPECTIVE));
    }

    public static void createAquafloraFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z) {
        ItemStack fireworkStarStack = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag starExplosionDataTag = fireworkStarStack.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        DyeColor[] allowedColors = new DyeColor[]{DyeColor.PINK, DyeColor.GREEN, DyeColor.WHITE};

        list.add(allowedColors[level.random.nextInt(allowedColors.length)].getFireworkColor());
        starExplosionDataTag.putIntArray("Colors", list);
        starExplosionDataTag.putByte("Type", (byte)(FireworkRocketItem.Shape.SMALL_BALL.getId()));

        CompoundTag fireworkDataTag = itemStack.getOrCreateTagElement("Fireworks");
        ListTag starDataListTag = new ListTag();
        CompoundTag explosionDataTag = fireworkStarStack.getTagElement("Explosion");

        if (explosionDataTag != null) starDataListTag.add(explosionDataTag);

        fireworkDataTag.putByte("Flight", (byte) 3);

        if (!starDataListTag.isEmpty()) fireworkDataTag.put("Explosions", starDataListTag);

        player.level().createFireworks(x, y, z, 0.01, 0.01, 0.01, fireworkDataTag);
    }
}
