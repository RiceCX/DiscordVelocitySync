package network.insurgence.velocitydiscordsync.database;

import com.zaxxer.hikari.HikariDataSource;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import org.slf4j.Logger;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager implements Closeable {


    private final Logger logger = VelocityDiscordSync.getLogger();

    private static final SQLUtils sqlUtils = new SQLUtils();
    private final HikariDataSource dataSource;


    public DatabaseManager(SQLTypes type, HikariAuthentication auth) {
        dataSource = new HikariDataSource(HikariUtils.generateConfig(
                type,
                auth, type.getPort()
        ));

        SQLUtils.setConnection(getConnection());
        logger.info("Connected to " + type.name() + "!");
    }

    public Connection getConnection() {
        if (isClosed())
            throw new IllegalStateException("Connection is not open.");

        try {

            return dataSource.getConnection();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
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
