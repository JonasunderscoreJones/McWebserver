package me.jonasjones.mcwebserver.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import me.jonasjones.mcwebserver.McWebserver;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.MessageArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static me.jonasjones.mcwebserver.McWebserver.MC_SERVER;
import static me.jonasjones.mcwebserver.web.api.v2.tokenmgr.TokenManager.*;
import static net.minecraft.server.command.CommandManager.*;

public class McWebCommand {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("mcweb").requires(serverCommandSource -> serverCommandSource.hasPermissionLevel(2))
                        .then(literal("token")
                                .then(literal("new")
                                        .then(CommandManager.argument("Name", StringArgumentType.word())
                                                .then(CommandManager.argument("Expiration Time (example: 1y3d4h -> 1 Year, 3 Days, 4 Hours)", StringArgumentType.word())
                                                        .suggests(ExpirationTimeArgumentType::suggestExpirationTimes)
                                                        .executes(context -> {
                                                            String name = StringArgumentType.getString(context, "Name");
                                                            String expires = StringArgumentType.getString(context, "Expiration Time (example: 1y3d4h -> 1 Year, 3 Days, 4 Hours)");
                                                            if (context.getSource().isExecutedByPlayer()) {
                                                            String result = registerToken(name, expires);
                                                                if (result.equals("exists")) {
                                                                    context.getSource().sendFeedback(() -> Text.of("A token with that name already exists"), false);
                                                                    return 0;
                                                                } else if (result.equals("failed")) {
                                                                    context.getSource().sendFeedback(() -> Text.of("Failed to create token (Unknown Error)"), false);
                                                                    return 0;
                                                                } else {
                                                                    context.getSource().sendFeedback(() -> Text.of("Token Created! - Expires " + convertToHumanReadable(convertExpirationDate(expires))), true);
                                                                    if (MC_SERVER != null) {
                                                                        if (context.getSource().isExecutedByPlayer()) {
                                                                            // get the player name
                                                                            String playerName = Objects.requireNonNull(context.getSource().getPlayer()).getName().getString();
                                                                            MC_SERVER.getCommandManager().executeWithPrefix(MC_SERVER.getCommandSource(), "tellraw " + playerName + " [\"\",\"Token: [\",{\"text\":\"" + result + "\",\"color\":\"green\",\"clickEvent\":{\"action\":\"copy_to_clipboard\",\"value\":\"" + result + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[\"Click to Copy to Clipboard\"]}},\"]\"]");
                                                                        }
                                                                        return 1;
                                                                    }
                                                                    context.getSource().sendFeedback(() -> Text.of("Failed to create token (Unknown Error)"), false);
                                                                    return 0;
                                                                }
                                                            } else {
                                                                context.getSource().sendFeedback(() -> Text.of("ERROR: Due to Security concerns it is not possible to generate tokens from the server console as they are saved in server logs. Tokens must be created by a player! (more info at: https://wiki.jonasjones.dev/McWebserver/Tokens)"), false);
                                                                return 0;
                                                            }
                                                        }))))
                                .then(literal("list")
                                        .executes(context -> {
                                            context.getSource().sendFeedback(() -> Text.of(listTokens()), false);
                                            return 1;
                                        }))
                                .then(literal("delete")
                                        .then(argument("Token Name", StringArgumentType.word())
                                                .suggests(DeleteTokenNameArgumentType::suggestTokenNames)
                                                .executes(context -> {
                                                    String name = StringArgumentType.getString(context, "Token Name");
                                                    if (deleteToken(name)) {
                                                        context.getSource().sendFeedback(() -> Text.of("Token '" + name + "' deleted!"), true);
                                                        return 1;
                                                    } else {
                                                        context.getSource().sendFeedback(() -> Text.of("Token not found!"), false);
                                                        return 0;
                                                    }
                                                }))))));
    }

    public static class ExpirationTimeArgumentType {

        public static ExpirationTimeArgumentType word() {
            return new ExpirationTimeArgumentType();
        }
        private boolean isValid(String input) {
            // The regex pattern for your requirements.
            String pattern = "^(\\d+h)?(\\d+d)?(\\d+y)?$";

            // Check if the input matches the pattern.
            //return input.matches(pattern);
            return input.equals("1h");
        }

        static CompletableFuture<Suggestions> suggestExpirationTimes(CommandContext<ServerCommandSource> serverCommandSourceCommandContext, SuggestionsBuilder builder) {
            try {
                // get the current input
                String input = StringArgumentType.getString(serverCommandSourceCommandContext, "Expiration Time (example: 1y3d4h -> 1 Year, 3 Days, 4 Hours)");


                // check if the input matches the pattern
                if (!input.equals("0")) {
                    if (input.matches("\\d+")) {
                        builder.suggest(input + "y");
                        builder.suggest(input + "d");
                        builder.suggest(input + "h");
                        suggestIntRange(builder, input, 0);
                    } else if (input.matches("\\d+y\\d+")) {
                        builder.suggest(input + "y");
                        builder.suggest(input + "d");
                        suggestIntRange(builder, input, 0);
                    } else if (input.matches("\\d+d\\d+")) {
                        builder.suggest(input + "d");
                        builder.suggest(input + "h");
                        suggestIntRange(builder, input, 0);
                    } else if (input.matches("\\d+y\\d+d\\d+")) {
                        builder.suggest(input + "h");
                        suggestIntRange(builder, input, 0);
                    } else if (input.matches("\\d+y")) {
                        suggestIntRange(builder, input, 1);
                    } else if (input.matches("\\d+d")) {
                        suggestIntRange(builder, input, 1);
                    } else if (input.matches("\\d+y\\d+d")) {
                        suggestIntRange(builder, input, 1);
                    }
                }
            } catch (IllegalArgumentException e) {
                suggestIntRange(builder, "", 0);
            }
            return builder.buildFuture();
        }

        private static void suggestIntRange(SuggestionsBuilder builder, String input, int min) {
            for (int i = min; i <= 9; i++) {
                builder.suggest(input + String.valueOf(i));
            }
        }
    }

    public static class DeleteTokenNameArgumentType {
        static CompletableFuture<Suggestions> suggestTokenNames(CommandContext<ServerCommandSource> serverCommandSourceCommandContext, SuggestionsBuilder builder) {
            String[] tokenNames = getTokenNames();

            for (String tokenName : tokenNames) {
                builder.suggest(tokenName);
            }
            return builder.buildFuture();
        }
    }
}
