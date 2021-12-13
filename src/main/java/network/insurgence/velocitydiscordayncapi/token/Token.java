package network.insurgence.velocitydiscordayncapi.token;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Represents a token used to authenticate with between Minecraft and Discord.
 */
public class Token {

    /**
     * The lifetime of the token in milliseconds. (default = 10 minutes)
     */
    public static final long LIFETIME = TimeUnit.MINUTES.toMillis(10);

    /**
     * Minecraft UUID of the person who initiated this token.
     */
    private final UUID uuid;

    /**
     * The token itself.
     */
    private final String token;

    /**
     * Time initiated.
     */
    private final long createdAt;

    private TokenState tokenState = TokenState.UNKNOWN;

    public Token(UUID uuid, String token) {
        this(uuid, token, System.currentTimeMillis());
    }

    /**
     * Token Object that handles tokens
     * @param uuid Minecraft UUID of the person who initiated this token.
     * @param token The token itself.
     * @param createdAt Time initiated.
     */
    public Token(UUID uuid, String token, long createdAt) {
        this.uuid = uuid;
        this.token = token;
        this.createdAt = createdAt;
    }

    /**
     * Generates a new token.
     * @param uuid Minecraft UUID of the person who initiated this token.
     * @return The new token.
     */
    public static Token generate(UUID uuid) {
        String token = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

        return new Token(uuid, token);
    }


    /**
     * Checks if a token is still valid.
     * If not, cases could be that it could be expired, or that it is invalid.
     * @return Whether the token is still valid.
     */
    public boolean isValid() {
        return tokenState == TokenState.UNUSED || (System.currentTimeMillis() - createdAt) < LIFETIME;
    }

    /**
     * @return The Minecraft UUID of the person who initiated this token.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @return The token itself.
     */
    public String getToken() {
        return token;
    }

    /**
     * @return Time initiated.
     */
    public long getCreatedAt() {
        return createdAt;
    }

    /**
     * @return The token state.
     */
    public TokenState getTokenState() {
        return tokenState;
    }

    /**
     * @param tokenState The token state.
     */
    public void setTokenState(TokenState tokenState) {
        this.tokenState = tokenState;
    }
}
