package network.insurgence.velocitydiscordsync.luckperms;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;

/**
 * @author RiceCX
 * Wrapper class for the LuckPerms API
 */
public class LPAPI {

    private final Logger logger = VelocityDiscordSync.getLogger();

    private final LuckPerms api;

    public LPAPI() {
        this.api = LuckPermsProvider.get();

        checkApiExistence();
    }

    public Optional<User> getUser(UUID uuid) {
        if(api == null) return Optional.empty();

        return Optional.ofNullable(api.getUserManager().getUser(uuid));
    }

    private void checkApiExistence() {
        if (api == null) {
            logger.warn("LuckPerms API not found! We will not be able to provide permissions!");
        }
    }

    public LuckPerms getApi() {
        return api;
    }
}
