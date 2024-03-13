package com.aqutheseal.celestisynth.common.item.weapons;

import com.aqutheseal.celestisynth.api.item.CSGeoItem;
import com.aqutheseal.celestisynth.common.attack.aquaflora.*;
import com.aqutheseal.celestisynth.common.attack.base.WeaponAttackInstance;
import com.aqutheseal.celestisynth.common.compat.CSCompatManager;
import com.aqutheseal.celestisynth.api.entity.CSEffectEntity;
import com.aqutheseal.celestisynth.common.item.base.SkilledSwordItem;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

import java.util.UUID;

public class AquafloraItem extends SkilledSwordItem implements CSGeoItem {
    public AquafloraItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public String geoIdentifier() {
        return "aquaflora";
    }

    @Override
    public String model(ItemStack stack) {
        if (attackController(stack) != null && attackController(stack).getBoolean(AquafloraAttack.CHECK_PASSIVE)) {
            return "aquaflora_blooming";
        } else {
            return "aquaflora";
        }
    }

    @Override
    public String texture(ItemStack stack) {
        return model(stack);
    }

    @Override
    public GeoAnimatable cacheItem() {
        return this;
    }

    @Override
    public ImmutableList<WeaponAttackInstance> getPossibleAttacks(Player player, ItemStack stack, int useDuration) {
        return ImmutableList.of(
                new AquafloraPetalPiercesAttack(player, stack, useDuration),
                new AquafloraBlastOffAttack(player, stack, useDuration),
                new AquafloraSlashFrenzyAttack(player, stack, useDuration),
                new AquafloraFlowersAwayAttack(player, stack, useDuration)
        );
    }

    @Override
    public void addExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> map) {
        if (CSCompatManager.checkIronsSpellbooks()) {
            map.put(AttributeRegistry.NATURE_SPELL_POWER.get(), new AttributeModifier(UUID.randomUUID(), "Item nature spell power", 0.1, AttributeModifier.Operation.MULTIPLY_BASE));
            map.put(AttributeRegistry.NATURE_MAGIC_RESIST.get(), new AttributeModifier(UUID.randomUUID(), "Item nature resist", 0.2, AttributeModifier.Operation.MULTIPLY_BASE));
        }
    }

    @Override
    public int getPassiveAmount() {
        return 1;
    }

    @Override
    public int getSkillsAmount() {
        return 4;
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, LivingEntity pTarget, LivingEntity pAttacker) {
        if (pAttacker instanceof Player player) {
            if (player.getMainHandItem().is(this) && player.getOffhandItem().is(this)) {
                if (player.getMainHandItem() == pStack) {
                    CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_SLICE.get(), 0, 1.75, 0);
                }
                if (player.getOffhandItem() == pStack) {
                    CSEffectEntity.createInstance(player, null, CSVisualTypes.AQUAFLORA_SLICE_INVERTED.get(), 0, 1.75, 0);
                }
            }
        }
        return super.hurtEnemy(pStack, pTarget, pAttacker);
    }

    @Override
    public void onPlayerHurt(LivingHurtEvent event, ItemStack stack) {
        if (attackController(stack).getBoolean(AquafloraSlashFrenzyAttack.ATTACK_ONGOING)) {
            event.setAmount(event.getAmount() * 0.25F);
        }
    }
}
