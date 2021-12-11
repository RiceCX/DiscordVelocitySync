package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.SimpleCommand;
import network.insurgence.velocitydiscordsync.config.Config;

public class ReloadCommand implements SimpleCommand {

    /**
     * Reloading VelocityDiscordSync's configuration
     */

    @Override
    public void execute(Invocation invocation) {
        Config.getInstance().reloadConfig();
    }
}
