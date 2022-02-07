package network.insurgence.velocitydiscordsync.config.configurations;

import network.insurgence.velocitydiscordsync.config.configurations.sections.DatabaseSection;
import network.insurgence.velocitydiscordsync.config.configurations.sections.DiscordConfigSection;
import network.insurgence.velocitydiscordsync.config.configurations.sections.LanguageConfigSection;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class MainConfig {

    @Comment("Configuration for Discord related settings")
    private DiscordConfigSection discord;

    @Comment("Language shit")
    private LanguageConfigSection lang;

    private DatabaseSection database;

    private @NotNull Map<String, List<Long>> roles = new HashMap<>();


    public LanguageConfigSection getLang() {
        return lang;
    }

    public DiscordConfigSection getDiscordSection() {
        return discord;
    }

    public DatabaseSection getDatabaseSection() { return database; }

    public @NotNull Map<String, List<Long>> getRoles() {
        return roles;
    }
}
