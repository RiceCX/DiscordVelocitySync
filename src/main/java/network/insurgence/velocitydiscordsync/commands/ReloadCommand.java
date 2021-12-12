package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;

public class ReloadCommand extends AbstractCommand {

    /**
     * Reloading VelocityDiscordSync's configuration
     */

    @Override
    public void execute(Invocation invocation) {
        Config.getInstance().reloadConfig();
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("reload").build();
    }
}
