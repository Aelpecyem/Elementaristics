package de.aelpecyem.elementaristics.client.mixin;

import de.aelpecyem.elementaristics.common.feature.stats.IElemStats;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityModel.class)
public abstract class PlayerModelMixin extends BipedEntityModel {
    public PlayerModelMixin(float scale) {
        super(scale);
    }

    @Inject(at = @At("TAIL"), method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V")
    private void setMeditateAngles(LivingEntity entity, float f, float g, float h, float i, float j, CallbackInfo info) {
        if (entity instanceof IElemStats && ((IElemStats) entity).isMeditating()) {
            this.rightLeg.pitch = -1.5F;
            this.rightLeg.yaw = 0.31415927F;
            this.rightLeg.roll = 0.07853982F;
            this.leftLeg.pitch = -1.5F;
            this.leftLeg.yaw = -0.31415927F;
            this.leftLeg.roll = -0.07853982F;
        }
    }
}
