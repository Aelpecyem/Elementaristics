package de.aelpecyem.elementaristics.client.mixin;

import de.aelpecyem.elementaristics.registry.ModRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HUDMixin extends DrawableHelper {
    @Inject(at = @At("HEAD") , method = "renderPortalOverlay(F)V", cancellable = true)
    public void noRenderPortalOverlay(float f, CallbackInfo info){
        if (MinecraftClient.getInstance().player.hasStatusEffect(ModRegistry.INTOXICATED)) info.cancel();
    }
}
