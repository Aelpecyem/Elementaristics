package de.aelpecyem.elementaristics.common;

import de.aelpecyem.elementaristics.common.handler.CommonEventHandler;
import de.aelpecyem.elementaristics.registry.ModObjects;
import de.aelpecyem.elementaristics.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;

public class CommonProxy implements ModInitializer{
    @Override
    public void onInitialize() {
        ModRegistry.init();
        CommonEventHandler.addEvents();
    }
}
