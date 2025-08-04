package org.thecelestialworkshop.celestisynth.api.item;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.util.Lazy;

import java.util.UUID;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

public interface CSDataPackableStatItem {

    IntSupplier getActualAttackDamage();

    DoubleSupplier getAttackSpeed();
    DoubleSupplier getAttackKnockback();
    DoubleSupplier getAttackReach();
    DoubleSupplier getBlockReach();

    Lazy<? extends Multimap<Attribute, AttributeModifier>> getAttributes();

    void setAttributes(Lazy<? extends Multimap<Attribute, AttributeModifier>> attributes);

    static UUID getBlockReachUUIDMod() {
        return UUID.fromString("1C0F03EC-EEB6-414A-8AC6-2A0913844821");
    }

    static UUID getEntityReachUUIDMod() {
        return UUID.fromString("2C0F04EC-EEB6-414A-1AC2-4A0353075221");
    }

    static UUID getKBUUIDMod() {
        return UUID.fromString("031FCABC-A15C-45C1-B799-5068DB1EAA98");
    }
}