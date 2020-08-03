package de.aelpecyem.elementaristics.registry;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.common.feature.stats.AscensionPath;
import de.aelpecyem.elementaristics.common.feature.stats.IElemStats;
import de.aelpecyem.elementaristics.lib.StatHelper;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.command.arguments.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;

import java.util.Collection;

import static de.aelpecyem.elementaristics.common.handler.AlchemyHandler.Helper.*;

public class ModCommands {
    public static void registerCommands() {
        LiteralArgumentBuilder builder = CommandManager.literal("elem").requires(source -> source.hasPermissionLevel(2));

        for (AscensionPath path : AscensionPath.PATHS.values())
            builder.then(CommandManager.literal("setAscensionPath").then(CommandManager.argument("targets", EntityArgumentType.players()).then(CommandManager.literal(path.getName()).executes(context -> executeSetAscensionPath(context, path, EntityArgumentType.getPlayers(context, "targets"))))));
        builder.then(CommandManager.literal("setAscensionStage")
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("stage", IntegerArgumentType.integer(0))
                                .executes(context -> executeSetAscensionStage(context, IntegerArgumentType.getInteger(context, "stage"), EntityArgumentType.getPlayers(context, "targets"))))));
        builder.then(CommandManager.literal("setMagan")
                .then(CommandManager.argument("targets", EntityArgumentType.players())
                        .then(CommandManager.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> executeSetMagan(context, IntegerArgumentType.getInteger(context, "amount"), EntityArgumentType.getPlayers(context, "targets"))))));
        builder.then(CommandManager.literal("alchemy")
                .then(CommandManager.literal("destabilize")
                        .executes(context -> convertHeldItem(context, 0)))
                .then(CommandManager.literal("convert")
                        .executes(context -> convertHeldItem(context, 1)))
                .then(CommandManager.literal("stabilize")
                        .executes(context -> convertHeldItem(context, 2)))
                .then(CommandManager.literal("setAttunement")
                        .then(CommandManager.argument("aether", IntegerArgumentType.integer(0, 5))
                                .then(CommandManager.argument("fire", IntegerArgumentType.integer(0, 5))
                                        .then(CommandManager.argument("water", IntegerArgumentType.integer(0, 5))
                                                .then(CommandManager.argument("earth", IntegerArgumentType.integer(0, 5))
                                                        .then(CommandManager.argument("air", IntegerArgumentType.integer(0, 5))
                                                                .then(CommandManager.argument("potential", IntegerArgumentType.integer(0, 5)).executes(context -> changeAttunement(context))))))))));

        CommandRegistrationCallback.EVENT.register((displatcher, b) -> displatcher.register(builder));
    }
    private static int changeAttunement(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        setAttunement(player.getMainHandStack(), new AspectAttunement(IntegerArgumentType.getInteger(context, "aether"),
                IntegerArgumentType.getInteger(context, "fire"), IntegerArgumentType.getInteger(context, "water"),
                IntegerArgumentType.getInteger(context, "earth"), IntegerArgumentType.getInteger(context, "air"),
                IntegerArgumentType.getInteger(context, "potential")));
        player.setStackInHand(Hand.MAIN_HAND, stabilize(player.getMainHandStack()));
        context.getSource().sendFeedback(new TranslatableText("elementaristics.command.set_attunement.success", player.getMainHandStack(), getAttunement(player.getMainHandStack()).toText()), true);
        return 15;
    }

    private static int convertHeldItem(CommandContext<ServerCommandSource> context, int type) throws CommandSyntaxException {
        PlayerEntity player = context.getSource().getPlayer();
        ItemStack originalItem = player.getMainHandStack();
        switch (type) {
            case 0:
                player.setStackInHand(Hand.MAIN_HAND, convertToAlchemyItem(player.getMainHandStack()));
                break;
            case 1:
                player.setStackInHand(Hand.MAIN_HAND, convertToOriginalItem(player.getMainHandStack()));
                break;
            case 2:
                player.setStackInHand(Hand.MAIN_HAND, stabilize(player.getMainHandStack()));
                break;
        }
        if (!originalItem.equals(player.getMainHandStack()))
            context.getSource().sendFeedback(new TranslatableText("elementaristics.command.convert_item.success", originalItem.getName(), player.getMainHandStack().getName()), true);
        else
            context.getSource().sendError(new TranslatableText("elementaristics.command.convert_item.failure", originalItem.getName()));
        return !originalItem.equals(player.getMainHandStack()) ? 15 : 0;
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
