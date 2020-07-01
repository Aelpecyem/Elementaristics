package de.aelpecyem.elementaristics.client.particle;

import de.aelpecyem.elementaristics.registry.ModParticles;
import net.minecraft.client.particle.BillboardParticle;
import net.minecraft.client.particle.EnchantGlyphParticle;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.world.ClientWorld;

public class GlowParticle extends BillboardParticle {
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
}
