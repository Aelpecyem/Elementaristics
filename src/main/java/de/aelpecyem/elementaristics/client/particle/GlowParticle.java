package de.aelpecyem.elementaristics.client.particle;

import de.aelpecyem.elementaristics.registry.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class GlowParticle extends SpriteBillboardParticle {
    public GlowParticle(ClientWorld world, double x, double y, double z, double velX, double velY, double velZ) {
        super(world, x, y, z, velX, velY, velZ);
    }

    @Override
    public ParticleTextureSheet getType() { //re-add the particle types, bind the glow texture to them
        return ModParticles.RenderTypes.BRIGHT;
    }

    @Override
    protected float getMinU() {
        return 0;
    }

    @Override
    protected float getMaxU() {
        return 1;
    }

    @Override
    protected float getMinV() {
        return 0;
    }

    @Override
    protected float getMaxV() {
        return 1;
    }


    @Environment(EnvType.CLIENT)
    public static class GlowFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;

        public GlowFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            GlowParticle glowParticle = new GlowParticle(clientWorld, d, e, f, g, h, i);
            glowParticle.setSprite(this.spriteProvider);
            return glowParticle;
        }
    }


}
