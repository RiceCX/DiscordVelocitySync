package network.insurgence.velocitydiscordsync.config.configurations;

import network.insurgence.velocitydiscordsync.config.configurations.sections.DatabaseSection;
import network.insurgence.velocitydiscordsync.config.configurations.sections.DiscordConfigSection;
import network.insurgence.velocitydiscordsync.config.configurations.sections.LanguageConfigSection;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class MainConfig {

    @Comment("Configuration for Discord related settings")
    private DiscordConfigSection discord;

    @Comment("Language shit")
    private LanguageConfigSection lang;

    private DatabaseSection database;


    public LanguageConfigSection getLang() {
        return lang;
    }

    public DiscordConfigSection getDiscordSection() {
        return discord;
    }

    public DatabaseSection getDatabaseSection() { return database; }
}
