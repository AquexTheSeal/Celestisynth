package com.aqutheseal.celestisynth.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber
public class CSConfig {

    public static final ForgeConfigSpec COMMON_SPEC;
    public static final CSCommonConfig COMMON;
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final CSClientConfig CLIENT;

    static {
        final Pair<CSCommonConfig, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(CSCommonConfig::new);
        COMMON_SPEC = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();

        final Pair<CSClientConfig, ForgeConfigSpec> clientSpecPair = new ForgeConfigSpec.Builder().configure(CSClientConfig::new);
        CLIENT_SPEC = clientSpecPair.getRight();
        CLIENT = clientSpecPair.getLeft();
    }

    public static class CSCommonConfig {
        public final ForgeConfigSpec.ConfigValue<Integer> solarisDmg;
        public final ForgeConfigSpec.ConfigValue<Integer> crescentiaDmg;

        public final ForgeConfigSpec.ConfigValue<Float> solarisSkillDmg;
        public final ForgeConfigSpec.ConfigValue<Float> solarisShiftSkillDmg;
        public final ForgeConfigSpec.ConfigValue<Float> crescentiaSkillDmg;
        public final ForgeConfigSpec.ConfigValue<Float> crescentiaShiftSkillDmg;

        protected CSCommonConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Base Damage Modifications (Temporarily Unusable)");
            solarisDmg = baseDamage(builder, "solaris", 7);
            crescentiaDmg = baseDamage(builder, "crescentia", 8);
            builder.pop();

            builder.push("Skill Damage Modifications");
            solarisSkillDmg = skillDamage(builder, "solaris", "Spinning Flames - Full Round", 1.0f);
            solarisShiftSkillDmg = skillDamage(builder, "solaris", "Spinning Flames - Soul Straight Dash [Shift]", 1.5f);
            crescentiaSkillDmg = skillDamage(builder, "crescentia", "Lunar Celebration Barrage", 1.3f);
            crescentiaShiftSkillDmg = skillDamage(builder, "crescentia", "Dragon Crescent Boom [Shift]", 0.7f);
            builder.pop();
        }

        public ForgeConfigSpec.ConfigValue<Float> skillDamage(ForgeConfigSpec.Builder builder, String weapon, String skillName, float dmg) {
            return builder.comment("Define how much damage does the " + StringUtils.capitalize(weapon) + " deal in a specified attack skill.").define(skillName, dmg);
        }

        public ForgeConfigSpec.ConfigValue<Integer> baseDamage(ForgeConfigSpec.Builder builder, String weapon, int dmg) {
            return  builder.comment("Define the base attack damage of the " + StringUtils.capitalize(weapon) + ".").define("Base Damage", dmg);
        }
    }

    public static class CSClientConfig {
        public final ForgeConfigSpec.ConfigValue<Boolean> visibilityOnFirstPerson;

        protected CSClientConfig(ForgeConfigSpec.Builder builder) {
            builder.push("Client-side Configurations");
            visibilityOnFirstPerson = builder.comment("Should the weapon attack effects be visible on first person mode?").define("Is Visible?", true);
            builder.pop();
        }
    }
}
