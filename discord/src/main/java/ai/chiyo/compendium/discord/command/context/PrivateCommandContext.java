package ai.chiyo.compendium.discord.command.context;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;

public class PrivateCommandContext extends CommandContext {

    private final PrivateChannel channel;

    public PrivateCommandContext(User user, Message message, PrivateChannel channel, String command, String[] args) {
        super(user, message, command, args);
        this.channel = channel;
    }

    public PrivateChannel getChannel() {
        return this.channel;
    }
}
