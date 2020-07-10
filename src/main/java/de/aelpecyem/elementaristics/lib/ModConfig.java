package de.aelpecyem.elementaristics.lib;

import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;

public class ModConfig {
    public static void init() {//might use Fiber2Cloth
        ConfigTree.builder()
                .fork("colors")
                .withValue("Air Color", ConfigTypes.INTEGER, Constants.Colors.AIR_COLOR); //add stuff that later
    }

    //client
    public static boolean shaders = true;

    public static int morning_glory_chance = 10;
}
