package dev.namd.discordlink.Listener;

import dev.namd.discordlink.DiscordLink;
import dev.namd.discordlink.Discord.Bot;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DeathListener implements Listener {

    private final DiscordLink main;

    public DeathListener(DiscordLink main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        main.getBot().sendEventMessage(event.getEntity(), Bot.Action.DEATH, ChatColor.stripColor(event.getDeathMessage()));
    }

}
