package network.insurgence.velocitydiscordsync.commands;

import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import network.insurgence.velocitydiscordsync.config.Config;
import network.insurgence.velocitydiscordsync.core.AbstractCommand;
import network.insurgence.velocitydiscordsync.core.TokenHandler;

import java.util.Objects;

public class LinkCommand extends AbstractCommand {

    /**
     * Link command, you get your code.
     */

    @Override
    public void execute(Invocation invocation) {
        if (invocation.source() instanceof Player player) {
            TokenHandler.TokenResult canGen = TokenHandler.canGenerate(player.getUniqueId());
            if (canGen != null) {
                String errorMessage = "&cYou can only get one code every &210&c minutes!";

                if(canGen == TokenHandler.TokenResult.ALREADY_LINKED) {
                    if(Config.get().getLang().getAlreadylinked() != null)
                        errorMessage = Config.get().getLang().getAlreadylinked();
                } else {
                    if(Config.get().getLang().getNospam() != null) errorMessage = Config.get().getLang().getNospam();
                }

                // I don't get why this should be here...
                if(errorMessage == null) errorMessage = "&cYou can only get one code every &210&c minutes!";

                TextComponent errorComponent = LegacyComponentSerializer.legacy('&').deserialize(errorMessage);
                player.sendMessage(errorComponent);
                return;
            }

            String tokenLang = Config.get().getLang().getTokenGrant();
            String token = Objects.requireNonNull(TokenHandler.generate(player.getUniqueId())).getToken();
            String tokenMessage;

            tokenMessage = Objects.requireNonNullElse(tokenLang, "&aYour code is &2&n{code}&a send this in the verification channel! &7(/discord)").replace("{code}", token);

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
