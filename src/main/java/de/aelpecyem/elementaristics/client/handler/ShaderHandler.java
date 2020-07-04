package de.aelpecyem.elementaristics.client.handler;

import com.google.gson.JsonSyntaxException;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.DummyConfig;
import de.aelpecyem.elementaristics.registry.ModRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.ShaderEffect;
import net.minecraft.client.util.GlAllocationUtils;
import net.minecraft.client.util.GlfwUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

public class ShaderHandler {
    public static final HashMap<Integer, ShaderEffect> ACTIVE_SHADERS = new HashMap<>();
    public static boolean resetShaders = false;

    private static int oldDisplayWidth = 0;
    private static int oldDisplayHeight = 0;

    public static final int SHADER_INTOX = 0;
    public static Identifier[] shaders = new Identifier[]{new Identifier("shaders/post/phosophor.json")};

    public static void handleShaders(MinecraftClient mc){
        if (DummyConfig.shaders){
            handleShader(mc.player.hasStatusEffect(ModRegistry.INTOXICATED), SHADER_INTOX, mc);
        }
    }

    public static void handleShader(boolean condition, int shaderId, MinecraftClient mc) {
        if (condition) {
            if (!ACTIVE_SHADERS.containsKey(shaderId)) {
                addShader(shaderId, mc);
            }
        } else if (ACTIVE_SHADERS.containsKey(shaderId)) {
            ACTIVE_SHADERS.remove(shaderId);
        }
    }

    public static void addShader(int id, MinecraftClient client){
        Identifier identifier = shaders[id];
        try {
            ShaderEffect effect = new ShaderEffect(client.getTextureManager(), client.getResourceManager(), client.getFramebuffer(), identifier);
            effect.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            ACTIVE_SHADERS.put(id, effect);
        } catch (IOException var3) {
            Constants.LOGGER.warn("Failed to load shader: {}", identifier, var3);
        } catch (JsonSyntaxException var4) {
            Constants.LOGGER.warn("Failed to parse shader: {}", identifier, var4);
        }
    }

    public static void renderShaders(MatrixStack ms, float parTick){
        updateShaderFrameBuffers(MinecraftClient.getInstance());
       // GL11.glMatrixMode(5890);
        //GL11.glLoadIdentity();
        ACTIVE_SHADERS.forEach((integer, shaderEffect) -> {
            shaderEffect.render(parTick);
        });
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
    }

    public static void updateShaderFrameBuffers(MinecraftClient client) {
        if (resetShaders || client.getWindow().getFramebufferWidth() != oldDisplayWidth || oldDisplayHeight != client.getWindow().getFramebufferHeight()) {
            ACTIVE_SHADERS.forEach((integer, shaderEffect) -> {
                shaderEffect.setupDimensions(client.getWindow().getFramebufferWidth(), client.getWindow().getFramebufferHeight());
            });

            oldDisplayWidth = client.getWindow().getFramebufferWidth();
            oldDisplayHeight = client.getWindow().getFramebufferHeight();
            resetShaders = false;
        }

    }

}
