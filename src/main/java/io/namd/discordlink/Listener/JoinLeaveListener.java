package io.namd.discordlink.Listener;

import io.namd.discordlink.Discord.Bot;
import io.namd.discordlink.DiscordLink;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveListener implements Listener {

    private final DiscordLink main;

    public JoinLeaveListener(DiscordLink main) {
        this.main = main;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent event) {
        main.getBot().sendEventMessage(event.getPlayer(), Bot.Action.JOIN, null);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent event) {
        main.getBot().sendEventMessage(event.getPlayer(), Bot.Action.LEAVE, null);
    }

}
