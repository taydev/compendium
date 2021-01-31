package dev.compendium.bot.commands;

import dev.compendium.core.util.ElementUtils;
import java.awt.Color;
import net.dv8tion.jda.api.EmbedBuilder;
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

    default void sendErrorMessage(MessageChannel channel, String error) {
        channel.sendMessage(new EmbedBuilder()
            .setColor(Color.decode("#990011"))
            .setTitle(ElementUtils.capitalise(this.getCommand()) + " - Error")
            .setDescription(error)
            .build())
            .queue();
    }
}
