package org.thecelestialworkshop.celestisynth.util;

import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import software.bernie.geckolib.core.object.Color;

public class ExtraUtil {
    public static Style getCelestialStyle(Style originalStype, int tickCount) {
        return originalStype.withColor(ExtraUtil.getCelestialColor(tickCount).argbInt()).withUnderlined(true);
    }

    public static Color getCelestialColor(int tickCount) {
        float rr = Mth.sin(tickCount * 0.3F) * 0.25F;
        float gg = Mth.sin(tickCount * 0.15F) * 0.25F;
        float bb = Mth.cos(tickCount * 0.25F) * 0.25F;
        return Color.ofRGBA(0.75F + rr, 1, 0.75F + bb, 1);
    }
}
