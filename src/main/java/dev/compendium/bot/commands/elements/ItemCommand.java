package dev.compendium.bot.commands.elements;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.bot.tasks.Scheduler;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.item.Item;
import dev.compendium.core.util.ElementUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ItemCommand extends ListenerAdapter implements ICommand {

    private final Map<String, List<Item>> pendingChoices = new HashMap<>();

    public ItemCommand() {
        CompendiumBot.getInstance().getClient().addEventListener(this);
    }

    @Override
    public String getCommand() {
        return "item";
    }

    @Override
    public String getDescription() {
        return "Shows the listing of an item based on the entered query.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.ELEMENTS;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        if (args.length == 0) {
            channel.sendMessage("Invalid usage.").queue();
        } else {
            String query = String.join(" ", args);
            List<Item> items = ElementRegistry.getInstance().findItemsByName(query);
            if (items.size() == 0) {
                items = ElementRegistry.getInstance().findItemsByKeyword(query);
            }
            if (items.size() > 1) {
                List<Item> tempItems = items.stream().limit(10).collect(Collectors.toList());
                StringBuilder tempItemDesc = new StringBuilder();
                int index = 1;
                for (Item item : tempItems) {
                    tempItemDesc.append("\n**").append(index).append("** - ")
                        .append(item.getName());
                    index++;
                }
                this.pendingChoices.put(sender.getId(), items);
                channel.sendMessage(new EmbedBuilder()
                    .setTitle("Search Results - " + query)
                    .setDescription("Multiple results were found with that query. Which one would you like to select? "
                        + "(Type the number to display that item, or type `c` to cancel.)\n" + tempItemDesc.toString()
                        .trim())
                    .build()).queue(msg -> Scheduler.delayTask((() -> {
                    message.delete().queue();
                    this.pendingChoices.remove(sender.getId());
                    channel.sendMessage("Search query timed out.").queue();
                }), TimeUnit.SECONDS.toMillis(15)));
            } else {
                this.processItem(items.get(0), channel);
            }
        }
    }

    private void processItem(Item item, MessageChannel channel) {
        channel.sendMessage(ElementUtils.createItemSummary(item).build()).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {
        String id = e.getAuthor().getId();
        if (this.pendingChoices.containsKey(id)) {
            try {
                if (e.getMessage().getContentRaw().equalsIgnoreCase("c")) {
                    this.pendingChoices.remove(id);
                    e.getChannel().sendMessage("Request cancelled.").queue();
                }
                int choice = Integer.parseInt(e.getMessage().getContentRaw());
                Item itemChoice = this.pendingChoices.get(id).get(choice - 1);
                if (itemChoice != null) {
                    this.pendingChoices.remove(id);
                    this.processItem(itemChoice, e.getChannel());
                }
            } catch (NumberFormatException ignored) {
            }
        }
    }
}
