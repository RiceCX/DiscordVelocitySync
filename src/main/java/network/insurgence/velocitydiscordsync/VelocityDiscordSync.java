package network.insurgence.velocitydiscordsync;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import network.insurgence.velocitydiscordsync.bot.SyncBot;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.config.ConfigKeys;
import org.slf4j.Logger;

@Plugin(
        id = "velocitydiscordsync",
        name = "VelocityDiscordSync",
        version = "1.0-SNAPSHOT",
        authors = {"RiceCX", "Pace1337"}
)
public class VelocityDiscordSync {

    @Inject
    private Logger logger;

    @Inject
    private ProxyServer server;


    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {

        new SyncBot(ConfigKeys.DISCORD_TOKEN.get(String.class));

        logger.info("VelocityDiscordSync has been enabled!");
    }
}
