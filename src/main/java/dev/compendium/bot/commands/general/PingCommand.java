package dev.compendium.bot.commands.general;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class PingCommand implements ICommand {

    @Override
    public String getCommand() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Returns the current ping.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.GENERAL;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        long ping = System.currentTimeMillis();
        channel.sendMessage("Rebounding...").queue(msg -> {
            msg.editMessage("Pong! \uD83C\uDFD3\nPing - " + (System.currentTimeMillis() - ping)
                + "ms\nGateway - " + CompendiumBot.getInstance().getClient().getGatewayPing() + "ms").queue();
        });
    }
}
