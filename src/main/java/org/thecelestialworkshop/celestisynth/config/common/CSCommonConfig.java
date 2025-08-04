package org.thecelestialworkshop.celestisynth.config.common;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.StringUtils;

public class CSCommonConfig {
    public final ForgeConfigSpec.BooleanValue keresCarnageIncarnateGriefing;

    public CSCommonConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Weapon Config");

        builder.push("Keres");

        this.keresCarnageIncarnateGriefing = builder
                .comment("Should Keres' Carnage Incarnate skill be able to grief blocks?")
                .define("Carnage Incarnate Griefing", true);

        builder.pop();

        builder.pop();
    }

    public ForgeConfigSpec.DoubleValue skillDamage(ForgeConfigSpec.Builder builder, String weapon, String skillName, Double dmg) {
        return builder.comment("Define how much damage does the " + StringUtils.capitalize(weapon) + " deal in a specified attack skill.").defineInRange("Damage: " + skillName, dmg, 0, 1000);
    }

    public ForgeConfigSpec.IntValue skillCooldown(ForgeConfigSpec.Builder builder, String weapon, String skillName, int cooldown) {
        return builder.comment("Define the duration of the cooldown provided by the " + StringUtils.capitalize(weapon) + " in a particular attack skill, measured in ticks.").defineInRange("Cooldown: " + skillName, cooldown, 0, 1000);
    }

    public ForgeConfigSpec.IntValue baseDamage(ForgeConfigSpec.Builder builder, String weapon, int dmg) {
        return builder.comment("Define the base attack damage of the " + StringUtils.capitalize(weapon) + ".").defineInRange("Base Damage: " + StringUtils.capitalize(weapon), dmg, 0, 1000);
    }
}
