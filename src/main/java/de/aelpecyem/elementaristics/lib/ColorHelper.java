package de.aelpecyem.elementaristics.lib;

public class ColorHelper {
    public static boolean isDark(float r, float g, float b) {
        //change this to consider luminescence of grayscale or so
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
        rgb[1] = (int) (from[1] * (1 - percentage) + to[2] * percentage);
        rgb[2] = (int) (from[2] * (1 - percentage) + to[2] * percentage);
        return rgb;
    }

    public static int[] toRGB(int decimalColor) {
        return new int[]{(decimalColor >> 16) & 0xff, (decimalColor >> 8) & 0xff, decimalColor & 0xff};
    }
}
