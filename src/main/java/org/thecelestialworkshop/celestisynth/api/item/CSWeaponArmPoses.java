package org.thecelestialworkshop.celestisynth.api.item;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;

public class CSWeaponArmPoses {
    public static final HumanoidModel.ArmPose SOLARIS_POSE = HumanoidModel.ArmPose.create("SOLARIS", false, (model, entity, arm) -> {
        float rotation = (entity.isSprinting() ? 20 : 0) + Mth.sin((float) entity.tickCount / 10) * 5;
        float running = entity.isSprinting() ? 40 + (Mth.cos((float) entity.tickCount / 4) * 5) : 0;

        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.zRot = (float) Math.toRadians(15 + rotation);
            model.rightArm.xRot = (float) Math.toRadians(30 + running);
        } else {
            model.leftArm.zRot = -((float) Math.toRadians(15 + rotation));
            model.leftArm.xRot = (float) Math.toRadians(30 + running);
        }
    });
    public static final HumanoidModel.ArmPose CRESCENTIA_POSE = HumanoidModel.ArmPose.create("CRESCENTIA", false, (model, entity, arm) -> {
        float rotation = (entity.isSprinting() ? 30 : -10) + Mth.sin((float) entity.tickCount / 22.5F) * 10;
        float running = entity.isSprinting() ? 40 + (Mth.cos((float) entity.tickCount / 8) * 5) : 0;

        if (arm == HumanoidArm.RIGHT) {
            model.rightArm.zRot = (float) Math.toRadians(30 + rotation);
            model.rightArm.xRot = (float) Math.toRadians(30 + running);
        } else {
            model.leftArm.zRot = -((float) Math.toRadians(30 + rotation));
            model.leftArm.xRot = (float) Math.toRadians(30 + running);
        }
    });
}
