package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;
import network.insurgence.velocitydiscordsync.core.TokenHandler;

public class UnlinkCommand extends AbstractCommand {

    @Override
    public void execute(Invocation invocation) {
        if(invocation.source() instanceof Player player) {
            TokenHandler.unlinkToken(player.getUniqueId());
        }
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("unlink").build();
    }
}
