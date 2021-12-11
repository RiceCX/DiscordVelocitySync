package network.insurgence.velocitydiscordsync.config;

import com.google.inject.Inject;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.function.Function;

public enum ConfigKeys {
    DISCORD_TOKEN("discord", "token")

    ;


    @Inject
    private Logger logger;

    private final String parent;
    private final String child;

    @Nullable
    private final Function<Object, Boolean> validator;

    ConfigKeys(String parent, String child, @Nullable Function<Object, Boolean> validator) {
        this.parent = parent;
        this.child = child;
        this.validator = validator;
    }

    ConfigKeys(String parent, String child) {
        this(parent, child, null);
    }


    /**
     * Gets the node of the config key.
     * @return The node of the config key.
     */
    public ConfigurationNode getNode() {
        return Config.getInstance().getRoot().node(this.parent, this.child);
    }


    /**
     * Gets the value of the config key.
     * @param clazz The class of the value.
     * @param <T> The type of the value.
     * @return The value of the config key.
     */
    public <T> T get(Class<? extends T> clazz) {
        try {
            return getNode().get(clazz);
        } catch (SerializationException e) {
            logger.error("Could not deserialize config value for key {}", this.name(), e);
            e.printStackTrace();
        }

        return null;
    }
}
