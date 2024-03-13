package com.aqutheseal.celestisynth.util;

import com.aqutheseal.celestisynth.common.capabilities.CSItemStackCapabilityProvider;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SkinUtil {

    public static List<UUID> getAquaSkinWhitelist() {
        List<UUID> whitelist = new ArrayList<>();
        whitelist.add(UUID.fromString("1004d7f3-e6ff-372d-afa5-7ea0a6397074"));
        whitelist.add(UUID.fromString("4cbe462d-50b3-4c85-add1-b7e4d5b5673b"));
        return whitelist;
    }

    public static int getSkinIndex(ItemStack stack) {
        AtomicInteger aquaSkinFlag = new AtomicInteger(0);
        stack.getCapability(CSItemStackCapabilityProvider.CAPABILITY).ifPresent(data -> {
            aquaSkinFlag.set(data.getSkinIndex());
        });
        return aquaSkinFlag.get();
    }
}
