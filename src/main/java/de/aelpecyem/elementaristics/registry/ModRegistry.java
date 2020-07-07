package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.feature.potion.StatusEffectIntoxicated;
import de.aelpecyem.elementaristics.common.feature.stats.AscensionPath;
import de.aelpecyem.elementaristics.lib.Util;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.util.registry.Registry;

public class ModRegistry {
    public static AscensionPath STANDARD_PATH = new AscensionPath("standard");

    public static final StatusEffect INTOXICATED = new StatusEffectIntoxicated();
    public static void init(){
        Util.register(Registry.STATUS_EFFECT, "intoxicated", INTOXICATED);
        STANDARD_PATH = new AscensionPath("standard");
        ModObjects.init();
        ModWorld.init();
        ModEntities.init();
        ModCommands.registerCommands();
    }
}
