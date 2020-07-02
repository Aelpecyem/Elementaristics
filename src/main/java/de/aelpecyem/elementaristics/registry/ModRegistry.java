package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.feature.stats.AscensionPath;

public class ModRegistry {
    public static AscensionPath STANDARD_PATH = new AscensionPath("standard");

    public static void init(){
        STANDARD_PATH = new AscensionPath("standard");
        ModObjects.init();
        ModCommands.registerCommands();
    }
}
