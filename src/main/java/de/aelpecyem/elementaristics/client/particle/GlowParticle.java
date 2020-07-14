package de.aelpecyem.elementaristics.client.particle;

import de.aelpecyem.elementaristics.client.particle.type.MagicParticleEffect;
import de.aelpecyem.elementaristics.lib.ColorHelper;
import de.aelpecyem.elementaristics.registry.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.world.ClientWorld;

import javax.annotation.Nullable;

public class GlowParticle extends SpriteBillboardParticle {
    public GlowParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ) {
        super(world, x, y, z, velX, velY, velZ);
        colorRed = 0.1F;
        colorBlue = 0.1F;
        colorGreen = 0.1F;
    }

    @Override
    public ParticleTextureSheet getType() {
        return isBright() ? ModParticles.ParticleTextureSheets.BRIGHT : ModParticles.ParticleTextureSheets.DARK; //ParticleTextureSheet.PARTICLE_SHEET_LIT;//ColorHelper.isDark(colorRed, colorGreen, colorBlue) ? ModParticles.RenderTypes.DARK : ModParticles.RenderTypes.BRIGHT;
    }

    private boolean isBright() {
        return !ColorHelper.isDark(colorRed, colorGreen, colorBlue);
    }


    @Environment(EnvType.CLIENT)
    public static class GlowFactory implements ParticleFactoryRegistry.PendingParticleFactory<MagicParticleEffect> {
        @Override
        public ParticleFactory<MagicParticleEffect> create(FabricSpriteProvider fabricSpriteProvider) {
            return new ParticleFactory<MagicParticleEffect>() {
                @Nullable
                @Override
                public Particle createParticle(MagicParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
                    GlowParticle glowParticle = new GlowParticle(world, x, y, z, velocityX, velocityY, velocityZ);
                    glowParticle.setSprite(fabricSpriteProvider.getSprites().get(0));
                    return glowParticle;
                }
            };
        }
    }

}
