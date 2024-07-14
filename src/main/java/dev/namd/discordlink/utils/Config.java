package dev.namd.discordlink.utils;

import dev.namd.discordlink.DiscordLink;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class Config {

    private final YamlConfiguration config;
    private final YamlConfiguration messages;
    private final File dataFolder;

    public Config(DiscordLink main) {
        config = (YamlConfiguration) main.getConfig();
        messages = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "messages.yml"));
        dataFolder = main.getDataFolder();
    }

    public String getMCChannel() {
        return config.getString("mc-chat-channel");
    }

    public String getWhitelistChannel() {
        return config.getString("whitelist-channel");
    }

    /*
     public String getMCInfo() {
         return config.getString("info-channel");
     }

     public String getInfoMessageID() {
         File dataFile = new File(dataFolder, "data.yml");

         if (!dataFile.exists()) return null;

         YamlConfiguration dataConf = YamlConfiguration.loadConfiguration(dataFile);

         return dataConf.getString("info-message-id-do-not-touch");
     }
    */

    public void saveInfoMessage(String messageID) {
        File dataFile = new File(dataFolder, "data.yml");

        if (!dataFile.exists()) {
            try {
                boolean ignored = dataFile.createNewFile();
            } catch (IOException e) {
                return;
            }
        };

        YamlConfiguration dataConf = YamlConfiguration.loadConfiguration(dataFile);

        dataConf.set("info-message-id-do-not-touch", messageID);
    }

    public String getDCMessageFormat() {
        return Color.colorizeAll(messages.getString("dc-message-format"));
    }

    public String getMCChatFormat() {
        return messages.getString("chat-format");
    }

    public String getMCMessageFormat() {
        return messages.getString("mc-message-format");
    }

    public MCMessageType getMCMessageType() {
        return MCMessageType.valueOf(messages.getString("mc-message-type"));
    }

    public String getMCJoinMessage(Player player) {
        try {
            return Objects.requireNonNull(messages.getString("mc-player-join")).replace("{PLAYER}", player.getName());
        } catch (NullPointerException e) {
            return player.getName() + "joined the server";
        }
    }

    public String getMCLeaveMessage(Player player) {
        try {
            return Objects.requireNonNull(messages.getString("mc-player-leave")).replace("{PLAYER}", player.getName());
        } catch (NullPointerException e) {
            return player.getName() + "left the server";
        }
    }

    public Activity getActivity() {
        boolean isActive = config.getBoolean("bot.activity");
        if (!isActive) return null;

        Activity activity;

        String activityType = config.getString("bot.activity-type");
        String activityText = config.getString("bot.activity-text");

        if (activityType == null || activityText == null) return null;

        switch (activityType.toLowerCase()) {
            case "watching":
                activity = Activity.watching(activityText);
                break;
            case "playing":
                activity = Activity.playing(activityText);
                break;
            case "competing":
                activity = Activity.competing(activityText);
                break;
            case "streaming":
                String streamingLink = config.getString("bot.streaming-link");
                activity = Activity.streaming(activityText, streamingLink);
                break;
            case "listening":
                activity = Activity.listening(activityText);
                break;
            case "custom":
                activity = Activity.customStatus(activityText);
                break;
            default:
                activity = Activity.playing("Minecraft");
        }

        return activity;
    }

    public OnlineStatus getStatus() {
        String Online = config.getString("bot.status");

        if (Online == null) return OnlineStatus.ONLINE;

        OnlineStatus status;
        try {
            status = OnlineStatus.valueOf(Online.toUpperCase());
        } catch (IllegalArgumentException e) {
            status = OnlineStatus.ONLINE;
        }

        return (status == OnlineStatus.UNKNOWN) ? OnlineStatus.ONLINE : status;
    }

    public enum MCMessageType {
        CUSTOM_HEAD,
        BOT;
    }
}
