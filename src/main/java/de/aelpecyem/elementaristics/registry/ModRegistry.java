package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.handler.stats.AscensionPath;

public class ModRegistry {
    public static AscensionPath STANDARD_PATH = new AscensionPath("standard");

    public static void init(){
        STANDARD_PATH = new AscensionPath("standard");
        ModObjects.init();
    }
}
