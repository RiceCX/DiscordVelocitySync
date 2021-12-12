package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;
import network.insurgence.velocitydiscordsync.core.TokenHandler;

public class LinkCommand extends AbstractCommand {

    /**
     * Link command, you get your code.
     */

    @Override
    public void execute(Invocation invocation) {
        if(invocation.source() instanceof Player player) {
            if(!TokenHandler.canGenerate(player.getUniqueId())) {
                String nospamLang = Config.get().getLang().getNospam();
                if(nospamLang == null) nospamLang = "&cYou can only get one code every &210&c minutes!";
                TextComponent nospamComp = LegacyComponentSerializer.legacy('&').deserialize(nospamLang);
                player.sendMessage(nospamComp);
                return;
            }
            String tokenLang = Config.get().getLang().getTokenGrant();
            String token = TokenHandler.generatePIN(player.getUniqueId());
            String tokenMessage;
            if(tokenLang != null)
                tokenMessage = tokenLang.replace("{code}", token);
            else
                tokenMessage = "&aYour code is &2&n{code}&a send this in the verification channel! &7(/discord)".replace("{code}", token);

            TextComponent txtComponent = LegacyComponentSerializer.legacy('&').deserialize(tokenMessage);
            player.sendMessage(txtComponent);
        } else {
            invocation.source().sendMessage(Component.text("You must be a player to use this command."));
        }
    }

    @Override
    public CommandMeta getMeta() {
        return getBuilder("link").build();
    }
}
