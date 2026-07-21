package io.github.pouffy.agrestic.core;

import net.neoforged.fml.ModList;

import java.util.Locale;

public enum CompatManager {
    EMI,
    APPLESKIN
    ;

    private final String namespace;

    CompatManager() {
        this.namespace = this.name().toLowerCase(Locale.ROOT);
    }

    public boolean isLoaded() {
        return ModList.get().isLoaded(this.namespace);
    }
}
