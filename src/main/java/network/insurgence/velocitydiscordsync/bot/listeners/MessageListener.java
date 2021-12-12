package network.insurgence.velocitydiscordsync.bot.listeners;

import com.velocitypowered.api.proxy.Player;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.bot.SyncBot;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.core.TokenHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.awt.*;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public class MessageListener extends ListenerAdapter {

    private final Logger logger = VelocityDiscordSync.getLogger();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        UUID uuid = TokenHandler.tokenCache.getIfPresent(event.getMessage().getContentRaw());
        if (uuid != null) {
            Optional<Player> player = VelocityDiscordSync.getInstance().getServer().getPlayer(uuid);
            String IGN = player.isPresent() ? player.get().getUsername() : "N/A";
            event.getChannel().sendMessageEmbeds(generateEmbed(IGN)).queue();

            TokenHandler.tokenCache.invalidate(event.getMessage().getContentRaw());
            // Rename the member that sent the message
            if (player.isPresent() && event.getMember() != null) {
                try {
                    event.getMember().modifyNickname(String.format(event.getMember().getEffectiveName() + " [%s]", IGN)).queue();
                } catch (InsufficientPermissionException e) {
                    logger.error("Could not rename member to due lack of permissions: " + e.getPermission());
                }
            }
        }
    }

    private MessageEmbed generateEmbed(String IGN) {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Discord Sync", "https://junglerealms.com")
                .setAuthor("JungleRealms Proxy", "https://i.imgur.com/Mnv0nmT.png", "https://i.imgur.com/Mnv0nmT.png")
                .setColor(Color.decode("#11ff00"))
                .setDescription(String.format("You have successfully linked to **%s**", IGN))
                .setFooter("JungleRealms", "https://i.imgur.com/Mnv0nmT.png")
                .setThumbnail("https://i.imgur.com/Mnv0nmT.png")
                .setTimestamp(Instant.now());

        return builder.build();
    }
}
