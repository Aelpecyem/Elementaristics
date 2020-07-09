package de.aelpecyem.elementaristics.common;

import de.aelpecyem.elementaristics.common.handler.CommonEventHandler;
import de.aelpecyem.elementaristics.common.handler.networking.PacketHandler;
import de.aelpecyem.elementaristics.registry.ModRegistry;
import net.fabricmc.api.ModInitializer;

public class CommonProxy implements ModInitializer{
    @Override
    public void onInitialize() {
        PacketHandler.registerClientToServerPackets();
        ModRegistry.init();
        CommonEventHandler.addEvents();
    }
}
