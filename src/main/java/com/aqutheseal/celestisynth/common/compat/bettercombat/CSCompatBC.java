package com.aqutheseal.celestisynth.common.compat.bettercombat;

import com.aqutheseal.celestisynth.common.registry.CSItems;
import com.mojang.datafixers.util.Pair;
import net.bettercombat.BetterCombat;
import net.bettercombat.api.WeaponAttributes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;

public class CSCompatBC {

    public static List<Pair<Item, WeaponAttributes>> specialProperties() {
        List<Pair<Item, WeaponAttributes>> specialPropertyList = new ArrayList<>();

        specialPropertyList.add(Pair.of(CSItems.FROSTBOUND.get(),
                new WeaponAttributes(
                        5, betterCombatID("pose_two_handed_scythe"), null, true, "scythe",
                        new WeaponAttributes.Attack[]{
                                new WeaponAttributes.Attack(new WeaponAttributes.Condition[]{}, WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 150, 0.7, betterCombatID("two_handed_slash_horizontal_right"),
                                        sound(betterCombatID("scythe_slash")), sound(SoundEvents.GLASS_BREAK)
                                ),
                                new WeaponAttributes.Attack(new WeaponAttributes.Condition[]{}, WeaponAttributes.HitBoxShape.HORIZONTAL_PLANE,
                                        1.0, 150, 0.7, betterCombatID("two_handed_slash_horizontal_left"),
                                        sound(betterCombatID("scythe_slash")), sound(SoundEvents.GLASS_BREAK)
                                )
                        }
                )
        ));

        return specialPropertyList;
    }
    private static WeaponAttributes.Sound sound(String id) {
        return new WeaponAttributes.Sound(id);
    }

    private static WeaponAttributes.Sound sound(SoundEvent soundEvent) {
        return sound(soundEvent.getLocation().toString());
    }

    private static String betterCombatID(String id) {
        return BetterCombat.MODID + ":" + id;
    }

}
