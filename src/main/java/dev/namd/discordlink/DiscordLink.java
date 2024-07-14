package dev.namd.discordlink;

import dev.namd.discordlink.Listener.ChatListener;
import dev.namd.discordlink.Listener.JoinLeaveListener;
import dev.namd.discordlink.utils.Message;
import dev.namd.discordlink.Discord.Bot;
import dev.namd.discordlink.Listener.DeathListener;
import dev.namd.discordlink.utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class DiscordLink extends JavaPlugin {

    private Bot discordBot;
    private Config conf;
    private Message messenger;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        saveResource("messages.yml", false);
        ReloadConfig();
        YamlConfiguration config = (YamlConfiguration) getConfig();
        this.discordBot = new Bot(config.getString("bot-token"), this);
        this.messenger = new Message(this);

        Bukkit.getPluginManager().registerEvents(new ChatListener(this), this);
        Bukkit.getPluginManager().registerEvents(new JoinLeaveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new DeathListener(this), this);

    }

    @Override
    public void onDisable() {
        discordBot.shutdown();
    }

    public void ReloadConfig() {
        this.reloadConfig();
        conf = new Config(this);
    }

    public Bot getBot() {
        return discordBot;
    }

    public Config getConf() { return conf; }

    public Message getMessenger() { return messenger; }

    public List<Player> getOnlinePlayers() {
        return new ArrayList<>(Bukkit.getOnlinePlayers());
    }
}
