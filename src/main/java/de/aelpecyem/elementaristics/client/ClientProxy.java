package de.aelpecyem.elementaristics.client;

import de.aelpecyem.elementaristics.client.handler.ClientEventHandler;
import net.fabricmc.api.ClientModInitializer;

public class ClientProxy implements ClientModInitializer{
    @Override
    public void onInitializeClient() {
        ClientEventHandler.addEvents();
    }
}
