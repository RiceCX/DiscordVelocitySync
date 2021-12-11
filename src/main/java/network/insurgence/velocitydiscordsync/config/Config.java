package network.insurgence.velocitydiscordsync.config;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.nio.file.Path;

public class Config {

    @DataDirectory
    @Inject
    private Path dataDirectory;

    @Inject
    private Logger logger;


    private final YamlConfigurationLoader loader;

    private static Config instance;


    private CommentedConfigurationNode root;

    private Config() {
        loader = YamlConfigurationLoader.builder()
                .path(dataDirectory)
                .build();

        loadConfig();
    }

    /**
     * Loads the configuration.
     */
    private void loadConfig() {
        try {
            root = loader.load();
        } catch (ConfigurateException e) {
            logger.error("An error occurred while loading this configuration: " + e.getMessage());
            if(e.getCause() != null) {
                logger.error("Caused by: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
    }


    public void reloadConfig() {
        loadConfig();
    }

    public CommentedConfigurationNode getRoot() {
        return root;
    }

    /**
     * Reloads the configuration.
     */
    public void save() {
        try {
            loader.save(root);
        } catch (final ConfigurateException e) {
            System.err.println("Unable to save your messages configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }

    }

    public static Config getInstance() {
        if(instance == null) {
            instance = new Config();
        }

        return instance;
    }
}
