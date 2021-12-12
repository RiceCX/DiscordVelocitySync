package network.insurgence.velocitydiscordsync.core;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class TokenHandler {

    /**
     * The cache of tokens mapped by the PIN -> UUID of the player.
     */
    public static Cache<String, UUID> tokenCache = CacheBuilder.newBuilder().expireAfterWrite(10, TimeUnit.MINUTES).maximumSize(1000).build();

    public static String generatePIN(UUID uuid) {
        String token = String.format("%04d", ThreadLocalRandom.current().nextInt(10000));

        tokenCache.put(token, uuid);
        return token;
    }

    public static boolean canGenerate(UUID uuid) {
        return !tokenCache.asMap().containsValue(uuid);
    }
}