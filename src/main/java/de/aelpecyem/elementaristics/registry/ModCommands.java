package de.aelpecyem.elementaristics.registry;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import de.aelpecyem.elementaristics.common.feature.stats.AscensionPath;
import de.aelpecyem.elementaristics.common.feature.stats.IElemStats;
import de.aelpecyem.elementaristics.lib.StatHelper;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.server.command.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stat;
import net.minecraft.text.TranslatableText;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class ModCommands {
    public static void registerCommands() {
        LiteralArgumentBuilder builder = CommandManager.literal("elem").requires(source -> source.hasPermissionLevel(2));

        for(AscensionPath path : AscensionPath.PATHS.values())
            builder.then(CommandManager.literal("setAscensionPath").then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal(path.getName()).executes(context -> executeSetAscensionPath(context, path, EntityArgumentType.getPlayers(context, "targets"))))));
        builder.then(CommandManager.literal("setAscensionStage")
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("stage", IntegerArgumentType.integer(0))
                                .executes(context -> executeSetAscensionStage(context, IntegerArgumentType.getInteger(context, "stage"), EntityArgumentType.getPlayers(context, "targets"))))));
        builder.then(CommandManager.literal("setMagan")
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> executeSetMagan(context, IntegerArgumentType.getInteger(context, "amount"), EntityArgumentType.getPlayers(context, "targets"))))));
        CommandRegistrationCallback.EVENT.register((displatcher, b) -> displatcher.register(builder));
    }

    private static int executeSetMagan(CommandContext<ServerCommandSource> context, int amount, Collection<ServerPlayerEntity> playerEntity) {
        int i = 0;
        for (ServerPlayerEntity player : playerEntity) {
            if (StatHelper.getMagan(player) > amount) i++;
            context.getSource().sendFeedback(new TranslatableText("elementaristics.command.set_magan.success", player.getDisplayName(), StatHelper.setMagan(amount, player)), true);
        }
        return i;
    }

    private static int executeSetAscensionStage(CommandContext<ServerCommandSource> context, int stage, Collection<ServerPlayerEntity> playerEntity) {
        int i = 0;
        for (ServerPlayerEntity player : playerEntity) {
            ((IElemStats) player).setAscensionStage((byte) stage);
            context.getSource().sendFeedback(new TranslatableText("elementaristics.command.set_stage.success", player.getDisplayName(), stage), true);
        }
        return i;
    }

    private static int executeSetAscensionPath(CommandContext<ServerCommandSource> context, AscensionPath path, Collection<ServerPlayerEntity> playerEntity) {
        int i = 0;
        for (ServerPlayerEntity player : playerEntity) {
            if (!((IElemStats) player).getAscensionPath().equals(path)) i++;

            ((IElemStats) player).setAscensionPath(path.getName());
            context.getSource().sendFeedback(new TranslatableText("elementaristics.command.set_path.success", player.getDisplayName(), path.getName()), true);

        }
        return i;
    }
}
