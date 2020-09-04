package de.aelpecyem.elementaristics.client.render.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import de.aelpecyem.elementaristics.lib.ColorHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.Random;

public class RenderNexus extends EntityRenderer<EntityNexus> {
    private static final RenderLayer NEXUS;

    public RenderNexus(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(EntityNexus nexus, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider buffer, int light) {
        //could apply slight phosphor shader
        if (nexus.age > 0) {
            float timeFactor = ((float) nexus.age + tickDelta) / 400.0F;
            float riteProgress = 0;
            float intensityFactor = Math.min(timeFactor, 0.3F) + riteProgress; //correlate with rite progress, might make it start smaller
            Random random = new Random(432L);
            VertexConsumer vertexBuilder = buffer.getBuffer(NEXUS);
            matrices.push();
            int[] rgb = ColorHelper.toRGB(nexus.getColor());
            int baseValue = 55 + (nexus.getAttunement().getPotential() * 40); //155 + (potency * 20) more or less
            matrices.translate(0, 0.25, 0);
            GL11.glDepthMask(false);
            RenderSystem.enableAlphaTest();
            int amount = 10;
            for (int i = 0; i < amount; ++i) {
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(random.nextFloat() * 360.0F));
                matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(random.nextFloat() * 360.0F + timeFactor * 90.0F));

                //might re-add the random size bonus, or calculate size completely differently (maybe a multiplier * randomFactor * generalFactor)
                float length = intensityFactor * 10.0F;//random.nextFloat() * 20.0F + 5.0F + intensityFactor * 10.0F; //possibly length, originally 10
                float width = intensityFactor * 3.0F; //random.nextFloat() * 1.5F + 1.0F + intensityFactor * 2.0F; //possibly width, originally 2
                Matrix4f matrix = matrices.peek().getModel();

                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseValue, baseValue, baseValue, 255).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseValue, baseValue, baseValue, 255).next();
                vertexBuilder.vertex(matrix, -0.86602540378F * width, length, -0.5F * width).color(rgb[0], rgb[1], rgb[2], 50).next();

                vertexBuilder.vertex(matrix, 0.86602540378F * width, length, -0.5F * width).color(rgb[0], rgb[1], rgb[2], 50).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseValue, baseValue, baseValue, 255).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseValue, baseValue, baseValue, 255).next();

                vertexBuilder.vertex(matrix, 0.86602540378F * width, length, -0.5F * width).color(rgb[0], rgb[1], rgb[2], 50).next();
                vertexBuilder.vertex(matrix, 0.0F, length, 1.0F * width).color(rgb[0], rgb[1], rgb[2], 50).next();
                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseValue, baseValue, baseValue, 255).next();

                vertexBuilder.vertex(matrix, 0.0F, 0.0F, 0.0F).color(baseValue, baseValue, baseValue, 255).next();
                vertexBuilder.vertex(matrix, 0.0F, length, 1.0F * width).color(rgb[0], rgb[1], rgb[2], 50).next();
                vertexBuilder.vertex(matrix, -0.86602540378F * width, length, -0.5F * width).color(rgb[0], rgb[1], rgb[2], 50).next();
            }
            GL11.glDepthMask(true);
            RenderSystem.disableAlphaTest();
            matrices.pop();
        }
        super.render(nexus, yaw, tickDelta, matrices, buffer, light);
    }

    @Override
    protected void renderLabelIfPresent(EntityNexus entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.renderLabelIfPresent(entity, text, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(EntityNexus entity) {
        return null;
    }

    static {
        NEXUS = RenderLayer.of("elem_nexus", VertexFormats.POSITION_COLOR, 7, 256, false, true, RenderLayer.MultiPhaseParameters.builder()
                .transparency(new RenderPhase.Transparency("lightning_transparency", () -> {
                    RenderSystem.enableBlend();
                    GL11.glDepthMask(false);
                    RenderSystem.enableAlphaTest();
                    RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE);
                }, () -> {
                    RenderSystem.disableBlend();
                    GL11.glDepthMask(true);
                    RenderSystem.disableAlphaTest();
                    RenderSystem.defaultBlendFunc();
                })).cull(new RenderPhase.Cull(true)).shadeModel(new RenderPhase.ShadeModel(true)).build(false));
    }
}
