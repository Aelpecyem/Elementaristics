package de.aelpecyem.elementaristics.client;

import de.aelpecyem.elementaristics.client.handler.ClientEventHandler;
import de.aelpecyem.elementaristics.client.handler.ShaderHandler;
import de.aelpecyem.elementaristics.client.particle.GlowParticle;
import de.aelpecyem.elementaristics.client.render.blockentity.BlockRenderBoilingBasin;
import de.aelpecyem.elementaristics.common.handler.AlchemyHandler;
import de.aelpecyem.elementaristics.common.handler.networking.PacketHandler;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.registry.ModEntities;
import de.aelpecyem.elementaristics.registry.ModObjects;
import de.aelpecyem.elementaristics.registry.ModParticles;
import io.github.cottonmc.cotton.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class ClientProxy implements ClientModInitializer {
    public static final ClientConfig CONFIG = ConfigManager.loadConfig(ClientConfig.class);
    public static KeyBinding meditate = new KeyBinding("key." + Constants.MOD_ID + ".meditate", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_M, "category." + Constants.MOD_ID);

    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.GLOW, new GlowParticle.GlowFactory());
        KeyBindingHelper.registerKeyBinding(meditate);
        PacketHandler.registerServerToClientPackets();
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.MORNING_GLORY, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.MORNING_GLORY_VINES, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModObjects.BOILING_BASIN, RenderLayer.getTranslucent());
        BlockEntityRendererRegistry.INSTANCE.register(ModObjects.BOILING_BASIN_BLOCK_ENTITY_TYPE, BlockRenderBoilingBasin::new);
        ModEntities.registerRenderers();
        ClientEventHandler.addEvents();
        ShaderHandler.init();

        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> AlchemyHandler.Helper.getColor(stack), ModObjects.ALCHEMICAL_MATTER);
        ClientSpriteRegistryCallback.registerBlockAtlas((spriteAtlasTexture, registry) -> {
            registry.register(new Identifier(Constants.MOD_ID, "misc/fluid"));
        });
    }
}
