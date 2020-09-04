package de.aelpecyem.elementaristics.common.feature.alchemy;

import de.aelpecyem.elementaristics.lib.ColorHelper;
import de.aelpecyem.elementaristics.lib.Constants;

public class Aspect {
    private final int id;
    private final String name;
    private final int color;

    public Aspect(int id, String name, int color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getColor(int potential) {
        return ColorHelper.blendTowards(Constants.Colors.POTENTIAL_LOW, color, potential / (float) AspectAttunement.ATTUNEMENT_CAP);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
