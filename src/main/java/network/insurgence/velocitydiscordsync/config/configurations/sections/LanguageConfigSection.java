package network.insurgence.velocitydiscordsync.config.configurations.sections;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class LanguageConfigSection {

    @Nullable
    private String grant;

    @Nullable
    private String nospam;

    @Nullable
    private String alreadylinked;

    public @Nullable String getTokenGrant() {
        return grant;
    }

    @Nullable
    public String getNospam() {
        return nospam;
    }

    @Nullable
    public String getAlreadylinked() {
        return alreadylinked;
    }
}
