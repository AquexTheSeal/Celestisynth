package com.aqutheseal.celestisynth.item;

import com.aqutheseal.celestisynth.item.helpers.CSWeapon;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class BreezebreakerItem extends SwordItem implements CSWeapon {

    public BreezebreakerItem(Tier tier, int attackDamage, float attackSpeed, Properties properties) {
        super(tier, attackDamage, attackSpeed, properties);
    }


}
