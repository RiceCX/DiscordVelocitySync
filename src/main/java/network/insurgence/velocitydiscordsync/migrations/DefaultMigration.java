package network.insurgence.velocitydiscordsync.migrations;


public class DefaultMigration implements Migration {
    @Override
    public void migrate() {

        getUtils().executeUpdate("""
                CREATE TABLE IF NOT EXISTS `linked_users` (
                  `uuid` varchar(255) NOT NULL,\s
                  `username` varchar(255) NOT NULL,
                  `linked_at` datetime NOT NULL,
                  `snowflake` varchar(255) NOT NULL,\s
                  PRIMARY KEY (`uuid`)
                ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;""");
    }
}
