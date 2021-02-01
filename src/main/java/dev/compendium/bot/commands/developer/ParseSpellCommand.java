package dev.compendium.bot.commands.developer;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
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
                JsonArray spells = (JsonArray) JsonParser.parseReader(fr);
                channel.sendMessage("first 100 chars: " + spells.toString().substring(0, 100)).queue();
                long time = System.currentTimeMillis();
                channel.sendMessage("parsing items").queue();
                int parsed = 0;
                for (JsonElement spell : spells) {
                    JsonObject jsonObject = (JsonObject) spell;
                    ParseUtils.parseSpell(jsonObject);
                    parsed++;
                }
                channel.sendMessage("parsed " + parsed + " spells from " + spells.size() + " objects in "
                    + (System.currentTimeMillis() - time) + "ms").queue();
            } catch (InterruptedException | ExecutionException ignored) {
                channel.sendMessage("download fucked").queue();
            } catch (FileNotFoundException e) {
                channel.sendMessage("file fucked").queue();
            }
        }
    }
}
