package org.thecelestialworkshop.celestisynth.common.registry;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.api.mixin.PlayerMixinSupport;

public class CSGuiOverlays {
    public static final ObjectArrayList<String> WHITELISTED_OVERLAY_FRAMES = Util.make(new ObjectArrayList<>(), whitelist -> {
       whitelist.add("keres_carnage_0");
       whitelist.add("keres_carnage_1");
       whitelist.add("keres_carnage_2");
    });

    public static final IGuiOverlay KERES_CARNAGE_INCARNATE_OVERLAY = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
        if (Minecraft.getInstance().player instanceof PlayerMixinSupport mixinPlayer && !Minecraft.getInstance().isPaused()) {
            if (mixinPlayer.getTexturePulseMark() < 20 && !mixinPlayer.getTexturePulseImage().isEmpty()) {
                gui.setupOverlayRenderState(true, false);

                float lerp = Mth.lerp(mixinPlayer.getTexturePulseMark() / 20F, 0.5F, 0);
                ResourceLocation location = Celestisynth.prefix("textures/misc/" + mixinPlayer.getTexturePulseImage() + ".png");

                if (WHITELISTED_OVERLAY_FRAMES.contains(mixinPlayer.getTexturePulseImage())) {
                    renderStandardTextureOverlay(guiGraphics, new ResourceLocation("textures/misc/vignette.png"),  0.0F, 0.0F, 0.0F, lerp * 0.5F, screenWidth, screenHeight);
                }

                renderStandardTextureOverlay(guiGraphics, location, 1.0F, 0.0F, 0.1F, lerp, screenWidth, screenHeight);
            }
        }
    };

    public static void renderStandardTextureOverlay(GuiGraphics guiGraphics, ResourceLocation texLoc, float r, float g, float b, float texAlpha, int screenWidth, int screenHeight) {
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);

        guiGraphics.setColor(r, g, b, texAlpha);
        guiGraphics.blit(texLoc, 0, 0, -90, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }
}
