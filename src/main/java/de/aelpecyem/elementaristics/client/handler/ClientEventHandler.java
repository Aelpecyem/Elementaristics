package de.aelpecyem.elementaristics.client.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.StatHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.event.client.ClientTickCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;

public class ClientEventHandler{
    public static void addEvents(){
        HudRenderCallback.EVENT.register(new HudRenderCallback() {
            @Override
            public void onHudRender(MatrixStack matrixStack, float parTick) {
                MinecraftClient minecraft = MinecraftClient.getInstance();
                PlayerEntity player = MinecraftClient.getInstance().player;
                float mult = Math.min(StatHelper.getMagan(player) / (float) StatHelper.getMaxMagan(player), 1);
                if (mult > 0) {
                    int width = (int)(mult * 79F);
                    int posY = minecraft.getWindow().getScaledHeight() - 33;
                    int posX =  minecraft.getWindow().getScaledWidth() / 2 - 91;
                    MinecraftClient.getInstance().getTextureManager().bindTexture(new Identifier(Constants.MOD_NAMESPACE, "textures/gui/hud_elements.png"));
                    if (width > 0) {
                        drawTexture(matrixStack, posX, posY, 2, 0, width, 9);
                        drawTexture(matrixStack, posX + 182 - width, posY, 180- width, 0, width, 9);
                    }
                }
            }
        });
    }


    public static void drawTexture(MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
        drawTexture(matrices, x, y, (float)u, (float)v, width, height, 256, 256);
    }

    public static void drawTexture(MatrixStack matrices, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    private static void drawTexture(MatrixStack matrices, int x0, int y0, int x1, int y1, int regionWidth, int regionHeight, float u, float v, int textureWidth, int textureHeight) {
        drawTexturedQuad(matrices.peek().getModel(), x0, y0, x1, y1, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
    }

    public static void drawTexture(MatrixStack matrices, int x, int y, int width, int height, float u, float v, int regionWidth, int regionHeight, int textureWidth, int textureHeight) {
        drawTexture(matrices, x, x + width, y, y + height, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    private static void drawTexturedQuad(Matrix4f matrices, int x0, int x1, int y0, int y1, float u0, float u1, float v0, float v1) {
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(7, VertexFormats.POSITION_TEXTURE_COLOR);
        bufferBuilder.vertex(matrices, (float)x0, (float)y1, 0).texture(u0, v1).color(0.9F, 1, 1, 1).next();
        bufferBuilder.vertex(matrices, (float)x1, (float)y1, 0).texture(u1, v1).color(0.9F, 1, 1, 1).next();
        bufferBuilder.vertex(matrices, (float)x1, (float)y0, 0).texture(u1, v0).color(0.9F, 1, 1, 1).next();
        bufferBuilder.vertex(matrices, (float)x0, (float)y0, 0).texture(u0, v0).color(0.9F, 1, 1, 1).next();
        bufferBuilder.end();
        RenderSystem.enableAlphaTest();
        BufferRenderer.draw(bufferBuilder);
    }
}
