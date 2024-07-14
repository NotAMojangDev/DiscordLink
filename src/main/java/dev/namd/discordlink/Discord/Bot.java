package dev.namd.discordlink.Discord;

import dev.namd.discordlink.DiscordLink;
import dev.namd.discordlink.utils.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Objects;

public class Bot {

    private final JDA bot;
    private final DiscordLink main;

    public Bot(String token, DiscordLink main) {
            JDABuilder builder = JDABuilder.createLight(token);
            builder.setEnabledIntents(
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.MESSAGE_CONTENT,
                    GatewayIntent.GUILD_WEBHOOKS,
                    GatewayIntent.DIRECT_MESSAGES,
                    GatewayIntent.GUILD_MESSAGE_POLLS
            );
            builder.setActivity(main.getConf().getActivity());
            builder.setAutoReconnect(true);
            builder.setStatus(main.getConf().getStatus());
            builder.addEventListeners(new DiscordListener(main));
            /*builder.addEventListeners(new WhitelistListener(main));*/


            this.bot = builder.build();
            this.main = main;
            sendMessage("🟢 Server Started!");
    }

    public void shutdown() {
        sendMessage("🔴 Server Stopped!");
        this.bot.shutdownNow();
    }

    public void sendMessage(String message) {
        String MCChannelID = main.getConf().getMCChannel();
        try {
            Objects.requireNonNull(bot.getTextChannelById(MCChannelID)).sendMessage(message).queue();
        } catch (NullPointerException e) {
            Logger.warn("Discord Chat Channel does not exist please double check the config!");
        }
    }

    public void sendEventMessage(Player player, Action action, @Nullable String message) {
        String MCChannelID = main.getConf().getMCChannel();
        MessageCreateAction Message = Objects.requireNonNull(bot.getTextChannelById(MCChannelID)).sendMessage("");
        EmbedBuilder embedBuilder = new EmbedBuilder();
        boolean isJoin = (action == Action.JOIN);
        boolean isLeave = (action == Action.LEAVE);


        /*embedBuilder.setTitle((isJoin) ?
                main.getConf().getMCJoinMessage(player) :
                main.getConf().getMCLeaveMessage(player));*/
        embedBuilder.setAuthor((isJoin) ?
                main.getConf().getMCJoinMessage(player) :
                        (isLeave) ? main.getConf().getMCLeaveMessage(player) : message,
                "https://crafatar.com/avatars/" + player.getUniqueId() + ".png?size=128&overlay",
                "https://crafatar.com/avatars/" + player.getUniqueId() + ".png?size=128&overlay"
        );

        embedBuilder.setColor((isJoin) ? Color.GREEN : Color.RED);

        Message.addEmbeds(embedBuilder.build());
        Message.queue();
    }

    public enum Action {
        JOIN, LEAVE, DEATH;
    }
}
