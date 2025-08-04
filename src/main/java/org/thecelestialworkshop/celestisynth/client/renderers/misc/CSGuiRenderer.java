package org.thecelestialworkshop.celestisynth.client.renderers.misc;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.RenderGuiEvent;
import org.joml.Matrix4f;
import org.thecelestialworkshop.celestisynth.api.mixin.PlayerMixinSupport;
import org.thecelestialworkshop.celestisynth.manager.CSConfigManager;
import software.bernie.geckolib.core.object.Color;

public class CSGuiRenderer {
    public RenderGuiEvent event;
    public GuiGraphics gui;
    public PoseStack pose;
    public Matrix4f poseMatrix;
    public MultiBufferSource.BufferSource buffer;
    public Font font;

    public CSGuiRenderer(RenderGuiEvent event) {
        this.event = event;
        this.gui = event.getGuiGraphics();
        this.pose = gui.pose();
        this.poseMatrix = pose.last().pose();
        this.buffer = gui.bufferSource();
        this.font = Minecraft.getInstance().font;
    }

    public int getWidth() {
        return event.getWindow().getWidth() / 2;
    }

    public int getHeight() {
        return event.getWindow().getHeight() / 2;
    }

    public void renderGuiAdditions() {
    }

    public void renderGuiAdditionsPre() {
    }

    public void renderGuiAdditionsPost() {
        if (Minecraft.getInstance().player instanceof PlayerMixinSupport mixinPlayer && !Minecraft.getInstance().isPaused()) {
            int pointerX = (getWidth() / 2);
            int pointerY = (getHeight() / 2);
            if (CSConfigManager.CLIENT.showChantMessages.get()) {
                if (mixinPlayer.getChantMark() < 20 && !mixinPlayer.getChantMessage().isEmpty()) {
                    Component text = Component.translatable(mixinPlayer.getChantMessage());
                    int textLength = font.width(text.getVisualOrderText());

                    int lerp = (int) Mth.lerp(mixinPlayer.getChantMark() / 20F, 255F, 0);
                    Color base = new Color(mixinPlayer.getChantColor());
                    Color pulse = Color.ofRGBA(base.getRed(), base.getGreen(), base.getBlue(), Mth.clamp(lerp, 0, 255));
                    Color modifiedPulse = pulse.darker(5);

                    float txt = textLength / 2F;
                    float xOffset = -(getWidth() / 8F) * 1.3F;
                    float yOffset = -((getHeight() / 8F) * 1.3F) + 7.5F;
                    font.drawInBatch8xOutline(text.getVisualOrderText(), pointerX + xOffset - txt, pointerY + yOffset, pulse.argbInt(), modifiedPulse.argbInt(), poseMatrix.scale(1.5F), buffer, LightTexture.FULL_BRIGHT);
                    poseMatrix.normal().scale(1.5F);
                }
            }
        }
    }
}
