package ai.chiyo.compendium.discord.command;

import ai.chiyo.compendium.discord.command.context.GuildCommandContext;
import ai.chiyo.compendium.discord.command.context.PrivateCommandContext;

// I know that people should be using Discord's proprietary "slash-command" functionality already, but... nah, I'm good.
// Slash Commands suck for in-dev projects. I'll think about it when I'm done.
public interface ICommand {
    String getCommandName();
    default String[] getCommandAliases() {
        return new String[]{};
    }
    String getCommandDescription();
    void onGuildCommand(GuildCommandContext ctx);
    void onPrivateCommand(PrivateCommandContext ctx);
    default boolean deleteMessage() {
        return false;
    }
}
