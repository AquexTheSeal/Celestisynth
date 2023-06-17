package com.aqutheseal.celestisynth;

import com.aqutheseal.celestisynth.client.animation.CSAnimator;
import com.aqutheseal.celestisynth.data.CSBlockModelProvider;
import com.aqutheseal.celestisynth.data.CSBlockstateProvider;
import com.aqutheseal.celestisynth.data.CSItemModelProvider;
import com.aqutheseal.celestisynth.data.CSRecipeProvider;
import com.aqutheseal.celestisynth.network.ForgeCSNetwork;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import software.bernie.geckolib.GeckoLib;

import static com.aqutheseal.celestisynth.Celestisynth.MODID;

@Mod(MODID)
public class CelestisynthForge {

    public CelestisynthForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                bus.addListener(this::registerAnimationLayer)
        );

        bus.addListener(this::registerPackets);
        bus.addListener(this::gatherData);

        Celestisynth.init();
        GeckoLib.initialize();
    }

    private void registerAnimationLayer(FMLClientSetupEvent event) {
        PlayerAnimationAccess.REGISTER_ANIMATION_EVENT.register(CSAnimator::registerPlayerAnimation);
    }

    private void registerPackets(FMLCommonSetupEvent event) {
        ForgeCSNetwork.register();
    }

    private void gatherData(final GatherDataEvent event) {
        DataGenerator dataGenerator = event.getGenerator();
        final ExistingFileHelper efh = event.getExistingFileHelper();
        if (event.includeServer()) {
            dataGenerator.addProvider(true, new CSBlockModelProvider(dataGenerator.getPackOutput(), MODID, efh));
            dataGenerator.addProvider(true, new CSBlockstateProvider(dataGenerator.getPackOutput(), MODID, efh));
            dataGenerator.addProvider(true, new CSItemModelProvider(dataGenerator.getPackOutput(), MODID, efh));
            dataGenerator.addProvider(true, new CSRecipeProvider(dataGenerator.getPackOutput()));
            // dataGenerator.addProvider(true, new CSSoundProvider(dataGenerator.getPackOutput(), MODID, efh));
        }
    }
}