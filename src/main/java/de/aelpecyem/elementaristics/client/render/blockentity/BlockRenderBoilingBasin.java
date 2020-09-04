package de.aelpecyem.elementaristics.client.render.blockentity;

import de.aelpecyem.elementaristics.common.block.blockentity.BlockEntityBoilingBasin;
import de.aelpecyem.elementaristics.common.handler.AlchemyHandler;
import de.aelpecyem.elementaristics.lib.ColorHelper;
import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Matrix4f;

public class BlockRenderBoilingBasin extends BlockEntityRenderer<BlockEntityBoilingBasin> {
    public static final SpriteIdentifier BOILING_FLUID = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEX, new Identifier(Constants.MOD_ID, "misc/fluid"));

    public BlockRenderBoilingBasin(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(BlockEntityBoilingBasin entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        int aboveLight = WorldRenderer.getLightmapCoordinates(entity.getWorld(), entity.getPos().up());
        matrices.push();
        Direction direction = entity.getWorld().getBlockState(entity.getPos()).get(HorizontalFacingBlock.FACING);
        matrices.translate(0.5, 0.25, 0.5);
        matrices.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(direction.asRotation()));

        matrices.push();
        matrices.translate(-0.25, 0, -0.25);
        drawFluidCube(entity, vertexConsumers.getBuffer(RenderLayer.getCutoutMipped()), matrices, BOILING_FLUID.getSprite(), aboveLight, overlay);
        matrices.pop();

        matrices.push();
        matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(90));
        matrices.scale(0.7F, 0.7F, 0.7F);
        matrices.translate(0, -0.1, -0.1);
        if (entity.getStack(0).getItem().getRecipeRemainder() == null)
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.GROUND, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
        matrices.pop();
        matrices.pop();
    }

    public static void drawFluidCube(BlockEntityBoilingBasin entity, VertexConsumer buffer, MatrixStack matStack, Sprite sprite, int light, int overlay) {
        if (entity.itemCount > 0 || (!entity.getStack(0).isEmpty() && entity.getStack(0).getItem().getRecipeRemainder() != null)) {
            float heightFactor = entity.itemCount > 0 ? Math.min(entity.itemCount - Math.min(entity.cooldownTicks / (float) BlockEntityBoilingBasin.MAX_COOLDOWN_TICKS, 0.85F), 2) / 2F : 0.10F;
            float height = 0.7F * heightFactor;
            if (entity.itemCount > 0)
                entity.currentColor = ColorHelper.blendTowards(entity.currentColor, AlchemyHandler.Helper.getColorForWorldTick(entity.getAspects(), entity.getWorld().getLevelProperties().getTime()), 0.03F); //ColorHelper.blendTowards(entity.currentColor, AlchemyHandler.Helper.getColorForWorldTick(entity.getAspects(), entity.getWorld().getLevelProperties().getTime()), 0.05F);
            matStack.translate(0, 0, 0.5);
            matStack.scale(0.5F, 0.5F, 0.5F);
            Matrix4f mat = matStack.peek().getModel();
            for (int i = 0; i < 4; i++) {
                matStack.translate(0, 0, -1);
                drawPlane(buffer, mat, sprite, light, overlay, height, heightFactor, entity.currentColor);
                mat.multiply(Vector3f.NEGATIVE_Y.getDegreesQuaternion(90));
            }
            mat.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(90));
            drawPlane(buffer, mat, sprite, light, overlay, 1, 1, entity.currentColor);
            matStack.translate(0, 1, height);
            mat.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(180));
            drawPlane(buffer, mat, sprite, light, overlay, 1, 1, entity.currentColor);
        }
    }

    private static void drawPlane(VertexConsumer buffer, Matrix4f mat, Sprite sprite, int light, int overlay, float height, float heightFactor, int rgbDecimal) {
        int[] rgb = ColorHelper.toRGB(rgbDecimal);
        float maxV = (sprite.getMaxV() - sprite.getMinV()) * heightFactor;
        buffer.vertex(mat, 0, height, 0).color(rgb[0], rgb[1], rgb[2], 225).texture(sprite.getMinU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, 1, height, 0).color(rgb[0], rgb[1], rgb[2], 225).texture(sprite.getMaxU(), sprite.getMinV() + maxV).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, 1, 0, 0).color(rgb[0], rgb[1], rgb[2], 225).texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, 0, 0, 0).color(rgb[0], rgb[1], rgb[2], 225).texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(1, 1, 1).next();

    }
}
