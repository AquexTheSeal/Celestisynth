package org.thecelestialworkshop.celestisynth.common.compat.apotheosis;

import org.thecelestialworkshop.celestisynth.common.entity.projectile.RainfallArrow;
import org.thecelestialworkshop.celestisynth.manager.CSIntegrationManager;
import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.registries.RegistryObject;

public class CSCompatAP {

    public static float apothRainfallSerenityDrawSpeed(LivingEntity entity) {
        if (!CSIntegrationManager.checkApothicAttributes()) return 0;
        return apothValue(ALObjects.Attributes.DRAW_SPEED, entity);
    }

    public static void installApothRainfallDamage(RainfallArrow rainfallArrow, LivingEntity pEntityLiving) {
        if (!CSIntegrationManager.checkApothicAttributes()) return;

        rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + apothValue(ALObjects.Attributes.PROT_SHRED, pEntityLiving));
        rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + apothValue(ALObjects.Attributes.PROT_PIERCE, pEntityLiving));
        rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + apothValue(ALObjects.Attributes.ARROW_VELOCITY, pEntityLiving));
        rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + apothValue(ALObjects.Attributes.ARROW_DAMAGE, pEntityLiving));
        if (pEntityLiving.getRandom().nextFloat() < apothValue(ALObjects.Attributes.CRIT_CHANCE, pEntityLiving)) {
            rainfallArrow.setBaseDamage(rainfallArrow.getBaseDamage() + apothValue(ALObjects.Attributes.CRIT_DAMAGE, pEntityLiving));
        }
    }

    public static float apothValue(RegistryObject<Attribute> attribute, LivingEntity entity) {
        return (float) entity.getAttribute(attribute.get()).getValue();
    }
}
