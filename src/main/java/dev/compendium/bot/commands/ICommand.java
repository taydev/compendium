package dev.compendium.bot.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public interface ICommand {

    String getCommand();

    String getDescription();

    CommandCategory getCategory();

    void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args);

    default String[] getAliases() {
        return new String[]{};
    }
}
