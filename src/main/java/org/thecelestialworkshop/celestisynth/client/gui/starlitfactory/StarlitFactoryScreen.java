package org.thecelestialworkshop.celestisynth.client.gui.starlitfactory;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import org.thecelestialworkshop.celestisynth.common.registry.CSBlocks;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import software.bernie.geckolib.core.object.Color;

import java.util.ArrayList;

public class StarlitFactoryScreen extends AbstractContainerScreen<StarlitFactoryMenu> {
    public static final ResourceLocation FACTORY_GUI = Celestisynth.prefix("textures/gui/starlit_factory.png");
    private final ArrayList<FactoryStar> stars = new ArrayList<>();
    private int usedProTip;

    public StarlitFactoryScreen(StarlitFactoryMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
        this.usedProTip = Minecraft.getInstance().player.getRandom().nextInt(5);
    }

    @Override
    public void render(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);

        if (Minecraft.getInstance().player.tickCount % 200 == 0) {
            this.usedProTip = this.usedProTip >= 4 ? 0 : this.usedProTip + 1;
        }

        MutableComponent text = Component.translatable("tip.celestisynth.starlit_factory_" + usedProTip);
        int i = 0;
        for (FormattedCharSequence formattedcharsequence : this.font.split(text, 180)) {
            pGuiGraphics.drawCenteredString(this.font, formattedcharsequence, super.leftPos + 89, super.topPos + 170 + (i * 9), Color.WHITE.argbInt());
            i++;
        }

        pGuiGraphics.pose().pushPose();
        double xOff = (double) width / 2;
        double yOff = (double) height / 2;
        pGuiGraphics.pose().translate(xOff, yOff, 0);
        assert Minecraft.getInstance().player != null;
        pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(Mth.sin((float) (Minecraft.getInstance().player.tickCount * 0.015))));
        pGuiGraphics.pose().translate(-xOff, -yOff, 0);

        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);

        assert minecraft.level != null;
        RandomSource random = minecraft.level.random;
        if (random.nextInt(10) == 0) {
            int offset = random.nextInt(pGuiGraphics.guiWidth());
            stars.add(new FactoryStar(-50 + offset, -20, 0.5 + random.nextDouble(), 0.5 + random.nextDouble()));
        }

        pGuiGraphics.pose().popPose();
    }

    @Override
    public void renderBackground(GuiGraphics pGuiGraphics) {
        int factoryForgeTime = menu.data.get(2);
        if (factoryForgeTime > 0) {
            float motion = Mth.sin((float) (Minecraft.getInstance().player.tickCount * 0.2));
            Color color = Color.ofRGBA(0, 0, 0.1F + motion * 0.1F, 0.75F);
            pGuiGraphics.fillGradient(0, 0, this.width, this.height, color.argbInt(), color.darker(15).argbInt());
            MinecraftForge.EVENT_BUS.post(new ScreenEvent.BackgroundRendered(this, pGuiGraphics));
        } else {
            super.renderBackground(pGuiGraphics);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY) {
        assert Minecraft.getInstance().player != null;
        float motion = Mth.sin((float) (Minecraft.getInstance().player.tickCount * 0.2)) * 1;
        Color color = Color.ofRGBA(0.75F + (motion * 0.25F), 0.75F + (motion * 0.25F), 1F, 1F);
        pGuiGraphics.renderItem(new ItemStack(CSBlocks.STARLIT_FACTORY.get()), this.titleLabelX - 5, this.titleLabelY - 22);
        this.font.drawInBatch8xOutline(this.title.getVisualOrderText(), this.titleLabelX + 15, this.titleLabelY - 15, color.argbInt(), color.darker(5F).getColor(), pGuiGraphics.pose().last().pose(), pGuiGraphics.bufferSource(), LightTexture.FULL_BRIGHT);
    }

    @Override
    protected void renderBg(GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {

        assert Minecraft.getInstance().player != null;
        float motion = Mth.sin((float) (Minecraft.getInstance().player.tickCount * 0.05)) * 1;
        float motion2 = Mth.cos((float) (Minecraft.getInstance().player.tickCount * 0.05)) * 1;

        for (FactoryStar star : this.stars) {
            if (star.tickCount < 1000) {
                star.render(pGuiGraphics);
            }
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(0.95F + (motion * 0.05F), 0.95F + (motion2 * 0.05F), 1.0F, 1.0F);

        pGuiGraphics.blit(FACTORY_GUI, super.leftPos, super.topPos, 0, 0, this.imageWidth, this.imageHeight);

        int energyBurnTime = menu.data.get(0);
        int energyAmount = menu.data.get(1);
        int factoryForgeTime = menu.data.get(2);
        int maxFactoryForgeTime = menu.data.get(3);

        boolean isHoldingValidRecipe = menu.data.get(4) == 1;

        double burnProgress = (double) energyBurnTime / 100;
        pGuiGraphics.blit(FACTORY_GUI, super.leftPos + 135, super.topPos + 30, 193, 16, (int) (burnProgress * 14), 2);

        double energyAmountProgress = (double) energyAmount / 1000;
        int vOffset = (int) (52 - (energyAmountProgress * 52));
        pGuiGraphics.blit(FACTORY_GUI, super.leftPos + 152, super.topPos + 17 + vOffset, 176,  vOffset, 16, (int) (energyAmountProgress * 52));

        double forgeProgress = (double) factoryForgeTime / maxFactoryForgeTime;
        pGuiGraphics.blit(FACTORY_GUI, super.leftPos + 62, super.topPos + 3, 208, 0, 44, (int) (forgeProgress * 75));

        if (isHoldingValidRecipe) {
            pGuiGraphics.blit(FACTORY_GUI, super.leftPos + 26, super.topPos + 34, 192, 0, 16, 15);
        }
    }

    public static class FactoryStar {
        private final int centerX;
        private final int centerY;
        private final double speedX;
        private final double speedY;

        public float scale;
        public int tickCount;
        public boolean isReverse;

        public FactoryStar(int centerX, int centerY, double speedX, double speedY) {
            this.centerX = centerX;
            this.centerY = centerY;
            this.speedX = speedX;
            this.speedY = speedY;
            this.scale = 1F + Minecraft.getInstance().player.getRandom().nextFloat() * 0.5F;
            this.isReverse = Minecraft.getInstance().player.getRandom().nextBoolean();
        }

        public void render(GuiGraphics pGuiGraphics) {
            tickCount++;
            assert Minecraft.getInstance().player != null;

            RenderSystem.enableBlend();
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 0.5F + Mth.sin(tickCount * 0.02F) + 0.5F);
            pGuiGraphics.pose().pushPose();

            double xOff = centerX + (tickCount * speedX);
            double yOff = centerY + (tickCount * speedY);

            //pGuiGraphics.pose().translate(xOff, yOff, 0);
            pGuiGraphics.pose().mulPose(Axis.ZP.rotationDegrees(tickCount * (isReverse ? -0.2F : 0.2F)));
            //pGuiGraphics.pose().translate(-xOff, -yOff, 0);
            pGuiGraphics.pose().scale(scale, scale, scale);
            pGuiGraphics.blit(FACTORY_GUI, (int) xOff, (int) yOff, 192, 32, 16, 16);

            pGuiGraphics.pose().popPose();
            pGuiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.disableBlend();
        }
    }
}
