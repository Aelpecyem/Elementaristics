package de.aelpecyem.elementaristics.client.mixin;

import de.aelpecyem.elementaristics.common.feature.stats.IElemStats;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerRendererMixin {
    @Inject(at = @At("TAIL"), method = "getPositionOffset(Lnet/minecraft/entity/Entity;F)Lnet/minecraft/util/math/Vec3d;", cancellable = true)
    private void meditateOffset(Entity player, float f, CallbackInfoReturnable<Vec3d> info) {
        if (!player.hasVehicle() && player instanceof IElemStats && ((IElemStats) player).isMeditating()) {
            info.setReturnValue(new Vec3d(0, -0.65F, 0));
            info.cancel();
        }
    }
}
