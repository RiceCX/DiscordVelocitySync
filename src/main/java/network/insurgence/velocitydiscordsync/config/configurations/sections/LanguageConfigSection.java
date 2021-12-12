package network.insurgence.velocitydiscordsync.config.configurations.sections;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class LanguageConfigSection {

    @Nullable
    private String grant;

    @Nullable
    private String nospam;

    public @Nullable String getTokenGrant() {
        return grant;
    }

    public String getNospam() {
        return nospam;
    }
}
