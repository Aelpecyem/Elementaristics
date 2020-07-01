package de.aelpecyem.elementaristics.registry;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.Identifier;
import org.lwjgl.opengl.GL11;

public class ModParticles {
    public static class RenderTypes{
        public static final Identifier GLOW_PARTICLE_TEX = new Identifier(Constants.MOD_NAMESPACE, "textures/item/liber_elementium.png");

        public static ParticleTextureSheet BRIGHT = new ParticleTextureSheet() {
            @Override
            public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
                RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
                RenderSystem.disableLighting();

                textureManager.bindTexture(GLOW_PARTICLE_TEX);
                textureManager.getTexture(GLOW_PARTICLE_TEX).setFilter(true, false);

                bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
            }

            @Override
            public void draw(Tessellator tessellator) {
                tessellator.draw();
               // MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).re();
                RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
            }
        };

        public static ParticleTextureSheet DARKEN = new ParticleTextureSheet() {
            @Override
            public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
                RenderSystem.depthMask(false);
                RenderSystem.enableBlend();
                RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
                RenderSystem.alphaFunc(GL11.GL_GREATER, 0.003921569F);
                RenderSystem.disableLighting();

                textureManager.bindTexture(GLOW_PARTICLE_TEX);
                textureManager.getTexture(GLOW_PARTICLE_TEX).setFilter(true, false);

                bufferBuilder.begin(GL11.GL_QUADS, VertexFormats.POSITION_COLOR_TEXTURE_LIGHT);
            }

            @Override
            public void draw(Tessellator tessellator) {
                tessellator.draw();
                // MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.PARTICLE_ATLAS_TEX).re();
                RenderSystem.alphaFunc(GL11.GL_GREATER, 0.1F);
                RenderSystem.disableBlend();
                RenderSystem.depthMask(true);
            }
        };
    }
}
