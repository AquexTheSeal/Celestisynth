package com.aqutheseal.celestisynth.animation;

import com.aqutheseal.celestisynth.item.weapons.RainfallSerenityItem;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.AnimationStack;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.api.layered.modifier.AdjustmentModifier;
import dev.kosmx.playerAnim.core.util.Vec3f;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;

public class CSAnimator {
    public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> animationData = new IdentityHashMap<>();
    public static final Map<AbstractClientPlayer, ModifierLayer<IAnimation>> otherAnimationData = new IdentityHashMap<>();

    public static void registerAnimationLayer(FMLClientSetupEvent event) {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(CSAnimator::registerPlayerAnimation);
    }

    public static void registerPlayerAnimation(AbstractClientPlayer player, AnimationStack stack) {
        var layer = new ModifierLayer<>();
        layer.addModifier(new AdjustmentModifier((partName) -> {
            float xRotMod = 0;
            float yRotMod = 0;
            float zRotMod = 0;
            float xMod = 0;
            float yMod = 0;
            float zMod = 0;

            double yAng = player.getXRot();
            double yBelow0Modifier = yAng > 0 ? 0 : 1;
            if (player.getUseItem().getItem() instanceof RainfallSerenityItem) {
                float isMain = player.getUsedItemHand() == InteractionHand.MAIN_HAND ? -1 : 1;
                if (partName.equals("rightArm") || partName.equals("leftArm")) {
                    if (FirstPersonMode.isFirstPersonPass()) {
                        zRotMod = (float) Math.toRadians(yAng) * isMain;
                        xMod = (float) (3 + Math.toRadians(yAng) * 6) * -isMain;
                        yMod = (float) (6.0f + Math.sin(Math.toRadians(yAng)) * yBelow0Modifier * 12.0f);
                        zMod = (float) (3 + Math.toRadians(yAng) * 6) * -isMain;
                    } else {
                        zRotMod = (float) Math.toRadians(yAng) * isMain;
                    }
                }
            }
            if (player.isCrouching()) {
                if (partName.equals("rightArm") || partName.equals("leftArm")) {
                    yMod += 3;
                }
            }
            return Optional.of(new AdjustmentModifier.PartModifier(
                    new Vec3f(xRotMod, yRotMod, zRotMod),
                    new Vec3f(xMod, yMod, zMod)
            ));
        }), 0);
        stack.addAnimLayer(6900, layer);
        CSAnimator.animationData.put(player, layer);

        var layerOther = new ModifierLayer<>();
        stack.addAnimLayer(0, layerOther);
        CSAnimator.otherAnimationData.put(player, layerOther);
    }
}
