package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;
import network.insurgence.velocitydiscordsync.core.TokenHandler;

public class UnlinkCommand extends AbstractCommand {

    @Override
    public void execute(Invocation invocation) {
        if(invocation.source() instanceof Player player) {
            TokenHandler.UnlinkResult result = TokenHandler.unlinkToken(player.getUniqueId());

            switch (result) {
                case SUCCESS -> invocation.source().sendMessage(Component.text("Unlinked!"));
                case NOT_LINKED -> invocation.source().sendMessage(Component.text("You are not linked!"));
                default -> invocation.source().sendMessage(Component.text("Unlink failed!"));
            }
        }
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("unlink").build();
    }
}
