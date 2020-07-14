package de.aelpecyem.elementaristics.common.handler;

import de.aelpecyem.elementaristics.client.particle.type.MagicParticleEffect;
import de.aelpecyem.elementaristics.registry.ModParticles;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.util.TypedActionResult;

public class CommonEventHandler {
    public static void addEvents(){
        UseItemCallback.EVENT.register((playerEntity, world, hand) -> {
            if (world.isClient()){
                world.addParticle(new MagicParticleEffect(ModParticles.GLOW, new MagicParticleEffect.MagicParticleInfo(1)), playerEntity.getX(), playerEntity.getY(), playerEntity.getZ(), 0, 0, 0);
            }
            return TypedActionResult.pass(playerEntity.getStackInHand(hand));
        });

    }
}
