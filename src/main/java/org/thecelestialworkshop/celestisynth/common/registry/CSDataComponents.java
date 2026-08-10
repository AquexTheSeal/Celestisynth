package org.thecelestialworkshop.celestisynth.common.registry;

import org.thecelestialworkshop.celestisynth.Celestisynth;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.registries.DeferredRegister;

import org.jetbrains.annotations.Nullable;
import java.util.function.Supplier;

/**
 * 1.21 replacement for the 1.20.1 stack NBT sub-tags "csController" and
 * "csExtras". The live-tag accessors intentionally mirror the old
 * getOrCreateTagElement contract: gameplay code mutates the returned tag in
 * place, and (as in 1.20.1) the logic runs on both sides, so client and
 * server each maintain their own mirror of this state.
 */
public class CSDataComponents {
    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, Celestisynth.MODID);

    public static final Supplier<DataComponentType<CustomData>> CS_CONTROLLER = DATA_COMPONENTS.register("cs_controller",
            () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    public static final Supplier<DataComponentType<CustomData>> CS_EXTRAS = DATA_COMPONENTS.register("cs_extras",
            () -> DataComponentType.<CustomData>builder().persistent(CustomData.CODEC).networkSynchronized(CustomData.STREAM_CODEC).build());

    public static CompoundTag getOrCreateLiveTag(ItemStack stack, Supplier<DataComponentType<CustomData>> type) {
        CustomData data = stack.get(type.get());
        if (data == null) {
            data = CustomData.of(new CompoundTag());
            stack.set(type.get(), data);
        }
        return data.getUnsafe();
    }

    @Nullable
    public static CompoundTag getLiveTag(ItemStack stack, Supplier<DataComponentType<CustomData>> type) {
        CustomData data = stack.get(type.get());
        return data == null ? null : data.getUnsafe();
    }
}
