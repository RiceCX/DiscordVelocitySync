package network.insurgence.velocitydiscordsync.config;

import com.google.inject.Inject;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.config.configurations.MainConfig;
import org.slf4j.Logger;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Config {

    @Inject
    private Logger logger;

    private final YamlConfigurationLoader defaultLoader;
    private final YamlConfigurationLoader loader;

    private static Config instance;

    private final Path configPath = Paths.get(VelocityDiscordSync.dataDirectory + "/config.yml");

    private CommentedConfigurationNode root;

    private MainConfig mainConfig;

    private Config() {
        defaultLoader = YamlConfigurationLoader.builder().indent(2)
                .nodeStyle(NodeStyle.BLOCK).url(getDefaultConfigResource()).build();


        loader = YamlConfigurationLoader.builder()
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions((opts) -> opts.shouldCopyDefaults(true))
                .path(configPath)
                .build();

        generateConfigFolderAndFile();
    }

    /**
     * Loads the configuration.
     */
    private void loadConfig() {
        try {
            root = loader.load();
            mainConfig = root.get(MainConfig.class);

        } catch (ConfigurateException e) {
            logger.error("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                logger.error("Caused by: " + e.getCause().getMessage());
            }
            e.printStackTrace();
        }
    }

    /**
     * Generate config folder and file if they don't exist.
     */
    private void generateConfigFolderAndFile() {
        try {
            if (!VelocityDiscordSync.dataDirectory.toFile().exists())
                VelocityDiscordSync.dataDirectory.toFile().mkdir();

            if (!configPath.toFile().exists())
                configPath.toFile().createNewFile();

            root = loader.load();
            root.mergeFrom(defaultLoader.load());
            loader.save(root);

            loadConfig();
        } catch (Exception e) {
            //    logger.error("An error occurred while creating the configuration file: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void reloadConfig() {
        loadConfig();
    }

    /**
     * Reloads the configuration.
     */
    public void save() {
        try {
            root.set(MainConfig.class, mainConfig);
            loader.save(root);
        } catch (final ConfigurateException e) {
            System.err.println("Unable to save your messages configuration! Sorry! " + e.getMessage());
            System.exit(1);
        }

    }


    public static MainConfig get() {
        return getInstance().mainConfig;
    }


    /**
     * Gets the path to the default configuration file from resources.
     *
     * @return The path to the default configuration file from resources.
     */
    public static URL getDefaultConfigResource() {
        return VelocityDiscordSync.class.getResource("/config.yml");
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }
}
