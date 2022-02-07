package network.insurgence.velocitydiscordsync.config.configurations.sections;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class RoleConfigSection {

    @NotNull
    private Map<String, List<Long>> roles = new HashMap<>();


    public @NotNull Map<String, List<Long>> getRoles() {
        return roles;
    }
}
