package ai.chiyo.compendium.discord.listeners;

import ai.chiyo.compendium.discord.CompendiumBot;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GeneralListener extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent e) {
        // Honestly, is this here to give actual metrics or is this just to shut up the NotNull warning? We'll never know.
        CompendiumBot.getLogger().info("Connection to Discord established! Current guild count at time of connection: {}", e.getGuildTotalCount());
        CompendiumBot.getInstance().onReady();
    }
}
