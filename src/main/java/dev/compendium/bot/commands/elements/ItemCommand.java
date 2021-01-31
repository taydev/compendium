package dev.compendium.bot.commands.elements;

import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Paginator;
import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.item.Item;
import dev.compendium.core.util.ElementUtils;
import java.awt.Color;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.internal.utils.tuple.Pair;

@SuppressWarnings("Duplicates")
public class ItemCommand extends ListenerAdapter implements ICommand {

    private final Map<String, Pair<Message, List<Item>>> pendingChoices = new HashMap<>();

    private final EventWaiter waiter;

    public ItemCommand() {
        this.waiter = new EventWaiter();
        CompendiumBot.getInstance().getClient().addEventListener(this.waiter);
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
            channel.sendMessage(new EmbedBuilder()
                .setColor(Color.decode("#990011"))
                .setTitle("Item - Invalid Usage")
                .setDescription("Syntax: `" + CompendiumBot.getInstance().getPrefix() + command + " (query)`")
                .build()).queue();
        } else {
            Message results = channel.sendMessage("Searching...").complete();
            String query = String.join(" ", args).replaceAll("-noAlt", "").replaceAll(" {2,}", " ").trim();
            List<Item> items = ElementRegistry.getInstance().findItemsByName(query, !Arrays.asList(args).contains("-noAlt"));
            if (items.size() == 0) {
                items = ElementRegistry.getInstance().findItemsByKeyword(query);
            }
            if (items.size() > 1) {
                this.pendingChoices.put(sender.getId(), Pair.of(results, items));
                Paginator.Builder builder = new Paginator.Builder();
                builder.showPageNumbers(true)
                    .setItemsPerPage(10)
                    .setTimeout(30, TimeUnit.SECONDS)
                    .waitOnSinglePage(true)
                    .setLeftRightText("p", "n")
                    .setEventWaiter(this.waiter)
                    .setUsers(sender)
                    .setColor(Color.decode("#c716c7"))
                    .setText("Multiple results were found with that query. Which one would you like to select?\n"
                        + "(Type the number to display that item, `n` to go to the next page, `p` to go to the previous page, "
                        + "or `c` to cancel)")
                    .setFinalAction((msg) -> {
                        if (this.pendingChoices.containsKey(sender.getId())) {
                            this.safeDelete(msg);
                            this.pendingChoices.remove(sender.getId());
                            channel.sendMessage("Search query timed out or was cancelled.").queue();
                        }
                    });
                for (Item item : items) {
                    builder.addItems("**" + (items.indexOf(item) + 1) + ".** " + item.getName()
                        + " - " + item.getSource().getAbbreviation());
                }
                Paginator paginator = builder.build();
                paginator.display(results);
                this.initialiseWait(sender, channel);
            } else if (items.size() == 1) {
                this.safeDelete(results);
                this.processItem(items.get(0), channel);
            } else {
                channel.sendMessage(new EmbedBuilder()
                    .setColor(Color.decode("#990011"))
                    .setTitle("Item - Error")
                    .setDescription("No potential items were found under the query `" + query + "`")
                    .build()).queue();
            }
        }
    }

    private boolean isValidInput(String input) {
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return input.equalsIgnoreCase("c");
        }
    }

    private void safeDelete(Message message) {
        try {
            message.delete().queue();
        } catch (InsufficientPermissionException ignored) {
        }
    }

    private void processItem(Item item, MessageChannel channel) {
        ElementUtils.sendItemSummary(item, channel);
    }

    private void initialiseWait(User sender, MessageChannel channel) {
        this.waiter.waitForEvent(MessageReceivedEvent.class,
            e -> this.isValidInput(e.getMessage().getContentRaw()) && e.getAuthor().getId().equals(sender.getId())
                && e.getChannel().getId().equals(channel.getId()),
            e -> {
                String id = e.getAuthor().getId();
                if (this.pendingChoices.containsKey(id)) {
                    Pair<Message, List<Item>> choices = this.pendingChoices.get(id);
                    String content = e.getMessage().getContentRaw();
                    try {
                        int choice = Integer.parseInt(content);
                        Item itemChoice = choices.getRight().get(choice - 1);
                        if (itemChoice != null) {
                            this.pendingChoices.remove(id);
                            this.processItem(itemChoice, channel);
                        }
                    } catch (NumberFormatException ignored) {
                        if (content.equalsIgnoreCase("c")) {
                            this.pendingChoices.remove(id);
                            e.getChannel().sendMessage("Request cancelled.").queue();
                        }
                    }
                    this.safeDelete(choices.getLeft());
                    this.safeDelete(e.getMessage());
                }
            });
    }
}
