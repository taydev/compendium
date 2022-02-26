package dev.compendium.bot.commands.developer;

import com.google.gson.*;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.core.spell.Spell;
import dev.compendium.core.util.ParseUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.concurrent.ExecutionException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class ParseSpellCommand implements ICommand {

    @Override
    public String getCommand() {
        return "parsespell";
    }

    @Override
    public String getDescription() {
        return "Parses a spell from the Open5e API JSON format.";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DEVELOPER;
    }

    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        if (message.getAttachments().size() == 1) {
            Attachment attachment = message.getAttachments().get(0);
            channel.sendMessage("downloading " + attachment.getFileName()).queue();
            try {
                File file = attachment.downloadToFile().get();
                channel.sendMessage("parsing file").queue();
                FileReader fr = new FileReader(file);
                JsonObject spellJson = (JsonObject) JsonParser.parseReader(fr);
                channel.sendMessage("parsing item").queue();
                Spell spell = new Spell(spellJson);
                Gson gson = new GsonBuilder().create();
                channel.sendMessage("done?").queue();
                String spellContents = gson.toJson(spell);
                if (spellContents.length() > 2000) {
                    spellContents = spellContents.substring(0, 2000);
                }
                channel.sendMessage(spellContents).queue();
            } catch (InterruptedException | ExecutionException ignored) {
                channel.sendMessage("download fucked").queue();
            } catch (FileNotFoundException e) {
                channel.sendMessage("file fucked").queue();
            }
        }
    }
}
