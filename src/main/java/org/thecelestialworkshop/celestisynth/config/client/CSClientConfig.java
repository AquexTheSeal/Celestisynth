package org.thecelestialworkshop.celestisynth.config.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class CSClientConfig {
    public final ForgeConfigSpec.ConfigValue<Boolean> visibilityOnFirstPerson;
    public final ForgeConfigSpec.ConfigValue<Boolean> showLeftArmOnAnimate;
    public final ForgeConfigSpec.ConfigValue<Boolean> showRightArmOnAnimate;
    public final ForgeConfigSpec.ConfigValue<Boolean> showChantMessages;

    public CSClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("Client-side Configurations");
        visibilityOnFirstPerson = builder.comment("Should the weapon attack effects be visible on first person mode?").define("Is Visible?", true);
        showLeftArmOnAnimate = builder.comment("Defines if your left arm must be shown during the ability casting process.").define("Show Left Arm", false);
        showRightArmOnAnimate = builder.comment("Defines if your right arm must be shown during the ability casting process.").define("Show Right Arm", false);
        showChantMessages = builder.comment("Should chant messages be displayed?").define("Enable Chant Messages", true);
        builder.pop();
    }
}
