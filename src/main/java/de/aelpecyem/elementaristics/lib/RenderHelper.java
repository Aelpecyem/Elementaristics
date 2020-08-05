package de.aelpecyem.elementaristics.lib;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.util.math.Matrix4f;

public class RenderHelper {
    public static void drawPlane(Matrix4f mat, VertexConsumer buffer, Sprite sprite, int light, int overlay) {
        buffer.vertex(mat, 0, 0, 1).color(1F, 1F, 1F, 1F).texture(sprite.getMinU(), sprite.getMaxV()).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, 1, 0, 1).color(1F, 1F, 1F, 1F).texture(sprite.getMaxU(), sprite.getMaxV()).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, 1, 0, 0).color(1F, 1F, 1F, 1F).texture(sprite.getMaxU(), sprite.getMinV()).light(light).overlay(overlay).normal(1, 1, 1).next();
        buffer.vertex(mat, 0, 0, 0).color(1F, 1F, 1F, 1F).texture(sprite.getMinU(), sprite.getMinV()).light(light).overlay(overlay).normal(1, 1, 1).next();
    }

    public static boolean isDark(float r, float g, float b) {
        return isDark(r, g, b, 0.5F);
    }

    public static float getLuminance(float r, float g, float b) {
        return 0.299F * r + 0.587F * g + 0.114F * b;
    }

    public static boolean isDark(float r, float g, float b, float threshold) {
        return getLuminance(r, g, b) < threshold;
    }

    public static int[] blendTowards(int from, int to, double percentage) {
        return blendTowards(toRGB(from), toRGB(to), percentage);
    }

    public static int[] blendTowards(int[] from, int[] to, double percentage) {
        int[] rgb = new int[3];
        rgb[0] = (int) (from[0] * (1 - percentage) + to[0] * percentage);
        rgb[1] = (int) (from[1] * (1 - percentage) + to[1] * percentage);
        rgb[2] = (int) (from[2] * (1 - percentage) + to[2] * percentage);
        return rgb;
    }

    public static int[] toRGB(int decimalColor) {
        return new int[]{(decimalColor >> 16) & 0xff, (decimalColor >> 8) & 0xff, decimalColor & 0xff};
    }
}
