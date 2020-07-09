package de.aelpecyem.elementaristics.client;

import de.aelpecyem.elementaristics.client.handler.ClientEventHandler;
import de.aelpecyem.elementaristics.common.handler.networking.PacketHandler;
import de.aelpecyem.elementaristics.registry.ModEntities;
import de.aelpecyem.elementaristics.registry.ModObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;

public class ClientProxy implements ClientModInitializer{
    @Override
    public void onInitializeClient() {
        PacketHandler.registerServerToClientPackets();
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.MORNING_GLORY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.MORNING_GLORY_VINES, RenderLayer.getCutout());
        ModEntities.registerRenderers();
        ClientEventHandler.addEvents();
    }
}
