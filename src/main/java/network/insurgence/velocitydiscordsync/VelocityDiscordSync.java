package network.insurgence.velocitydiscordsync;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import network.insurgence.velocitydiscordsync.bot.SyncBot;
import network.insurgence.velocitydiscordsync.commands.LinkCommand;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;
import org.slf4j.Logger;

import java.nio.file.Path;

@Plugin(
        id = "velocitydiscordsync",
        name = "VelocityDiscordSync",
        version = "1.0-SNAPSHOT",
        authors = {"RiceCX", "Pace1337"}
)
public class VelocityDiscordSync {


    private static VelocityDiscordSync instance = null;

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer server;

    public static Path dataDirectory;

    @Inject
    public VelocityDiscordSync(@DataDirectory Path dataDirectory) {
        instance = this;
        VelocityDiscordSync.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        new SyncBot(Config.get().getDiscordSection().getToken());
        registerCommands();
        logger.info("VelocityDiscordSync has been enabled!");
    }


    private void registerCommands() {
        registerCommand(new LinkCommand());
    }

    private void registerCommand(AbstractCommand ...commands) {
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
