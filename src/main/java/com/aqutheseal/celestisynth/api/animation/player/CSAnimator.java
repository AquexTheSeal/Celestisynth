package com.aqutheseal.celestisynth.api.animation.player;

import com.aqutheseal.celestisynth.common.item.weapons.RainfallSerenityItem;
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
            float xRotMod = 0, yRotMod = 0, zRotMod = 0;
            float xMod = 0, yMod = 0, zMod = 0;

            if (player.getUseItem().getItem() instanceof RainfallSerenityItem) {
                boolean checkHand = player.getUsedItemHand() == InteractionHand.MAIN_HAND;
                float f0 = (float) Math.toRadians(player.getXRot()) * 1.75F;
                float f1 = (float) Math.toRadians(player.getXRot()) * 9F;
                float rSplit0 = rotationSplit(player.getXRot(), f0, f1);
                if (checkHand) {
                    switch (partName) {
                        case "leftArm" -> {
                            xRotMod = (float) Math.toRadians(player.getXRot()) / 6;
                            if (FirstPersonMode.isFirstPersonPass()) {
                                xMod = rSplit0;
                            }
                        }
                        case "rightArm" -> {
                            zRotMod = (float) -Math.toRadians(player.getXRot());
                            if (FirstPersonMode.isFirstPersonPass()) {
                                xMod = rSplit0;
                            }
                        }
                        case "head" -> {
                            xRotMod = (float) Math.toRadians(player.getXRot());
                        }
                        default -> {
                            return Optional.empty();
                        }
                    }
                } else {
                    switch (partName) {
                        case "rightArm" -> {
                            xRotMod = (float) -Math.toRadians(player.getXRot()) / 6;
                            if (FirstPersonMode.isFirstPersonPass()) {
                                xMod = -rSplit0;
                            }
                        }
                        case "leftArm" -> {
                            zRotMod = (float) Math.toRadians(player.getXRot());
                            if (FirstPersonMode.isFirstPersonPass()) {
                                xMod = -rSplit0;
                            }
                        }
                        case "head" -> {
                            xRotMod = (float) Math.toRadians(player.getXRot());
                        }
                        default -> {
                            return Optional.empty();
                        }
                    }
                }
            }
            if (player.isCrouching()) {
                if (partName.equals("rightArm") || partName.equals("leftArm")) {
                    yMod += 3;
                }
                if (partName.equals("head")) {
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

    public static float rotationSplit(float faceRotation, float faceUpValue, float faceDownValue) {
        if (faceRotation < 0) {
            return faceUpValue;
        } else if (faceRotation > 0) {
            return faceDownValue;
        } else {
            return 0;
        }
    }
}
