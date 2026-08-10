package org.thecelestialworkshop.celestisynth.config.common;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.StringUtils;

public class CSCommonConfig {
    public final ModConfigSpec.BooleanValue keresCarnageIncarnateGriefing;

    public CSCommonConfig(ModConfigSpec.Builder builder) {
        builder.push("Weapon Config");

        builder.push("Keres");

        this.keresCarnageIncarnateGriefing = builder
                .comment("Should Keres' Carnage Incarnate skill be able to grief blocks?")
                .define("Carnage Incarnate Griefing", true);

        builder.pop();

        builder.pop();
    }

    public ModConfigSpec.DoubleValue skillDamage(ModConfigSpec.Builder builder, String weapon, String skillName, Double dmg) {
        return builder.comment("Define how much damage does the " + StringUtils.capitalize(weapon) + " deal in a specified attack skill.").defineInRange("Damage: " + skillName, dmg, 0, 1000);
    }

    public ModConfigSpec.IntValue skillCooldown(ModConfigSpec.Builder builder, String weapon, String skillName, int cooldown) {
        return builder.comment("Define the duration of the cooldown provided by the " + StringUtils.capitalize(weapon) + " in a particular attack skill, measured in ticks.").defineInRange("Cooldown: " + skillName, cooldown, 0, 1000);
    }

    public ModConfigSpec.IntValue baseDamage(ModConfigSpec.Builder builder, String weapon, int dmg) {
        return builder.comment("Define the base attack damage of the " + StringUtils.capitalize(weapon) + ".").defineInRange("Base Damage: " + StringUtils.capitalize(weapon), dmg, 0, 1000);
    }
}
