package io.github.pouffy.agrestic.compat;

import io.github.pouffy.agrestic.compat.appleskin.AppleskinEventHandler;
import io.github.pouffy.agrestic.core.CompatManager;
import net.neoforged.bus.api.IEventBus;

public class CompatEventHandler {

    public static void init(IEventBus modBus, IEventBus gameBus) {
        if (CompatManager.APPLESKIN.isLoaded()) {
            AppleskinEventHandler.init(modBus, gameBus);
        }
    }
}
