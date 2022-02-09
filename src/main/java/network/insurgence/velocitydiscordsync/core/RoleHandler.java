package network.insurgence.velocitydiscordsync.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.bot.SyncBot;
import network.insurgence.velocitydiscordsync.config.Config;
import org.slf4j.Logger;

import java.util.List;

public class RoleHandler {

    private final JDA jda;
    private final Logger logger = VelocityDiscordSync.getLogger();

    public RoleHandler() {
        jda = SyncBot.getInstance();
    }

    public List<Long> parseRoleIdFromGroupName(String groupName) {
        return Config.get().getRoles().get(groupName);
    }

    public void giveRole(String snowflake, String groupName) {
        List<Long> roleIds = parseRoleIdFromGroupName(groupName);
        if (roleIds == null || roleIds.isEmpty()) return;
        for (Long roleId : roleIds) {
            giveRole(snowflake, roleId);
        }
    }

    /**
     * Gives the user the specified role.
     *
     * @param snowflake The snowflake of the user.
     * @param roleId    The snowflake of the role.
     */
    public void giveRole(String snowflake, Long roleId) {
        logger.info("Giving role {} to {}", roleId, snowflake);
        String guildId = Config.get().getDiscordSection().getGuildId();
        if (guildId == null) {
            logger.error("No guild id found in config.yml");
            return;
        }

        Guild guild = SyncBot.getInstance().getGuildById(guildId);

        if (guild == null) {
            logger.error("Guild {} not found", guildId);
            return;
        }

        guild.retrieveMemberById(snowflake).queue((member) -> {
            Role role = guild.getRoleById(roleId);

            if(role == null) {
                logger.error("Role {} not found", roleId);
                return;
            }

            guild.addRoleToMember(member, role)
                    .queue(
                            (r) -> logger.info("Added role: " + role.getName() + " to member: " + member.getUser().getName()),
                            (error) -> logger.error("Could not add role {} to {} due to: {}", role.getName(), member.getUser().getAsTag(), error.getMessage()));

        }, (err) -> logger.error("Error retrieving member: {}", err.getMessage()));
    }

    public void removeRole(String snowflake, Long roleId) {

    }
}
