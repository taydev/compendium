package ai.chiyo.compendium.discord.command.context;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

public class CommandContext {

  private final User user;
  private final Message message;
  private final String command;
  private final String[] args;

  public CommandContext(User user, Message message, String command, String[] args) {
    this.user = user;
    this.message = message;
    this.command = command;
    this.args = args;
  }

  public User getUser() {
    return this.user;
  }

  public Message getMessage() {
    return this.message;
  }

  public String getCommandString() {
    return this.command;
  }

  public String[] getArguments() {
    return this.args;
  }
}
