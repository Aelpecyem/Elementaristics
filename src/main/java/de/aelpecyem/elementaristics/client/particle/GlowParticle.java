package de.aelpecyem.elementaristics.client.particle;

import de.aelpecyem.elementaristics.client.particle.type.MagicParticleEffect;
import de.aelpecyem.elementaristics.lib.RenderHelper;
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
    public GlowParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ, MagicParticleEffect.MagicParticleInfo info) {
        super(world, x, y, z, velX, velY, velZ);
        scale *= info.getSize();
        velocityX = velX;
        velocityY = velY;
        velocityZ = velZ;
        colorRed = (((info.getColor() >> 16) & 255) / 255F);
        colorGreen = (((info.getColor() >> 8) & 255) / 255F);
        colorBlue = ((info.getColor() & 255) / 255F);
        maxAge = 120;
    }

    @Override
    public void tick() {
        float lifeRatio = (float) this.age / (float) this.maxAge;
        this.colorAlpha = colorAlpha - (lifeRatio * colorAlpha);
        super.tick();
    }

    @Override
    public ParticleTextureSheet getType() {
        return isBright() ? ModParticles.ParticleTextureSheets.BRIGHT : ModParticles.ParticleTextureSheets.DARK;
    }

    private boolean isBright() {
        return !RenderHelper.isDark(colorRed, colorGreen, colorBlue);
    }


    @Environment(EnvType.CLIENT)
    public static class GlowFactory implements ParticleFactoryRegistry.PendingParticleFactory<MagicParticleEffect> {
        @Override
        public ParticleFactory<MagicParticleEffect> create(FabricSpriteProvider fabricSpriteProvider) {
            return new ParticleFactory<MagicParticleEffect>() {
                @Nullable
                @Override
                public Particle createParticle(MagicParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
                    GlowParticle glowParticle = new GlowParticle(world, x, y, z, velocityX, velocityY, velocityZ, parameters.getInfo());
                    glowParticle.setSprite(fabricSpriteProvider.getSprites().get(0));
                    return glowParticle;
                }
            };
        }
    }

}
