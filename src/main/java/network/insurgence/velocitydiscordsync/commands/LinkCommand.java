package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;

public class LinkCommand extends AbstractCommand {

    /**
     * Link command, you get your code.
     */

    @Override
    public void execute(Invocation invocation) {
        if(invocation.source() instanceof Player) {
            Player player = (Player) invocation.source();

            player.sendMessage(Component.text("You have been given your code: " + player.getUsername()));
        } else {
            invocation.source().sendMessage(Component.text("You must be a player to use this command."));
        }
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("link").build();
    }
}
