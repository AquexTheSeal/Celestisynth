package com.aqutheseal.celestisynth.common.entity.skill;

import com.aqutheseal.celestisynth.common.entity.base.CSEffectEntity;
import com.aqutheseal.celestisynth.common.entity.base.EffectControllerEntity;
import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.aqutheseal.celestisynth.common.registry.CSSoundEvents;
import com.aqutheseal.celestisynth.common.registry.CSVisualTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class SkillCastFrostboundIceCast extends EffectControllerEntity {
    public SkillCastFrostboundIceCast(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public Item getCorrespondingItem() {
        return CSItems.FROSTBOUND.get();
    }

    @Override
    public void tick() {
        UUID ownerUuid = getOwnerUuid();
        Player ownerPlayer = ownerUuid == null ? null : this.level().getPlayerByUUID(ownerUuid);

        if (tickCount == 1) {
            this.playSound(CSSoundEvents.ICE_CAST.get());
            CSEffectEntity.createInstance(ownerPlayer, this, CSVisualTypes.FROSTBOUND_ICE_CAST.get(), 0, 0.25, 0);
        }
    }
}
