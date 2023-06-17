package com.aqutheseal.celestisynth.core;

import com.aqutheseal.celestisynth.common.block.CSBlocks;
import com.aqutheseal.celestisynth.common.entity.CSEntities;
import com.aqutheseal.celestisynth.common.item.CSItems;
import com.aqutheseal.celestisynth.common.sound.CSSounds;
import com.aqutheseal.celestisynth.common.world.feature.CSFeatures;

public class Registry {
    public static void loadClasses(){
        CSItems.init();
        CSBlocks.init();
        CSEntities.init();
        CSSounds.init();
        CSFeatures.init();
    }
}
