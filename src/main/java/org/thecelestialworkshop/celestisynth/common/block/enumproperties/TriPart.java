package org.thecelestialworkshop.celestisynth.common.block.enumproperties;

import net.minecraft.util.StringRepresentable;

public enum TriPart implements StringRepresentable {
    LEFT("left"),
    MIDDLE("middle"),
    RIGHT("right");

    private final String name;

    TriPart(String name) {
        this.name = name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
