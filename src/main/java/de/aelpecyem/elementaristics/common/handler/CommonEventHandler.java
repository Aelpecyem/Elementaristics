package de.aelpecyem.elementaristics.common.handler;

import ca.weblite.objc.Client;
import de.aelpecyem.elementaristics.client.particle.GlowParticle;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.TypedActionResult;

public class CommonEventHandler {
    public static void addEvents(){
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            if (world.isClient()){
                MinecraftClient.getInstance().particleManager.addParticle(new GlowParticle((ClientWorld) world, playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0, 0, 0));
            }
            return TypedActionResult.pass(playerEntity.getStackInHand(hand));
        });

    }
}
