package de.aelpecyem.elementaristics.common;

import de.aelpecyem.elementaristics.common.handler.AlchemyHandler;
import de.aelpecyem.elementaristics.common.handler.CommonEventHandler;
import de.aelpecyem.elementaristics.common.handler.networking.PacketHandler;
import de.aelpecyem.elementaristics.lib.ModConfig;
import de.aelpecyem.elementaristics.registry.ModRegistry;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ModInitializer;

public class CommonProxy implements ModInitializer{
    public static final CommonConfig CONFIG = ConfigManager.loadConfig(CommonConfig.class);
    @Override
    public void onInitialize() {
        AlchemyHandler.init();
        ModConfig.init();
        PacketHandler.registerClientToServerPackets();
        ModRegistry.init();
        CommonEventHandler.addEvents();
    }
}
