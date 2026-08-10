package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.util.ExtraUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Rarity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.asm.enumextension.EnumProxy;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.function.UnaryOperator;

/**
 * Custom rarities are injected through NeoForge's enum extension system
 * (META-INF/enumextensions.json references the two EnumProxy fields below).
 * The values MUST be read lazily: eager static fields could run before the
 * extended enum constants exist (this class can be class-loaded from within
 * Rarity's own static initializer while the extension is being applied).
 */
public class CSRarityTypes {
    public static final EnumProxy<Rarity> CELESTIAL_ENUM_PARAMS = new EnumProxy<>(Rarity.class,
            -1, "celestisynth:celestial", (UnaryOperator<Style>) CSRarityTypes::celestialStyle);

    public static final EnumProxy<Rarity> ETHEREAL_ENUM_PARAMS = new EnumProxy<>(Rarity.class,
            -1, "celestisynth:ethereal", (UnaryOperator<Style>) style -> style.withColor(0xb3f7ff).withBold(true).withUnderlined(true));

    public static Rarity celestial() {
        Rarity.values();
        return CELESTIAL_ENUM_PARAMS.getValue();
    }

    public static Rarity ethereal() {
        Rarity.values();
        return ETHEREAL_ENUM_PARAMS.getValue();
    }

    private static Style celestialStyle(Style style) {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            return ClientStyleHolder.animatedCelestialStyle(style);
        }
        return style.withColor(ChatFormatting.GOLD);
    }

    private static class ClientStyleHolder {
        private static Style animatedCelestialStyle(Style style) {
            var player = Minecraft.getInstance().player;
            if (player != null) {
                return ExtraUtil.getCelestialStyle(style, player.tickCount);
            }
            return style.withColor(ChatFormatting.GOLD);
        }
    }
}
