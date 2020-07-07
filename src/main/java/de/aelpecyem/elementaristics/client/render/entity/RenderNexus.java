package de.aelpecyem.elementaristics.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import javafx.scene.paint.Color;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderNexus extends EntityRenderer<EntityNexus> {
    private static final RenderPhase.Transparency NEXUS_TRANSPARENCY = new RenderPhase.Transparency("lightning_transparency", () -> {
        RenderSystem.enableBlend();
        GL11.glDepthMask(false);
        RenderSystem.enableAlphaTest();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        GL11.glDepthMask(true);
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
    });
    private static RenderLayer NEXUS = RenderLayer.of("elem_nexus", VertexFormats.POSITION_COLOR, 7, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
            .transparency(NEXUS_TRANSPARENCY).cull(new RenderPhase.Cull(true)).shadeModel(new RenderPhase.ShadeModel(true)).build(false));

    //todo remove all usage of java.awt classes
    public RenderNexus(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(EntityNexus nexus, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider buffer, int light) {
        if (nexus.age > 0) {
            float animState = 20;//50F - Math.abs(50F - ((float) nexus.age % 100F));//ranges from 0 - 50
            int alpha = (int) (255 * (1 - (0.1F + animState / 1000F)));

            float timeFactor = ((float) nexus.age + tickDelta) / 200.0F; //10 = nexus.age
            float intensityFactor = timeFactor; //change that later
            Random random = new Random(432L);
            VertexConsumer vertexBuilder = buffer.getBuffer(NEXUS);
            matrices.push();
            Color baseColor = Color.BLACK; //changes with potential: pale -> black
            Color targetColor = Color.RED; //changes with aspects
            matrices.translate(0, 0.5, 0);
            GL11.glDepthMask(false);
            RenderSystem.enableAlphaTest();
            int amount = 10;
            for (int i = 0; i < amount; ++i) {
                //might just steal the buffer stuff from the 1.12 version, because apparently I don't need to do GL magic here
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0F + timeFactor * 90.0F));
                float length = random.nextFloat() * 20.0F + 5.0F + intensityFactor * 10.0F; //possibly length, originally 10
                float width = random.nextFloat() * 2.0F + 1.0F + intensityFactor * 3.0F; //possibly width, originally 2
                Matrix4f matrix = matrices.peek().getModel();

                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color((float) baseColor.getRed(), (float) baseColor.getGreen(), (float) baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color((float) baseColor.getRed(), (float) baseColor.getGreen(), (float) baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, -0.86602540378F * width, length, -0.5F * width).color((float) targetColor.getRed(), (float) targetColor.getGreen(), (float) targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.86602540378F * width, length, -0.5F * width).color((float) targetColor.getRed(), (float) targetColor.getGreen(), (float) targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color((float) baseColor.getRed(), (float) baseColor.getGreen(), (float) baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color((float) baseColor.getRed(), (float) baseColor.getGreen(), (float) baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.86602540378F * width, length, -0.5F * width).color((float) targetColor.getRed(), (float) targetColor.getGreen(), (float) targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.0F, length, 1.0F * width).color((float) targetColor.getRed(), (float) targetColor.getGreen(), (float) targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color((float) baseColor.getRed(), (float) baseColor.getGreen(), (float) baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color((float) baseColor.getRed(), (float) baseColor.getGreen(), (float) baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, length, 1.0F * width).color((float) targetColor.getRed(), (float) targetColor.getGreen(), (float) targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, -0.86602540378F * width, length, -0.5F * width).color((float) targetColor.getRed(), (float) targetColor.getGreen(), (float) targetColor.getBlue(), 0).next();
            }
            GL11.glDepthMask(true);
            RenderSystem.disableAlphaTest();
            matrices.pop();
        }
        super.render(nexus, yaw, tickDelta, matrices, buffer, light);
    }

    @Override
    public Identifier getTexture(EntityNexus entity) {
        return null;
    }
}
