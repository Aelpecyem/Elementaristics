package de.aelpecyem.elementaristics.common.feature.alchemy;

public class Aspect {
    private final int id;
    private final String name;
    private final int relativeBoiling;

    public Aspect(int id, String name, int relativeBoiling) {
        this.id = id;
        this.name = name;
        this.relativeBoiling = relativeBoiling;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRelativeBoiling() {
        return relativeBoiling;
    }
}
