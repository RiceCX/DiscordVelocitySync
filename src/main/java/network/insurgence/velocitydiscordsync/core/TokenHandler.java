package network.insurgence.velocitydiscordsync.core;

import com.google.common.cache.*;
import com.velocitypowered.api.proxy.Player;
import network.insurgence.velocitydiscordayncapi.DiscordLinkEvent;
import network.insurgence.velocitydiscordayncapi.token.Token;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.database.DatabaseManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Handles the token cache and database.
 * <p>
 * This class is thread-safe.
 * This class is not intended to be instantiated.
 * This class is not intended to be extended.
 * </p>
 *
 * @author RiceCX
 */
public class TokenHandler {


    private static final Logger logger = VelocityDiscordSync.getLogger();

    /**
     * The cache of tokens mapped by the PIN -> UUID of the player.
     */
    private static final Cache<Token, UUID> tokenCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).removalListener(new TokenExpireHandler()).maximumSize(1000).build();

    /**
     * Generates a new token for the given UUID.
     * This will also add the token to the cache.
     *
     * @param uuid The UUID of the player.
     * @return The generated token.
     */
    public static Token generate(UUID uuid) {
        if (canGenerate(uuid) != null) return null;

        Token token = Token.generate(uuid);
        tokenCache.put(token, uuid);
        return token;
    }

    /**
     * Gets the username of the given UUID.
     * @param uuid The UUID of the player.
     * @return The username of the player or N/A if not found.
     */
    public static String getUsername(UUID uuid) {
        String ign;

        Optional<Player> player = VelocityDiscordSync.getInstance().getServer().getPlayer(uuid);

        if (player.isPresent())
            ign = player.get().getUsername();
        else ign = "N/A";

        return ign;
    }

    /**
     * Links the given token to the given UUID and saves it to the database.
     *
     * @param token     The token to link.
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

    /**
     * Unlinks the given uuid from Discord through the database.
     *
     * @param uuid The UUID of the player.
     * @return {@link UnlinkResult} result of the unlink.
     */
    public static UnlinkResult unlinkToken(UUID uuid) {
        String username = getUsername(uuid);
        AtomicReference<UnlinkResult> result = new AtomicReference<>(UnlinkResult.UNLINK_FAILED);

        int affected = DatabaseManager.getSQLUtils().executeUpdate("DELETE FROM linked_users WHERE uuid = ?", (ps) -> ps.setString(1, uuid.toString()));

        if (affected > 0) {
            result.set(UnlinkResult.SUCCESS);
            logger.info("User {} ({}) has been unlinked from Discord.", username, uuid);
        } else {
            result.set(UnlinkResult.NOT_LINKED);
            logger.warn("User {} ({}) was not found in the database.", username, uuid);

        }

        return result.get();
    }

    /**
     * Returns whether if they can generate a uuid or not.
     * Usually depending on if they exist in the db or not.
     * @param uuid The UUID of the player.
     * @return {@link TokenResult} result of the check. Null if no error.
     */
    public static TokenResult canGenerate(UUID uuid) {
        AtomicBoolean isLinked = new AtomicBoolean(false);
        DatabaseManager.getSQLUtils().executeQuery("SELECT * FROM linked_users WHERE uuid = ?", (ps) -> ps.setString(1, uuid.toString()), (rs) -> {
            if (rs.next()) {
                logger.info("User {} ({}) is already linked to a Discord account.", uuid, getUsername(uuid));
                isLinked.set(true);
            } else {
                isLinked.set(false);
            }
            return rs;
        });

        if (isLinked.get()) return TokenResult.ALREADY_LINKED;
        if (tokenCache.asMap().containsValue(uuid)) return TokenResult.TOKEN_ACTIVE;

        return null;
    }

    /**
     * Returns a token from the provided string if it exists.
     * @param token The token to check.
     * @return The token if it exists. Empty if not.
     */
    public static Optional<Token> fromString(String token) {
        for (Token tokens : tokenCache.asMap().keySet()) {
            if (tokens.getToken().equalsIgnoreCase(token))
                return Optional.of(tokens);
        }

        return Optional.empty();
    }

    /**
     * Class representing a listener that listens for
     * any changes to the token cache.
     */
    private static class TokenExpireHandler implements RemovalListener<Token, UUID> {

        @Override
        public void onRemoval(@NotNull RemovalNotification<Token, UUID> notification) {
            if (notification.getCause().equals(RemovalCause.EXPIRED)) {
                tokenCache.invalidate(notification.getKey());
                logger.info("Token from " + getUsername(notification.getKey().getUuid()) + " expired.");
            }
        }
    }


    public enum UnlinkResult {
        SUCCESS,
        NOT_LINKED,
        UNLINK_FAILED
    }

    public enum TokenResult {
        ALREADY_LINKED,
        TOKEN_ACTIVE
    }
}