package dev.compendium.bot.commands.dnd;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.bot.utils.RollResults;
import dev.compendium.bot.utils.RollUtils;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class MultiRollCommand implements ICommand {

    @Override
    public String[] getAliases() {
        return new String[]{"rr", "mr"};
    }

    @Override
    public String getCommand() {
        return "multiroll";
    }

    @Override
    public String getDescription() {
        return "Rolls multiple sets of dice.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DND;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        if (args.length >= 2) {
            new Thread(() -> {
                try {
                    List<RollResults> resultsList = RollUtils.processRoll(sender, message, channel, command, args[1], Long.parseLong(args[0]));
                    Map<String, Long> totalLabelledRolls = new HashMap<>();
                    long totalRoll = 0;
                    if (resultsList != null) {
                        String rollTitle = (args.length > 2 ? Arrays.stream(args).skip(2).collect(Collectors.joining(" "))
                            : "Results");
                        StringBuilder finalRollString = new StringBuilder();
                        for (RollResults results : resultsList) {
                            finalRollString.append(results.getRollString().toString()).append(" = `")
                                .append(results.getTotalRoll()).append("`");
                            if (results.getTotalRoll() != results.getTrueResult()) {
                                finalRollString.append(" (").append(results.getTrueResult()).append(")");
                            }
                            finalRollString.append("\n");
                            for (Map.Entry<String, Long> entry : results.getLabelledRolls().entrySet()) {
                                if (totalLabelledRolls.containsKey(entry.getKey())) {
                                    long value = totalLabelledRolls.get(entry.getKey());
                                    totalLabelledRolls.put(entry.getKey(), value + entry.getValue());
                                } else {
                                    totalLabelledRolls.put(entry.getKey(), entry.getValue());
                                }
                            }
                            totalRoll += results.getTrueResult();
                        }
                        if (finalRollString.length() >= 1800) {
                            finalRollString = new StringBuilder(
                                "(input was too long for discord, here's the important stuff)\n");
                        }
                        for (Map.Entry<String, Long> entry : totalLabelledRolls.entrySet()) {
                            finalRollString.append("**").append(entry.getKey()).append(":** ").append(entry.getValue())
                                .append("\n");
                        }
                        finalRollString.append("**Total:** ").append(totalRoll);
                        channel.sendMessage(new MessageBuilder()
                            .setContent(sender.getAsMention())
                            .setEmbed(new EmbedBuilder()
                                .setTitle("Multiroll - " + rollTitle)
                                .setDescription(finalRollString.toString().trim())
                                .build())
                            .build()).queue();
                    }
                } catch (OutOfMemoryError ignored) {
                    channel.sendMessage("We ran out of memory :(").queue();
                } catch (NumberFormatException ignored) {
                    sendErrorMessage(channel, "Invalid syntax: `" + CompendiumBot.getInstance().getPrefix()
                        + command + " (number of rolls) (dice query) [roll name]");
                }
            }).start();
        } else {
            sendErrorMessage(channel, "Invalid syntax: `" + CompendiumBot.getInstance().getPrefix()
                + command + " (number of rolls) (dice query) [roll name]");
        }
    }
}
