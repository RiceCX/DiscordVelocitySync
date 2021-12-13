package network.insurgence.velocitydiscordayncapi;

import com.velocitypowered.api.proxy.Player;
import network.insurgence.velocitydiscordsync.core.Token;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;


/**
 * This event is called when a player links their account successfully to Discord.
 */
public class DiscordLinkEvent {

    private final Token token;
    private final String snowflake;

    @Nullable
    private final Player player;

    public DiscordLinkEvent(Token token, String snowflake, @Nullable Player player) {
        this.token = token;
        this.snowflake = snowflake;
        this.player = player;
    }

    /**
     * @return The {@link Token} that's linked to this player.
     */
    public Token getToken() {
        return token;
    }

    /**
     * @return The Discord Snowflake of the player that's linked.
     */
    public String getSnowflake() {
        return snowflake;
    }

    /**
     * @return The {@link Player} that's linked to this token.
     */
    public Optional<Player> getPlayer() {
        return Optional.ofNullable(player);
    }
}
