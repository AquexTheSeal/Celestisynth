package com.aqutheseal.celestisynth.common.entity.helper;

public class CSVisualModel {
    public static CSVisualModel FLAT = new CSVisualModel("cs_effect_flat");
    public static CSVisualModel FLAT_INVERTED = new CSVisualModel("cs_effect_flat_inverted");
    public static CSVisualModel FLAT_VERTICAL_SIDEFACE = new CSVisualModel("cs_effect_flat_vertical");
    public static CSVisualModel FLAT_VERTICAL_FRONTFACE = new CSVisualModel("cs_effect_flat_vertical_side");
    public static CSVisualModel SIX_WAY_CROSS = new CSVisualModel("cs_effect_cross");
    public static CSVisualModel WALL = new CSVisualModel("cs_effect_wall");

    final String modelName;

    public CSVisualModel(String model) {
        this.modelName = model;
    }

    public String getModelName() {
        return modelName;
    }
}
