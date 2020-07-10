package de.aelpecyem.elementaristics.client;

import de.aelpecyem.elementaristics.client.handler.ClientEventHandler;
import de.aelpecyem.elementaristics.common.handler.networking.PacketHandler;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.registry.ModEntities;
import de.aelpecyem.elementaristics.registry.ModObjects;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ClientProxy implements ClientModInitializer{
    public static KeyBinding meditate = new KeyBinding("key." + Constants.MODID + ".meditate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category." + Constants.MODID);

    @Override
    public void onInitializeClient() {
        KeyBindingHelper.registerKeyBinding(meditate);
        PacketHandler.registerServerToClientPackets();
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.MORNING_GLORY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.MORNING_GLORY_VINES, RenderLayer.getCutout());
        ModEntities.registerRenderers();
        ClientEventHandler.addEvents();
    }
}
