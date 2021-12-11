package network.insurgence.velocitydiscordsync.bot.listeners;


import com.google.inject.Inject;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import org.slf4j.Logger;

public class ReadyListener extends ListenerAdapter {

    @Inject
    private Logger logger;

    @Override
    public void onReady(@NotNull ReadyEvent evt) {
        logger.info("Velocity Discord Sync Bot is ready!");
    }
}
