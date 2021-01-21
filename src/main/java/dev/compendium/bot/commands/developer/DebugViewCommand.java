package dev.compendium.bot.commands.developer;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.core.ElementRegistry;
import java.util.Arrays;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class DebugViewCommand implements ICommand {

    private final TextSearchOptions CASE_INSENSITIVE = new TextSearchOptions().caseSensitive(false)
        .diacriticSensitive(false);

    @Override
    public String getCommand() {
        return "debugview";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"dbv"};
    }

    @Override
    public String getDescription() {
        return "Debug view.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        if (args.length >= 2) {
            String label = args[0];
            String query = Arrays.stream(args).skip(1).collect(Collectors.joining(" "));
            channel.sendMessage(ElementRegistry.getInstance().getDatabase().getCollection(label)
                .find(Filters.text(query, CASE_INSENSITIVE)).first().toString()).queue();
        } else {
            channel.sendMessage("invalid").queue();
        }
    }
}
