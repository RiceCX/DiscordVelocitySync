package network.insurgence.velocitydiscordsync.database;

public enum SQLTypes {

    MYSQL("com.mysql.cj.jdbc.Driver", "jdbc:mysql://{host}:{port}/{database}", 3306),
    SQLITE("org.sqlite.JDBC", "jdbc:sqlite:{database}", 0),
    POSTGRES("org.postgresql.ds.PGSimpleDataSource", "jdbc:postgresql://{host}:{port}/{database}", 5432);

    private final String driverName;
    private final String driverURL;
    private final int port;

    SQLTypes(String driverName, String driverURL, int port) {
        this.driverName = driverName;
        this.driverURL = driverURL;
        this.port = port;
    }

    public static SQLTypes fromName(String name) {
        for (SQLTypes type : values()) {
            if(type.name().equalsIgnoreCase(name))
                return type;
        }
        return SQLITE;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getDriverURL() {
        return driverURL;
    }

    public int getPort() {
        return port;
    }
}

