package network.insurgence.velocitydiscordsync.migrations;

import network.insurgence.velocitydiscordsync.database.DatabaseManager;
import network.insurgence.velocitydiscordsync.database.SQLUtils;

public interface Migration {
    void migrate();

    default SQLUtils getUtils() {
        return DatabaseManager.getSQLUtils();
    }
}