package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;
import network.insurgence.velocitydiscordsync.core.TokenHandler;

public class UnlinkCommand extends AbstractCommand {

    /**
     * Unlinking with a command.
     */

    @Override
    public void execute(Invocation invocation) {
        if(invocation.source() instanceof Player player) {
            TokenHandler.UnlinkResult result = TokenHandler.unlinkToken(player.getUniqueId());

            switch (result) {
                // Did the messages in a lazy way, maybe make it configurable in future.
                case SUCCESS -> invocation.source().sendMessage(Component.text("§aYour account has been unlinked!"));
                case NOT_LINKED -> invocation.source().sendMessage(Component.text("§aYou are not linked!"));
                default -> invocation.source().sendMessage(Component.text("§cUnlink failed, please contact server administration."));
            }
        }
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("unlink").build();
    }
}
