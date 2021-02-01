package dev.compendium.bot.commands.dnd;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.bot.utils.RollResults;
import dev.compendium.bot.utils.RollUtils;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

// have to suppress dupe code because intellij doesn't like my min/max methods
// spaghetti ahead

public class RollCommand implements ICommand {

    @Override
    public String getCommand() {
        return "roll";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"r", "dice", "die", "d"};
    }

    @Override
    public String getDescription() {
        return "Roll the dice!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DND;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        if (args.length >= 1) {
            new Thread(() -> {
                try {
                    List<RollResults> resultsList = RollUtils.processRoll(sender, message, channel, command, args[0], 1);
                    if (resultsList != null) {
                        RollResults results = resultsList.get(0);
                        String rollTitle = (args.length > 1 ? Arrays.stream(args).skip(1).collect(Collectors.joining(" "))
                            : "Results");
                        StringBuilder finalRollString = new StringBuilder();
                        finalRollString.append("**Input:** ").append(results.getRollString().toString());
                        if (finalRollString.length() >= 1000) {
                            finalRollString = new StringBuilder("(input was too long for discord, here's the important stuff)");
                        }
                        for (Map.Entry<String, Long> entry : results.getLabelledRolls().entrySet()) {
                            finalRollString.append("\n**").append(entry.getKey()).append(":** ").append(entry.getValue());
                        }
                        finalRollString.append("\n**Total:** ").append(results.getTotalRoll());
                        if (results.getTotalRoll() != results.getTrueResult()) {
                            finalRollString.append(" (").append(results.getTrueResult()).append(")");
                        }
                        channel.sendMessage(new MessageBuilder()
                            .setContent(sender.getAsMention())
                            .setEmbed(new EmbedBuilder()
                                .setTitle("Roll - " + rollTitle)
                                .setDescription(finalRollString.toString().trim())
                                .build())
                            .build()).queue();
                    }
                } catch (OutOfMemoryError e) {
                    channel.sendMessage("We ran out of memory :(").queue();
                }
            }).start();
        } else {
            sendErrorMessage(channel, "Invalid syntax: `" + CompendiumBot.getInstance().getPrefix()
                + command + " (dice query) [roll name]");
        }
    }
}
