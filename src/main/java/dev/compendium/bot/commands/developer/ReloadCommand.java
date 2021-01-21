package dev.compendium.bot.commands.developer;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class ReloadCommand implements ICommand {

    @Override
    public String getCommand() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads the bot's commands.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        channel.sendMessage("Reloading...").queue();
        CompendiumBot.getInstance().reloadCommands();
        channel.sendMessage("Reloaded.").queue();
    }
}
