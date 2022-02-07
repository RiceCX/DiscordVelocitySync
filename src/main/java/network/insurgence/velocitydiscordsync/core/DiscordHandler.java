package network.insurgence.velocitydiscordsync.core;

import network.insurgence.velocitydiscordsync.database.DatabaseManager;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class DiscordHandler {

    public static Optional<String> getSnowflakeByUUID(UUID uuid) {
        AtomicReference<String> snowflake = new AtomicReference<>();
        DatabaseManager.getSQLUtils().executeQuery("SELECT * FROM `linked_users` WHERE uuid = ?", ps -> ps.setString(1, uuid.toString()), (rs) -> {
            if(rs.next()) {
                snowflake.set(rs.getString("snowflake"));
            }
            return rs;
        });
        return Optional.ofNullable(snowflake.get());
    }
}
