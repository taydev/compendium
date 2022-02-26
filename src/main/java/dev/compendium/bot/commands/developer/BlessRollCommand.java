package dev.compendium.bot.commands.developer;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class BlessRollCommand implements ICommand {
    @Override
    public String getCommand() {
        return "blessroll";
    }

    @Override
    public String getDescription() {
        return "null";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        if (!CompendiumBot.getInstance().isBlessRoll()) {
            CompendiumBot.getInstance().setBlessRoll(true);
            message.addReaction("✅").queue();
        } else {
            CompendiumBot.getInstance().setBlessRoll(false);
            message.addReaction("❌").queue();
        }
    }
}
