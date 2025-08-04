package org.thecelestialworkshop.celestisynth.client.renderers.misc.tooltips;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.util.ExtraUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.joml.Matrix4f;
import software.bernie.geckolib.core.object.Color;

public class PassiveComponent {

    public record Data(String itemName, int totalPassiveAmount, int highlightedPassiveIndex) implements TooltipComponent {
    }

    public record Renderer(Data data) implements ClientTooltipComponent {
        public static final int DESC_WRAP_WIDTH = 320;
        public static final int ICON_HEIGHT = 16;
        public static final int ICON_OFFSETED_HEIGHT = ICON_HEIGHT + 8;

        @Override
        public int getHeight() {
            return this.descriptionWordWrapHeight() + ICON_OFFSETED_HEIGHT + 20;
        }

        @Override
        public int getWidth(Font pFont) {
            return (int) (DESC_WRAP_WIDTH * 0.8);
        }

        @Override
        public void renderText(Font pFont, int pMouseX, int pMouseY, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {

            Matrix4f copyMatrix = new Matrix4f(pMatrix);
            copyMatrix.translate(pMouseX, pMouseY, 0);
            copyMatrix.scale(0.8F);
            copyMatrix.translate(-pMouseX, -pMouseY, 0);
            pFont.drawInBatch(this.highlightedName(), pMouseX, pMouseY + ICON_OFFSETED_HEIGHT, -1, true, pMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
            this.drawWordWrap(pFont, this.highlightedDescription(), pMouseX, pMouseY + ICON_OFFSETED_HEIGHT + 10, DESC_WRAP_WIDTH, -1, pMatrix, pBufferSource);
        }

        @Override
        public void renderImage(Font pFont, int pX, int pY, GuiGraphics pGuiGraphics) {
            int order = 0;
            for (int i = 1; i < data.totalPassiveAmount() + 1; i++) {
                if (i != data.highlightedPassiveIndex()) {
                    pGuiGraphics.setColor(0.25F, 0.25F, 0.25F, 1.0F);
                }
                pGuiGraphics.pose().pushPose();
                pGuiGraphics.pose().translate(pX + order, pY, 0);
                pGuiGraphics.pose().scale(0.65F, 0.65F, 0.65F);
                pGuiGraphics.pose().translate(-pX + order, -pY, 0);
                pGuiGraphics.blit(Celestisynth.prefix("textures/passiveicon/" + data.itemName() + "_passive_icon_" + i + ".png"), pX + order, pY - 4, 0, 0, 32, 32, 32, 32);
                pGuiGraphics.pose().popPose();

                pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
                order += 12;
            }
            pGuiGraphics.fill(pX - 10, pY + ICON_OFFSETED_HEIGHT - 3, pX + this.getWidth(pFont) + 10, pY + ICON_OFFSETED_HEIGHT + this.descriptionWordWrapHeight() + 9, Color.BLACK.argbInt());
        }

        public Component highlightedName() {
            int tickCount = (int) (Minecraft.getInstance().player.tickCount * 0.5);
            return Component.translatable("item.celestisynth." + data.itemName() + ".passive_" + data.highlightedPassiveIndex()).withStyle(Style.EMPTY.withColor(ExtraUtil.getCelestialColor(tickCount).argbInt()));
        }

        public Component highlightedDescription() {
            return Component.translatable("item.celestisynth." + data.itemName() + ".desc_" + data.highlightedPassiveIndex()).withStyle(Style.EMPTY.withColor(Color.GRAY.argbInt()));
        }

        public int descriptionWordWrapHeight() {
            return Minecraft.getInstance().font.wordWrapHeight(this.highlightedDescription(), DESC_WRAP_WIDTH);
        }

        public void drawWordWrap(Font pFont, FormattedText pText, int pX, int pY, int pLineWidth, int pColor, Matrix4f pMatrix, MultiBufferSource.BufferSource pBufferSource) {
            for(FormattedCharSequence formattedcharsequence : pFont.split(pText, pLineWidth)) {
                Matrix4f copyMatrix = new Matrix4f(pMatrix);
                copyMatrix.translate(pX, pY, 0);
                copyMatrix.scale(0.8F);
                copyMatrix.translate(-pX, -pY, 0);
                pFont.drawInBatch(formattedcharsequence, pX, pY, pColor, true, copyMatrix, pBufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
                pY += 9;
            }

        }
    }
}
