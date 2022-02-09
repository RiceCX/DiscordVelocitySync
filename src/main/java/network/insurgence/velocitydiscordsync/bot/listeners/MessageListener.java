package network.insurgence.velocitydiscordsync.bot.listeners;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordayncapi.token.Token;
import network.insurgence.velocitydiscordsync.core.TokenHandler;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.awt.*;
import java.time.Instant;
import java.util.Optional;

public class MessageListener extends ListenerAdapter {

    private final Logger logger = VelocityDiscordSync.getLogger();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()
                || !event.getMessage().getChannelType().equals(ChannelType.TEXT)) {
            return;
        }
        String message = event.getMessage().getContentRaw();
        Optional<Token> tokenOptional = TokenHandler.fromString(message);
        if (tokenOptional.isPresent()) {
            String IGN = TokenHandler.getUsername(tokenOptional.get().getUuid());
            TokenHandler.linkToken(tokenOptional.get(), event.getAuthor().getId());
            if (event.getMember() != null)
                renameMember(event.getMember(), IGN);

            event.getChannel().sendMessageEmbeds(generateEmbed(IGN)).queue(
                    msg -> logger.info("Successfully linked {} to {}", event.getAuthor().getAsTag(), tokenOptional.get().getUuid()),
                    err -> logger.error("Could not send embed: " + err.getMessage()));
        } else {
            // Invalid token, just delete their message.
            if (event.getChannel().getId().equals("904755643221704735")) {
                event.getMessage().delete().queue();
            }
        }
    }

    private void renameMember(@NotNull Member member, String IGN) {
        try {
            member.modifyNickname(IGN).queue();
        } catch (InsufficientPermissionException e) {
            logger.error("Could not rename member to due lack of permissions: " + e.getPermission());
        }
    }

    private MessageEmbed generateEmbed(String IGN) {
        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("Your Discord account has been linked to Minecraft.", "https://junglerealms.com")
                .setColor(Color.decode("#11ff00"))
                .setDescription(String.format(":white_check_mark: You have successfully linked to **%s**", IGN))
                .setFooter("JungleRealms", "https://i.imgur.com/Mnv0nmT.png")
                .setTimestamp(Instant.now());

        return builder.build();
    }
}
