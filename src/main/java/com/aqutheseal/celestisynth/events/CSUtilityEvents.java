package com.aqutheseal.celestisynth.events;

import com.aqutheseal.celestisynth.Celestisynth;
import com.aqutheseal.celestisynth.item.CrescentiaItem;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Celestisynth.MODID)
public class CSUtilityEvents {

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        CrescentiaItem.doCrescentiaDamageReduction(event);
    }
}
