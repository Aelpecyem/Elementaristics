package de.aelpecyem.elementaristics.client.handler;

import de.aelpecyem.elementaristics.client.ClientProxy;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.registry.ModRegistry;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public class ShaderHandler {
    private static final ManagedShaderEffect INTOXICATED = ShaderEffectManager.getInstance().manage(new Identifier(Constants.MOD_ID, "shaders/post/intox.json"));

    public static void init() {
        ShaderEffectRenderCallback.EVENT.register(parTick -> {
            if (ClientProxy.CONFIG.useShaders && MinecraftClient.getInstance().player != null && MinecraftClient.getInstance().player.hasStatusEffect(ModRegistry.INTOXICATED)) {
                INTOXICATED.render(parTick);
            }
        });
    }
}
