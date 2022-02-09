package network.insurgence.velocitydiscordsync.luckperms;

import com.velocitypowered.api.event.Subscribe;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.event.node.NodeAddEvent;
import net.luckperms.api.model.PermissionHolder;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.node.types.PermissionNode;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.core.DiscordHandler;
import network.insurgence.velocitydiscordsync.database.HikariUtils;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author RiceCX
 * Wrapper class for the LuckPerms API
 */
public class LPAPI {

    private final Logger logger = VelocityDiscordSync.getLogger();
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final LuckPerms api;

    public LPAPI() {
        if(!isClassActive()) {
            this.api = null;
        } else this.api = LuckPermsProvider.get();

        checkApiExistence();

        if(api != null) this.api.getEventBus().subscribe(VelocityDiscordSync.getInstance(), NodeAddEvent.class, this::onGroupAdd);
    }

    /**
     * This needs to be run async. Gets a user from the
     * specified identifier. This usually is a UUID.
     *
     * @param identifier The identifier to get the user from.
     * @return The user, or null if not found.
     */
    public Optional<User> getUserFromIdentifier(String identifier) {
        if (api == null) return Optional.empty();

        User user = null;
        UUID uuid = null;

        if (identifier.length() == 36) {
            try {
                uuid = UUID.fromString(identifier);
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid UUID provided: " + identifier);
            }
        } else {
            uuid = api.getUserManager().lookupUniqueId(identifier).join();
        }

        if (uuid != null) {
            user = api.getUserManager().getUser(uuid);
        }

        return Optional.ofNullable(user);
    }

    @Subscribe
    public void onGroupAdd(NodeAddEvent event) {
        if (!(event.getNode() instanceof InheritanceNode node)) return;
        if (!event.getTarget().getIdentifier().getType().equals(PermissionHolder.Identifier.USER_TYPE)) return;

        logger.info("User inherited group: " + event.getTarget().getIdentifier() + " -> " + node.getGroupName());
        String identifier = event.getTarget().getIdentifier().getName();

        try {
            UUID playerUUID = UUID.fromString(identifier);

            executorService.submit(
                    () -> DiscordHandler.getSnowflakeByUUID(playerUUID)
                            .ifPresent(snowflake -> VelocityDiscordSync.getInstance().getRoleHandler().giveRole(snowflake, node.getGroupName()))
            );
        } catch (Exception e) {
            // Not a UUID.
        }
    }


    private boolean isClassActive() {
        try {
            Class.forName("net.luckperms.api.LuckPermsProvider");
            return true;
        } catch (Exception e) {
            return false;
        }
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
