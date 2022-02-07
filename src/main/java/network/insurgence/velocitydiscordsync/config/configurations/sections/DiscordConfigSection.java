package network.insurgence.velocitydiscordsync.config.configurations.sections;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class DiscordConfigSection {

    @Comment("The Discord Bot token, KEEP THIS PRIVATE!")
    @Nullable
    private String token;

    @Nullable
    private String guildId;

    @Nullable
    private String channel;

    public @Nullable String getToken() {
        return token;
    }

    public @Nullable String getGuildId() {
        return guildId;
    }

    public @Nullable String getTokenChannel() {
        return channel;
    }
}
