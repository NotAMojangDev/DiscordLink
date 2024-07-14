package io.namd.discordlink.utils;

import io.namd.discordlink.DiscordLink;
import net.dv8tion.jda.api.entities.Member;
import org.bukkit.Bukkit;

public class Message {

    private final Config config;

    public Message(DiscordLink main) {
        this.config = main.getConf();
    }

    public void broadcastMessage(Member author, String message) {
        String format = config.getDCMessageFormat()
                .replace("{USER}", author.getUser().getName())
                .replace("{MESSAGE}", message);
        Bukkit.broadcastMessage(format);
    }

}
