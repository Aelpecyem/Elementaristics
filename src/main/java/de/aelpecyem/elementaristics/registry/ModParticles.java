package de.aelpecyem.elementaristics.registry;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.serialization.Codec;
import de.aelpecyem.elementaristics.client.particle.type.MagicParticleEffect;
import de.aelpecyem.elementaristics.lib.Util;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ModParticles {
    public static List<ParticleTextureSheet> ELEM_SHEETS = Arrays.asList(ParticleTextureSheets.BRIGHT, ParticleTextureSheets.DARK);
    public static ParticleType<MagicParticleEffect> GLOW = register("glow", MagicParticleEffect.PARAMETERS_FACTORY, MagicParticleEffect::getCodec);
    //todo add ambient glow that fades in and out

    private static <T extends ParticleEffect> ParticleType<T> register(String name, ParticleEffect.Factory<T> factory, final Function<ParticleType<T>, Codec<T>> function) {
        return Util.register(Registry.PARTICLE_TYPE, name, new ParticleType<T>(false, factory) {
            public Codec<T> method_29138() {
                return function.apply(this);
            }
        });
    }

    public static class Helper {
        public static void spawnDefaultBlockParticles(World world, BlockPos pos, int color) {
            double motionX = world.random.nextGaussian() * 0.001D;
            double motionY = world.random.nextGaussian() * 0.001D;
            double motionZ = world.random.nextGaussian() * 0.001D;
            spawnBlockParticles(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, 0.5F, color, 0.5F, 0.5F, motionX, motionY, motionZ);
        }

        //this could also be a special type that fades in/out
        public static void spawnBlockParticles(World world, double posX, double posY, double posZ, float size, int color, float spreadXZ, float spreadY, double motionX, double motionY, double motionZ) {
            world.addParticle(new MagicParticleEffect(GLOW, new MagicParticleEffect.MagicParticleInfo(size, color)), posX + world.random.nextGaussian() * spreadXZ, posY + world.random.nextGaussian() * spreadY, posZ + world.random.nextGaussian() * spreadXZ, motionX, motionY, motionZ);
        }

        public static void spawnDirectedBurst(Entity from, Entity to, int color, int size) {
            //new extension of the glow particle that uses the velocity in the factory as coordinates to move to
        }
    }

    public static class ParticleTextureSheets {
        public static ParticleTextureSheet BRIGHT = new ParticleTextureSheet() {
            @Override
            public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
                RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
                RenderSystem.disableLighting();

                textureManager.bindTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX);
                textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).setFilter(true, false);

                bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
            }

            @Override
            public void draw(Tessellator tessellator) {
                tessellator.draw();
            }

            @Override
            public String toString() {
                return "ELEM_SHEET_BRIGHT";
            }
        };

        public static ParticleTextureSheet DARK = new ParticleTextureSheet() {
            @Override
            public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
                RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
                RenderSystem.disableLighting();

                textureManager.bindTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX);
                textureManager.getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).setFilter(true, false);

                bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_TEXTURE_COLOR_LIGHT);
            }

            @Override
            public void draw(Tessellator tessellator) {
                tessellator.draw();
            }

            @Override
            public String toString() {
                return "ELEM_SHEET_DARK";
            }
        };
    }
}
