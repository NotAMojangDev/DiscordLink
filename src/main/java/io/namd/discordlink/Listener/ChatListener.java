package io.namd.discordlink.Listener;

import io.namd.discordlink.DiscordLink;
import io.namd.discordlink.utils.Color;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private final DiscordLink main;
    private final String messageFormat;
    private final String discordMessageFormat;

    public ChatListener(DiscordLink main) {
        this.main = main;
        this.messageFormat = main.getConf().getMCChatFormat();
        this.discordMessageFormat = main.getConf().getMCMessageFormat();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void chatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        event.setMessage(Color.colorizeAll(event.getMessage()));
        String message = event.getMessage();

        // Message format
        event.setFormat(chatFormat(player, message));

        main.getBot().sendMessage(
                discordMessageFormat
                        .replace("{PLAYER}", player.getName())
                        .replace("{MESSAGE}", ChatColor.stripColor(message))
        );

    }

    public String chatFormat(Player player, String message) {
        return Color.colorizeAll(messageFormat
                .replace("{PLAYER}", player.getName())
                .replace("{DISPLAYNAME}", player.getDisplayName())
                .replace("{MESSAGE}", message));

    }

}
