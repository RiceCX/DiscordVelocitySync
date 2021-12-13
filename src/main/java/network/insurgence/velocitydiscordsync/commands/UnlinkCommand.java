package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;

public class UnlinkCommand extends AbstractCommand {

    // TODO ANDY: I don't know whats the best day of doing this and remove user from database, so you can do this.

    /**
     * Unlink command, used to unlink your account.
     */


    @Override
    public void execute(Invocation invocation) {
        // andy
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("unlink").build();
    }
}
