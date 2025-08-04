package org.thecelestialworkshop.celestisynth.common.compat.jei.drawable;

import org.thecelestialworkshop.celestisynth.client.gui.starlitfactory.StarlitFactoryScreen;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class StarlitFactoryDrawable implements IDrawable {
    public StarlitFactoryDrawable() {
    }

    @Override
    public int getWidth() {
        return 176;
    }

    @Override
    public int getHeight() {
        return 79;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int i, int i1) {
        guiGraphics.blit(StarlitFactoryScreen.FACTORY_GUI, i, i1, 0, 0, this.getWidth(), this.getHeight());

        guiGraphics.blit(StarlitFactoryScreen.FACTORY_GUI, i + 135, i1 + 30, 193, 16, 14, 2);
        guiGraphics.blit(StarlitFactoryScreen.FACTORY_GUI, i + 152, i1 + 17, 176, 0, 16, 52);
        //guiGraphics.blit(StarlitFactoryScreen.FACTORY_GUI, i + 62, i1 + 3, 208, 0, 44, 75);
        guiGraphics.blit(StarlitFactoryScreen.FACTORY_GUI, i + 26, i1 + 34, 192, 0, 16, 15);
    }
}
