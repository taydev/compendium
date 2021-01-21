package dev.compendium.bot.commands.creation;

import dev.compendium.bot.CompendiumBot;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.bot.tasks.Scheduler;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Creator;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SessionCommand extends ListenerAdapter implements ICommand {

    private final Map<String, String> pendingAgreementIds = new HashMap<>();

    public SessionCommand() {
        CompendiumBot.getInstance().getClient().addEventListener(this);
    }

    @Override
    public String getCommand() {
        return "session";
    }

    @Override
    public String getDescription() {
        return "Starts a homebrew creation session.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.CREATION;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        Creator creator = ElementRegistry.getInstance().getCreatorByUUID(sender.getId());
        if (creator == null) {
            creator = new Creator(sender.getId());
            ElementRegistry.getInstance().storeCreator(creator);
        }
        if (creator.isReadTerms()) {
            channel.sendMessage("Creation session feature has not been implemented yet.").queue();
        } else {
            channel.sendMessage(new EmbedBuilder()
                .setColor(Color.decode("#EF3E36"))
                .setTitle("Compendium - Terms of Service")
                .setDescription("The Creation Suite feature set of the Compendium Discord Bot allows for users to "
                    + "create their own Dungeons & Dragons 5th Edition content (also referred to as \"homebrew\") for "
                    + "use with the Compendium Discord Bot's campaign management features. In order for the content "
                    + "you create to be usable within the Bot's systems, you must agree to the Terms of Service. You "
                    + "can find those Terms of Service here: https://github.com/taydev/compendium/TERMS.md"
                    + "\n\nUpon reading and agreeing with these terms, simply react to this message to confirm."
                    + "This message will, otherwise, time out in 5 minutes.")
                .build()).queue((msg) -> {
                msg.addReaction("✅").queue();
                this.pendingAgreementIds.put(msg.getId(), sender.getId());
                Scheduler.delayTask(() -> {
                    if (this.pendingAgreementIds.containsKey(msg.getId())) {
                        channel.sendMessage(new EmbedBuilder()
                            .setColor(Color.decode("#660011"))
                            .setTitle("Compendium - Terms of Service")
                            .setDescription(
                                "Your Terms of Service agreement request has timed out. Please run the command "
                                    + "again to generate a new agreement message.")
                            .build()).queue();
                        this.pendingAgreementIds.remove(msg.getId());
                    }
                }, TimeUnit.MINUTES.toMillis(5));
            });
        }
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e) {
        if (this.pendingAgreementIds.containsKey(e.getMessageId())
            && this.pendingAgreementIds.get(e.getMessageId()).equals(e.getUserId())
            && e.getReactionEmote().getEmoji().equals("✅")) {
            Creator creator = ElementRegistry.getInstance().getCreatorByUUID(e.getUserId());
            if (creator != null) {
                creator.setReadTerms(true);
                ElementRegistry.getInstance().storeCreator(creator);
                e.getChannel().sendMessage(new EmbedBuilder()
                    .setColor(Color.decode("#00CC66"))
                    .setTitle("Compendium - Terms of Service")
                    .setDescription("Your Terms of Service agreement has been confirmed. Happy creating!")
                    .build()).queue();
                this.pendingAgreementIds.remove(e.getMessageId());
            }
        }
    }
}
