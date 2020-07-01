package de.aelpecyem.elementaristics.common.handler.stats;

import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.Map;

public class AscensionPath {
    public static final Map<String, AscensionPath> PATHS = new HashMap<>();
    protected String name;

    public AscensionPath(String name) {
        this.name = name;
        PATHS.put(name, this);
    }

    public int getMaxMagan(PlayerEntity player, IElemStats stats){
        return (int) (100 * (1 + ((Math.sqrt(stats.getAscensionStage())))));
    }

    public int getStandardRegenFactor(PlayerEntity playerEntity, IElemStats stats){
        return (int) (stats.getAscensionStage() / 2F) + 1;
    }
}
