package io.namd.discordlink.Discord;

import io.namd.discordlink.DiscordLink;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.enginehub.squirrelid.Profile;
import org.enginehub.squirrelid.resolver.HttpRepositoryService;
import org.enginehub.squirrelid.resolver.ProfileService;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

// Failed to make this file work

public class WhitelistListener extends ListenerAdapter {

    private final ProfileService resolver;
    private final DiscordLink main;

    public WhitelistListener(DiscordLink main) {
        resolver = HttpRepositoryService.forMinecraft();
        this.main = main;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        if (!event.getChannel().getId().equals(main.getConf().getWhitelistChannel())) {
            return;
        };

        if (event.isWebhookMessage()) return;

        User user = event.getAuthor();

        if (user.isBot()) return;

        if (user.isSystem()) return;

        Member member = event.getMember();

        if (member == null) return;
        if (!event.getMessage().getContentRaw().matches("^[a-zA-Z0-9_]{3,16}$")) {
            return;
        }

        Profile profile = null;

        try {
            profile = resolver.findByName(event.getMessage().getContentRaw());
        } catch (IOException | InterruptedException exception) {
            event.getMessage()
                    .reply("There was an Error Registering this username\n" +
                            "Please report this to <@566630884623777822>\n" +
                            "Or just wait for him to see this\n").queue();
            return;
        }

        if (profile == null) {
            event.getMessage().reply("This Username was not found\n" +
                    "Please try again later or check for any mistakes").queue();
            return;
        }

        OfflinePlayer player = Bukkit.getOfflinePlayer(profile.getUniqueId());
        if (!Bukkit.getWhitelistedPlayers().contains(player)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("whitelist add %s", profile.getName()));
            event.getMessage().reply("Whitelisted Successfully!").queue();
        } else {
            event.getMessage().reply("That account is already whitelisted!").queue();
        }
    }

}
