package de.aelpecyem.elementaristics.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import de.aelpecyem.elementaristics.client.particle.type.MagicParticleEffect;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.StatHelper;
import de.aelpecyem.elementaristics.registry.ModParticles;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

public class ClientEventHandler{
    public static void addEvents(){
        ClientTickCallback.EVENT.register(new ClientTickCallback() {
            @Override
            public void tick(MinecraftClient mc) {
                if (mc != null && mc.player != null) {
                    ShaderHandler.handleShaders(mc);
                    mc.player.world.addParticle(new MagicParticleEffect(ModParticles.GLOW, new MagicParticleEffect.MagicParticleInfo(1)), mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0);

                    //   mc.particleManager.addParticle(new GlowParticle((ClientWorld) mc.player.world, mc.player.getX(), mc.player.getY(), mc.player.getZ(), 0, 0, 0));
                }
            }
        });
        HudRenderCallback.EVENT.register((matrixStack, parTick) -> {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            PlayerEntity player = MinecraftClient.getInstance().player;
            float mult = Math.min(StatHelper.getMagan(player) / (float) StatHelper.getMaxMagan(player), 1);
            if (mult > 0) {
                int width = (int) (mult * 79F);
                int posY = minecraft.getWindow().getScaledHeight() - 33;
                int posX = minecraft.getWindow().getScaledWidth() / 2 - 91;
                MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(Constants.MODID, "textures/gui/hud_elements.png"));
                if (width > 0) {
                    drawTexture(matrixStack, posX, posY, 2, 0, width, 9, Constants.Colors.MAGAN_COLOR, 0.9F);
                    drawTexture(matrixStack, posX + 182 - width, posY, 180 - width, 0, width, 9, Constants.Colors.MAGAN_COLOR, 0.9F);
                }
            }

            ShaderHandler.renderShaders(matrixStack, parTick);
        });
    }


    public static void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height, int color, float alpha) {
        drawTexture(matrices, x, y, (float)u, (float)v, width, height, 256, 256, color, alpha);
    }

    public static void drawTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight, int color, float alpha) {
        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight, color, alpha);
    }

    private static void drawTexture(MatrixStack matrices, int x0, int y0, int x1, int y1, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight, int color, float alpha) {
        drawTexturedQuad(matrices.peek().getModel(), x0, y0, x1, y1, 0, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight, color, alpha);
    }

    public static void drawTexture(MatrixStack matrices, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight, int color, float alpha) {
        drawTexture(matrices, x, x + width, y, y + height, regionWidth, regionHeight, u, v, textureWidth, textureHeight, color, alpha);
    }

    private static void drawTexturedQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, float z, float u0, float u1, float v0, float v1, int color, float alpha) {
        float red = ((color >> 16) & 255) / 255F;
        float green = ((color >> 8) & 255) / 255F;
        float blue = (color & 255) / 255F;
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrices, (float)x0, (float)y1, z).texture(u0, v1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(matrices, (float)x1, (float)y1, z).texture(u1, v1).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(matrices, (float)x1, (float)y0, z).texture(u1, v0).color(red, green, blue, alpha).next();
        bufferBuilder.vertex(matrices, (float)x0, (float)y0, z).texture(u0, v0).color(red, green, blue, alpha).next();
        bufferBuilder.end();
        RenderSystem.enableAlphaTest();
        BufferRenderer.draw(bufferBuilder);
    }
}
