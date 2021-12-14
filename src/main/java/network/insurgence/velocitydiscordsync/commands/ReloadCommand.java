package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import net.kyori.adventure.text.Component;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;

public class ReloadCommand extends AbstractCommand {

    /**
     * Reloading VelocityDiscordSync's configuration
     */

    @Override
    public void execute(Invocation invocation) {
        Config.getInstance().reloadConfig();
        invocation.source().sendMessage(Component.text("§bYou have reloaded the config."));
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("linkreload").build();
    }
}
