package de.aelpecyem.elementaristics.client.render.entity;

import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

public class RenderNexus extends EntityRenderer<EntityNexus> {
    //todo remove all usage of java.awt classes
    public RenderNexus(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(EntityNexus nexus, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider buffer, int light) {
        matrices.push();
        if (nexus.age > 0) {
            float animState = 20;//50F - Math.abs(50F - ((float) nexus.age % 100F));//ranges from 0 - 50
            int alpha = (int) (255 * (1 - (0.1F + animState / 1000F)));

            float timeFactor = ((float) nexus.age + tickDelta) / 200.0F; //10 = nexus.age
            float intensityFactor = timeFactor; //change that later
            Random random = new Random(432L);
            VertexConsumer vertexBuilder = buffer.getBuffer(RenderLayer.getLightning());
            matrices.push();
            GL11.glDepthMask(true);
            Color baseColor = Color.BLACK; //changes with potential: pale -> black
            Color targetColor = Color.RED; //changes with aspects
            matrices.translate(0, 0, 0);
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

                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, -0.86602540378F * width, length, -0.5F * width).color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.86602540378F * width, length, -0.5F * width).color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.86602540378F * width, length, -0.5F * width).color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.0F, length, 1.0F * width).color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), alpha).next();
                vertexBuilder.vertex(matrix, 0.0F, length, 1.0F * width).color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), 0).next();
                vertexBuilder.vertex(matrix, -0.86602540378F * width, length, -0.5F * width).color(targetColor.getRed(), targetColor.getGreen(), targetColor.getBlue(), 0).next();
            }
            GL11.glDepthMask(false);

            matrices.pop();
        }
        matrices.pop();
        super.render(nexus, yaw, tickDelta, matrices, buffer, light);
    }

    @Override
    public Identifier getTexture(EntityNexus entity) {
        return null;
    }
}
