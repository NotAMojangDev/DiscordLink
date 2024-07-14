package io.namd.discordlink.utils;

import org.bukkit.Bukkit;

public class Logger {

    public static void error(String message) {
        Bukkit.getLogger().severe(message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().warning(message);
    }

    public static void info(String message) {
        Bukkit.getLogger().info(message);
    }
}
