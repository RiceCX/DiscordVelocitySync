package network.insurgence.velocitydiscordsync.core;

import com.google.common.cache.*;
import com.velocitypowered.api.proxy.Player;
import network.insurgence.velocitydiscordayncapi.DiscordLinkEvent;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.database.DatabaseManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class TokenHandler {


    private static final Logger logger = VelocityDiscordSync.getLogger();

    /**
     * The cache of tokens mapped by the PIN -> UUID of the player.
     */
    private static final Cache<Token, UUID> tokenCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).removalListener(new TokenExpireHandler()).maximumSize(1000).build();

    /**
     * Generates a new token for the given UUID.
     * This will also add the token to the cache.
     * @param uuid The UUID of the player.
     * @return The generated token.
     */
    public static Token generate(UUID uuid) {
        if(canGenerate(uuid) != null) return null;

        Token token = Token.generate(uuid);
        tokenCache.put(token, uuid);
        return token;
    }

    public static String getUsername(UUID uuid) {
        String ign;

        Optional<Player> player = VelocityDiscordSync.getInstance().getServer().getPlayer(uuid);

        if(player.isPresent())
            ign = player.get().getUsername();
        else ign = "N/A";

        return ign;
    }

    /**
     * Links the given token to the given UUID and saves it to the database.
     * @param token The token to link.
     * @param snowflake The Discord snowflake of the player.
     */
    public static void linkToken(Token token, String snowflake) {
        DatabaseManager.getSQLUtils().executeUpdate("INSERT INTO linked_users (uuid, username, linked_at, snowflake) VALUES(?, ?, ?, ?)", (ps) -> {
            ps.setString(1, token.getUuid().toString());
            ps.setString(2, getUsername(token.getUuid()));
            ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            ps.setString(4, String.valueOf(snowflake));
        });

        Optional<Player> player = VelocityDiscordSync.getInstance().getServer().getPlayer(token.getUuid());
        VelocityDiscordSync.getInstance().getServer().getEventManager().fireAndForget(new DiscordLinkEvent(token, snowflake, player.orElse(null)));
        tokenCache.invalidate(token);
    }

    public static TokenError canGenerate(UUID uuid) {
        AtomicBoolean isLinked = new AtomicBoolean(false);
        DatabaseManager.getSQLUtils().executeQuery("SELECT * FROM linked_users WHERE uuid = ?", (ps) -> ps.setString(1, uuid.toString()), (rs) -> {
            if(rs.next()) {
                logger.info("User {} ({}) is already linked to a Discord account.", uuid, getUsername(uuid));
                isLinked.set(true);
            } else {
                isLinked.set(false);
            }
            return rs;
        });

        if(isLinked.get()) return TokenError.ALREADY_LINKED;
        if(tokenCache.asMap().containsValue(uuid)) return TokenError.TOKEN_ACTIVE;

        return null;
    }

    private static class TokenExpireHandler implements RemovalListener<Token, UUID> {

        @Override
        public void onRemoval(@NotNull RemovalNotification<Token, UUID> notification) {
            if (notification.getCause().equals(RemovalCause.EXPIRED)) {
                tokenCache.invalidate(notification.getKey());
                logger.info("Token from " + getUsername(notification.getKey().getUuid()) + " expired.");
            }
        }
    }

    public static Optional<Token> fromString(String token) {
        for (Token tokens : tokenCache.asMap().keySet()) {
            if(tokens.getToken().equalsIgnoreCase(token))
                return Optional.of(tokens);
        }

        return Optional.empty();
    }

    public enum TokenError {
        ALREADY_LINKED,
        TOKEN_ACTIVE
    }
}