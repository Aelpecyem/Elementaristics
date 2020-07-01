package de.aelpecyem.elementaristics.lib;

import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Util {
    public static <T> T register(Registry<? super T> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Constants.MOD_NAMESPACE, name), entry);
    }
}
