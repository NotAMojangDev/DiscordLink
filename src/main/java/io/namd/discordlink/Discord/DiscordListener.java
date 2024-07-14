package io.namd.discordlink.Discord;

import io.namd.discordlink.DiscordLink;
import io.namd.discordlink.utils.Config;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DiscordListener extends ListenerAdapter {

    private final DiscordLink main;
    private final Config config;

    public DiscordListener(DiscordLink main) {
        this.main = main;
        this.config = main.getConf();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (!event.isFromGuild()) return;

        if (event.isWebhookMessage()) return;

        if (!event.getChannel().getId().equals(config.getMCChannel())) {
            return;
        };

        User user = event.getAuthor();

        if (user.isBot()) return;
        if (user.isSystem()) return;

        if (event.getMessage().getContentRaw().equalsIgnoreCase("playerlist")) {
            if (Bukkit.getOnlinePlayers().isEmpty()) {
                event.getMessage().reply("There are no players online!").queue();
                return;
            }

            StringBuilder strBuilder = new StringBuilder();

            for (Player player : Bukkit.getOnlinePlayers()) {
                strBuilder.append("> ").append(player.getName()).append("\n");
            }

            event.getMessage().reply("Online Players:\n" + strBuilder).queue();
            return;
        }

        Member member = event.getMember();

        if (member == null) return;

        Bukkit.getLogger().info("Got to this point");

        main.getMessenger().broadcastMessage(member, event.getMessage().getContentRaw());

    }


}
