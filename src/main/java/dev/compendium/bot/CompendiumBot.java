package dev.compendium.bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.compendium.bot.commands.ICommand;
import dev.compendium.bot.commands.creation.SessionCommand;
import dev.compendium.bot.commands.developer.*;
import dev.compendium.bot.commands.dnd.MultiRollCommand;
import dev.compendium.bot.commands.dnd.RollCommand;
import dev.compendium.bot.commands.elements.BackgroundCommand;
import dev.compendium.bot.commands.elements.ItemCommand;
import dev.compendium.bot.commands.general.PingCommand;
import dev.compendium.bot.listeners.CommandListener;
import dev.compendium.core.ElementRegistry;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompendiumBot {

    private final static Logger LOGGER = LoggerFactory.getLogger("Compendium");
    private static CompendiumBot instance;

    private JsonObject configuration;
    private JDA client;

    private Set<ICommand> commands;

    private boolean curseRoll;
    private boolean blessRoll;

    public static void main(String[] args) {
        instance = new CompendiumBot();
        instance.loadConfiguration();
    }

    public static CompendiumBot getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    //region Configuration
    private void loadConfiguration() {
        File configurationFile = new File("config.json");
        try {
            if (configurationFile.exists()) {
                FileReader fr = new FileReader(configurationFile);
                this.setConfiguration((JsonObject) JsonParser.parseReader(fr));
                if (this.getConfiguration().has("token") && this.getConfiguration().has("prefix")) {
                    LOGGER.info("Config format valid. Loading Compendium...");
                    this.initialise();
                }
            } else {
                FileWriter fw = new FileWriter(configurationFile);
                fw.write(this.getDefaultConfiguration());
                fw.flush();
                fw.close();
                LOGGER.info("Default configuration has been written. Please enter your Discord Bot token in the file "
                    + "to start.");
            }
        } catch (IOException e) {
            LOGGER.error("An error has occurred while attempting to save the default configuration file.", e);
        }
    }

    private JsonObject getConfiguration() {
        return this.configuration;
    }

    private void setConfiguration(JsonObject configuration) {
        this.configuration = configuration;
    }

    private String getDefaultConfiguration() {
        JsonObject object = new JsonObject();
        object.addProperty("token", "ENTER-DISCORD-TOKEN-HERE");
        object.addProperty("prefix", "=");
        object.addProperty("dev_id", "277385484919898112");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(object);
    }

    public boolean isDeveloper(String id) {
        return this.getConfiguration().get("dev_id").getAsString().equals(id);
    }

    public String getPrefix() {
        return this.getConfiguration().get("prefix").getAsString();
    }
    //endregion

    //region Discord
    private void initialise() {
        try {
            this.setClient(JDABuilder.createDefault(this.getConfiguration().get("token").getAsString())
                .setActivity(Activity.watching("from the Disconnected Plane"))
                .setAutoReconnect(true)
                .addEventListeners(new CommandListener())
                .build());
        } catch (LoginException e) {
            LOGGER.error("An error has occurred while attempting to connect to Discord.");
        }
    }

    public JDA getClient() {
        return this.client;
    }

    private void setClient(JDA client) {
        this.client = client;
    }

    public void onReady() {
        LOGGER.info("Discord connection initialised! Registering commands...");
        this.registerCommands();
        LOGGER.info("Commands registered. Initialising element registry...");
        ElementRegistry.getInstance();
        LOGGER.info("Element registry initialised. Happy adventuring!");
        this.setBlessRoll(false);
        this.setCurseRoll(false);
    }
    //endregion

    //region Commands
    public Set<ICommand> getCommands() {
        return this.commands;
    }

    public void registerCommand(ICommand command) {
        this.getCommands().add(command);
        LOGGER.info("Registered command - {}", command.getCommand());
    }

    public void registerCommands() {
        this.commands = new HashSet<>();
        //--
        this.registerCommand(new SessionCommand());
        //--
        this.registerCommand(new BlessRollCommand());
        this.registerCommand(new CurseRollCommand());
        this.registerCommand(new DebugViewCommand());
        this.registerCommand(new ParseSpellCommand());
        this.registerCommand(new ReloadCommand());
        //--
        this.registerCommand(new MultiRollCommand());
        this.registerCommand(new RollCommand());
        //--
        this.registerCommand(new BackgroundCommand());
        this.registerCommand(new ItemCommand());
        //--
        this.registerCommand(new PingCommand());
    }

    public void reloadCommands() {
        this.getCommands().clear();
        this.registerCommands();
    }

    public ICommand getCommand(String commandName) {
        for (ICommand command : this.getCommands()) {
            if (command.getCommand().equalsIgnoreCase(commandName)
                || Arrays.stream(command.getAliases()).map(String::toLowerCase).collect(Collectors.toList())
                .contains(commandName.toLowerCase())) {
                return command;
            }
        }
        return null;
    }
    //endregion

    public boolean isCurseRoll() {
        return this.curseRoll;
    }

    public void setCurseRoll(boolean curseRoll) {
        this.curseRoll = curseRoll;
    }

    public boolean isBlessRoll() {
        return this.blessRoll;
    }

    public void setBlessRoll(boolean blessRoll) {
        this.blessRoll = blessRoll;
    }
}
