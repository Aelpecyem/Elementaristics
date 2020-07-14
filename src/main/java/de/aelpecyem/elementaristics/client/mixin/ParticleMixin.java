package de.aelpecyem.elementaristics.client.mixin;

import de.aelpecyem.elementaristics.registry.ModParticles;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.*;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

@Mixin(ParticleManager.class)
public class ParticleMixin {
    @Shadow
    private Map<ParticleTextureSheet, Queue<Particle>> particles;
    @Shadow
    private TextureManager textureManager;

    @Inject(at = @At(value = "INVOKE", target = "java/util/List.iterator ()Ljava/util/Iterator;"), method = "renderParticles(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider$Immediate;Lnet/minecraft/client/render/LightmapTextureManager;Lnet/minecraft/client/render/Camera;F)V")
    private void renderElemParticles(MatrixStack matrixStack, VertexConsumerProvider.Immediate immediate, LightmapTextureManager lightmapTextureManager, Camera camera, float f, CallbackInfo info) {
        Iterator iterator = ModParticles.ELEM_SHEETS.iterator();
        while (iterator.hasNext()) {
            ParticleTextureSheet particleTextureSheet = (ParticleTextureSheet) iterator.next();
            if (particles.containsKey(particleTextureSheet)) {
                Tessellator tessellator = Tessellator.getInstance();
                BufferBuilder bufferBuilder = tessellator.getBuffer();
                particleTextureSheet.begin(bufferBuilder, MinecraftClient.getInstance().getTextureManager());
                Iterator<Particle> queue = this.particles.get(particleTextureSheet).iterator();
                while (queue.hasNext()) {
                    Particle particle = queue.next();
                    try {
                        particle.buildGeometry(bufferBuilder, camera, f);
                    } catch (Throwable var16) {
                        CrashReport crashReport = CrashReport.create(var16, "Rendering Elementaristics Particle");
                        CrashReportSection crashReportSection = crashReport.addElement("Particle being rendered");
                        crashReportSection.add("Particle", particle::toString);
                        crashReportSection.add("Particle Type", particleTextureSheet::toString);
                        throw new CrashException(crashReport);
                    }
                }

                particleTextureSheet.draw(tessellator);
                textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).setFilter(false, true);
            }
        }
    }
}
