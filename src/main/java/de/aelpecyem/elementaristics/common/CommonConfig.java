package de.aelpecyem.elementaristics.common;

import blue.endless.jankson.Comment;
import de.aelpecyem.elementaristics.lib.Constants;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name = Constants.MOD_ID + "/common")
public class CommonConfig {
    @Comment(value = "Determines the 1 out of x chance for Morning Glories to attempt spawning in valid chunks")
    public int morningGloryChance = 10;
}
