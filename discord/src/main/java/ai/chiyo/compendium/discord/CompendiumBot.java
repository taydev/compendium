package ai.chiyo.compendium.discord;

import ai.chiyo.compendium.discord.command.ICommand;
import ai.chiyo.compendium.discord.listeners.CommandListener;
import ai.chiyo.compendium.discord.listeners.GeneralListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CompendiumBot {

    private static CompendiumBot instance;
    private static Logger logger;

    private JDA discordClient;

    private Set<ICommand> commands;

    public static void main(String[] args) {
        instance = new CompendiumBot();
        logger = LoggerFactory.getLogger(CompendiumBot.class);
        instance.initialise();
    }

    public static CompendiumBot getInstance() {
        return instance;
    }

    public static Logger getLogger() {
        return logger;
    }

    private void initialise() {
        // TODO: load configuration
        this.initialiseDiscordClient();
    }

    private void initialiseDiscordClient() {
        try {
            this.discordClient = JDABuilder.create(
                            GatewayIntent.DIRECT_MESSAGES,
                            GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_MEMBERS,
                            GatewayIntent.GUILD_MESSAGES,
                            GatewayIntent.GUILD_MESSAGE_REACTIONS,
                            GatewayIntent.GUILD_WEBHOOKS
                    ).addEventListeners(new GeneralListener(), new CommandListener())
                    .setAutoReconnect(true)
                    .setActivity(Activity.watching("the dice roll! | >help"))
                    .setToken("~~ insert token from configuration ~~")
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public void onReady() {
        this.commands = ConcurrentHashMap.newKeySet();
        // TODO: stuff
    }

    public Set<ICommand> getCommands() {
        return this.commands;
    }

    private void registerCommand(ICommand command) {
        this.getCommands().add(command);
        logger.info("Command registered - {}", command.getCommandName());
    }
}
