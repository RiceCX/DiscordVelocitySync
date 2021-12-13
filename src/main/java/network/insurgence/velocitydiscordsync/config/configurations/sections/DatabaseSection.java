package network.insurgence.velocitydiscordsync.config.configurations.sections;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class DatabaseSection {

    @Comment("The user of the database")
    @Nullable
    private String user;

    @Nullable
    private String password;

    @Nullable

    private String host;

    @Nullable
    private String database;


    public @Nullable String getUser() {
        return user;
    }

    public @Nullable String getPassword() {
        return password;
    }

    public @Nullable String getHost() {
        return host;
    }

    public @Nullable String getDatabase() {
        return database;
    }
}
