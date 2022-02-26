package dev.compendium.bot.commands.developer;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class CurseRollCommand implements ICommand {
    @Override
    public String getCommand() {
        return "curseroll";
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
        if (!CompendiumBot.getInstance().isCurseRoll()) {
            CompendiumBot.getInstance().setCurseRoll(true);
            message.addReaction("✅").queue();
        } else {
            CompendiumBot.getInstance().setCurseRoll(false);
            message.addReaction("❌").queue();
        }
    }
}
