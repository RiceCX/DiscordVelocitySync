package network.insurgence.velocitydiscordsync.core;

import com.google.common.base.Preconditions;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import network.insurgence.velocitydiscordsync.VelocityDiscordSync;
import network.insurgence.velocitydiscordsync.bot.SyncBot;
import network.insurgence.velocitydiscordsync.config.Config;
import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

public class RoleHandler {

    private final JDA jda;
    private final Logger logger = VelocityDiscordSync.getLogger();

    public RoleHandler() {
        jda = SyncBot.getInstance();
    }

    public List<Long> parseRoleIdFromGroupName(String groupName) {
        return Config.get().getRole().getRoles().get(groupName);
    }

    public void giveRole(String snowflake, String groupName) {
        List<Long> roleIds = parseRoleIdFromGroupName(groupName);
        if(roleIds == null || roleIds.isEmpty()) return;
        for (Long roleId : roleIds) {
            giveRole(snowflake, roleId);
        }
    }

    /**
     * Gives the user the specified role.
     * @param snowflake The snowflake of the user.
     * @param roleId The snowflake of the role.
     */
    public void giveRole(String snowflake, Long roleId) {
        String guildId = Config.get().getDiscordSection().getGuildId();
        if(guildId == null) {
            logger.warn("No guild id found in config.yml");
            return;
        }

        Guild guild = jda.getGuildById(guildId);
        Preconditions.checkNotNull(guild, "Guild not found");

        Member member = guild.getMemberById(snowflake);
        if(member == null) {
            logger.warn("Member not found with id: " + snowflake);
            return;
        }

        Optional.ofNullable(guild.getRoleById(roleId))
                .ifPresentOrElse(
                        role -> guild.addRoleToMember(member, role).queue(),
                        () -> logger.warn("Role not found: " + roleId)
                );

    }

    public void removeRole(String snowflake, Long roleId) {

    }
}
