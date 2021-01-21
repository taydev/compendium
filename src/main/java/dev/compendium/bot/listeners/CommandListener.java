package dev.compendium.bot.listeners;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import java.util.Arrays;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CommandListener extends ListenerAdapter {

    private final Logger LOGGER = LoggerFactory.getLogger(CommandListener.class);

    @Override
    public void onReady(@NotNull ReadyEvent e) {
        CompendiumBot.getInstance().onReady();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        if (e.getAuthor().isBot()) {
            return;
        }
        if (!e.getMessage().getContentRaw().startsWith(CompendiumBot.getInstance().getPrefix())) {
            return;
        }

        String[] args = e.getMessage().getContentRaw().substring(1).split(" ");
        String commandName = args[0];

        ICommand command = CompendiumBot.getInstance().getCommand(args[0]);
        if (command != null) {
            if (command.getCategory() == CommandCategory.DEVELOPER
                && !CompendiumBot.getInstance().isDeveloper(e.getAuthor().getId())) {
                return;
            }

            User sender = e.getAuthor();
            Message message = e.getMessage();
            MessageChannel channel = e.getChannel();
            args = Arrays.stream(args).skip(1).toArray(String[]::new);
            LOGGER.info("User {}#{} executed command {} in channel {} with arguments: {}",
                sender.getName(),
                sender.getDiscriminator(),
                commandName,
                channel.getName(),
                args);
            command.onCommand(sender, message, channel, commandName, args);
        }
    }
}
