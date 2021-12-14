package network.insurgence.velocitydiscordayncapi;

import com.velocitypowered.api.proxy.Player;

public class DiscordUnlinkEvent {

    private final Player player;

    public DiscordUnlinkEvent(Player player) {
        this.player = player;
    }

    /**
     * @return The {@link Player} that's linked to this token.
     */
    public Player getPlayer() {
        return player;
    }
}
