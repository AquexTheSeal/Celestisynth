package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.api.item.CSWeaponUtil;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.attack.cresentia.CrescentiaBarrageAttack;
import com.aqutheseal.celestisynth.common.attack.cresentia.CrescentiaDragonAttack;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.List;
import java.util.Random;

public class CrescentiaItem extends SkilledSwordItem implements CSGeoItem {

    public CrescentiaItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "crescentia";
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int dur) {
        return ImmutableList.of(
                new CrescentiaBarrageAttack(player, stack),
                new CrescentiaDragonAttack(player, stack)
        );
    }

    @Override
    public int getSkillsAmount() {
        return 2;
    }

    @Override
    public int getPassiveAmount() {
        return 1;
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity entity, LivingEntity source) {
        entity.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.MOVEMENT_SLOWDOWN, 40, 2));

        return super.hurtEnemy(itemStack, entity, source);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, @NotNull Level level, @NotNull Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof Player player && (isSelected || player.getOffhandItem().getItem() instanceof CrescentiaItem)) player.addEffect(CSWeaponUtil.nonVisiblePotionEffect(MobEffects.DAMAGE_RESISTANCE, 2, 0));
        super.inventoryTick(itemStack, level, entity, itemSlot, isSelected);
    }

    public static void createCrescentiaFirework(ItemStack itemStack, Level level, Player player, double x, double y, double z, boolean isBig) {
        Random random = new Random();
        ItemStack star = new ItemStack(Items.FIREWORK_STAR);
        CompoundTag compoundtag = star.getOrCreateTagElement("Explosion");
        List<Integer> list = Lists.newArrayList();
        DyeColor[] allowedColors = new DyeColor[]{DyeColor.LIGHT_BLUE, DyeColor.WHITE, DyeColor.BLUE, DyeColor.MAGENTA, DyeColor.YELLOW, DyeColor.ORANGE};
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
        level.createFireworks(x, y, z, 0.01, 0.01, 0.01, itemCompound);
        player.playSound(SoundEvents.FIREWORK_ROCKET_LARGE_BLAST, 1.0F, 0.5F + random.nextFloat());
    }
}
