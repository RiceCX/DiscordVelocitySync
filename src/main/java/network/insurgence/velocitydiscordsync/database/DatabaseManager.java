package network.insurgence.velocitydiscordsync.database;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import org.slf4j.Logger;

import java.io.Closeable;

public class DatabaseManager implements Closeable {


    private final Logger logger = VelocityDiscordSync.getLogger();

    private static final SQLUtils sqlUtils = new SQLUtils();

    private HikariDataSource dataSource;

    public DatabaseManager(SQLTypes type, HikariAuthentication auth) {
        try {
            dataSource = new HikariDataSource(HikariUtils.generateConfig(
                    type,
                    auth, type.getPort()
            ));

            SQLUtils.setDataSource(dataSource);

            logger.info("Connected to " + type.name() + "!");
        } catch(Exception e) {
            logger.error("Failed to connect to database.\n Error: {}", e.getMessage());
        }
    }

    @Override
    public void close() {
        if (isClosed())
            throw new IllegalStateException("Connection is not open.");

        logger.info("Closing connection...");
        this.dataSource.close();
    }

    public boolean isClosed() {
        return dataSource == null || dataSource.isClosed();
    }

    public static SQLUtils getSQLUtils() {
        return sqlUtils;
    }
}
