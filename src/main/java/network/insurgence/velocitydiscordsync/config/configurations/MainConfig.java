package network.insurgence.velocitydiscordsync.config.configurations;


import network.insurgence.velocitydiscordsync.config.configurations.sections.DiscordConfigSection;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class MainConfig {

    @Comment("Configuration for Discord related settings")
    private DiscordConfigSection discord;


    @Comment("you're a nigger")
    private String test = "ngiger";


    public DiscordConfigSection getDiscordSection() {
        return discord;
    }
}
