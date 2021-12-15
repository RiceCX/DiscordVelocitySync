package network.insurgence.velocitydiscordsync;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import network.insurgence.velocitydiscordsync.bot.SyncBot;
import network.insurgence.velocitydiscordsync.commands.LinkCommand;
import network.insurgence.velocitydiscordsync.commands.ReloadCommand;
import network.insurgence.velocitydiscordsync.commands.UnlinkCommand;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;
import network.insurgence.velocitydiscordsync.database.DatabaseManager;
import network.insurgence.velocitydiscordsync.database.HikariAuthentication;
import network.insurgence.velocitydiscordsync.database.SQLTypes;
import network.insurgence.velocitydiscordsync.migrations.DefaultMigration;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "velocitydiscordsync",
        name = "VelocityDiscordSync",
        version = "1.0-SNAPSHOT",
        authors = {"RiceCX", "Pace1337"},
        dependencies = {
                @Dependency(id = "luckperms", optional = true)
        }
)
public class VelocityDiscordSync {

    private static VelocityDiscordSync instance = null;

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer server;

    public static Path dataDirectory;

    private DatabaseManager databaseManager;

    @Inject
    public VelocityDiscordSync(@DataDirectory Path dataDirectory) {
        instance = this;
        VelocityDiscordSync.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        registerDatabase();
        new SyncBot(Config.get().getDiscordSection().getToken());
        registerCommands();
        logger.info("VelocityDiscordSync has been enabled!");
    }


    private void registerDatabase() {
        databaseManager = new DatabaseManager(SQLTypes.MYSQL, getDatabaseCredentials());

        new DefaultMigration().migrate();

    }

    private HikariAuthentication getDatabaseCredentials() {
        return new HikariAuthentication(
                Config.get().getDatabaseSection().getUser(),
                Config.get().getDatabaseSection().getPassword(),
                Config.get().getDatabaseSection().getHost(),
                Config.get().getDatabaseSection().getDatabase()
        );
    }

    private void registerCommands() {
        registerCommand(
                new LinkCommand(),
                new ReloadCommand(),
                new UnlinkCommand()
        );
    }

    private void registerCommand(AbstractCommand... commands) {
        for (AbstractCommand command : commands) {
            server.getCommandManager().register(command.getMeta(), command);
        }
    }

    public static VelocityDiscordSync getInstance() {
        return instance;
    }

    public ProxyServer getServer() {
        return server;
    }

    public static Logger getLogger() {
        return instance.logger;
    }

}
