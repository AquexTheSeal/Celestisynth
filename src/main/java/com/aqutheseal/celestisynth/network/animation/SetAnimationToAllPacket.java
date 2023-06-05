package com.aqutheseal.celestisynth.network.animation;

import com.aqutheseal.celestisynth.animation.AnimationManager;
import com.aqutheseal.celestisynth.animation.CSAnimator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SetAnimationToAllPacket {
    private final int id;
    private final int animId;

    public SetAnimationToAllPacket(int id, int animId) {
        this.id = id;
        this.animId = animId;
    }

    public SetAnimationToAllPacket(FriendlyByteBuf buf) {
        this.id = buf.readInt();
        this.animId = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(id);
        buf.writeInt(animId);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            Minecraft instance = Minecraft.getInstance();
            var player = instance.level.getEntity(id);
            animatePlayer(animId, (AbstractClientPlayer) player);
        });
        return true;
    }

    public static void animatePlayer(int animId, AbstractClientPlayer player) {
        var animation = CSAnimator.animationData.get(player);
        if (animation != null) {
            AnimationManager.playAnimation(AnimationManager.getAnimFromId(animId).getAnimation(), animation);
        }
    }
}
