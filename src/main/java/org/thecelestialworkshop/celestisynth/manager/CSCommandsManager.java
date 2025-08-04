package org.thecelestialworkshop.celestisynth.manager;

import org.thecelestialworkshop.celestisynth.api.animation.player.LayerManager;
import org.thecelestialworkshop.celestisynth.api.animation.player.PlayerAnimationContainer;
import org.thecelestialworkshop.celestisynth.common.network.c2s.UpdateAnimationToAllPacket;
import org.thecelestialworkshop.celestisynth.common.registry.CSPlayerAnimations;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CSCommandsManager {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext pContext) {
        dispatcher.register(Commands.literal("celestisynth")
                .then(Commands.literal("animation")
                        .then(Commands.literal("clear")
                                .executes(CSCommandsManager::clearAnimation))
//                        .then(Commands.literal("set")
//                                .then(Commands.argument("animation", ResourceArgument.resource(pContext, CSPlayerAnimations.ANIMATIONS_KEY)))
//                                .executes(commandContext -> setAnimation(commandContext, ResourceArgument.getResource(commandContext, "enchantment", CSPlayerAnimations.ANIMATIONS_KEY))))
        ));
    }

    private static int clearAnimation(CommandContext<CommandSourceStack> command) {
        if (command.getSource().getEntity() instanceof Player player) {
            CSNetworkManager.sendToAll(new UpdateAnimationToAllPacket(LayerManager.MAIN_LAYER, player.getId(), CSPlayerAnimations.CLEAR.getId()));
            CSNetworkManager.sendToAll(new UpdateAnimationToAllPacket(LayerManager.MIRRORED_LAYER, player.getId(), CSPlayerAnimations.CLEAR.getId()));
            command.getSource().sendSuccess(() -> Component.translatable("commands.celestisynth.clear_animation"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int setAnimation(CommandContext<CommandSourceStack> command, Holder<PlayerAnimationContainer> animation) {
        if (command.getSource().getEntity() instanceof Player player) {
            CSNetworkManager.sendToAll(new UpdateAnimationToAllPacket(LayerManager.MAIN_LAYER, player.getId(), animation.get().animationId()));
            //command.getSource().sendSuccess(() -> Component.translatable("commands.celestisynth.clear_animation"), false);
        }
        return Command.SINGLE_SUCCESS;
    }

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event){
        CSCommandsManager.register(event.getDispatcher(), event.getBuildContext());
    }
}
