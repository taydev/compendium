package ai.chiyo.compendium.discord.command.context;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildMessageChannel;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class GuildCommandContext extends CommandContext {

  private final Guild guild;
  private final GuildMessageChannel channel;

  public GuildCommandContext(User user, Message message, Guild guild, GuildMessageChannel channel,
      String command, String[] args) {
    super(user, message, command, args);
    this.guild = guild;
    this.channel = channel;
  }

  public Guild getGuild() {
    return this.guild;
  }

  public GuildMessageChannel getChannel() {
    return this.channel;
  }

  public void deleteCommandMessage() {
    this.getMessage().delete().queue();
  }
}
