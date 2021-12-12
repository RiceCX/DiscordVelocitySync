package network.insurgence.velocitydiscordsync.core;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.SimpleCommand;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;

public abstract class AbstractCommand implements SimpleCommand {

    protected CommandMeta.Builder getBuilder(String alias) {
        return VelocityDiscordSync.getInstance().getServer().getCommandManager().metaBuilder(alias);
    }

    public abstract CommandMeta getMeta();
}
