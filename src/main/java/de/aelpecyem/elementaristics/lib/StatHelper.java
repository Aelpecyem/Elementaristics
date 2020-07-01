package de.aelpecyem.elementaristics.lib;

import de.aelpecyem.elementaristics.common.handler.stats.AscensionPath;
import de.aelpecyem.elementaristics.common.handler.stats.IElemStats;
import de.aelpecyem.elementaristics.registry.ModRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;

public class StatHelper {
    public static void regenMagan(PlayerEntity playerEntity, int modifier){
        if (getMagan(playerEntity) < getMaxMagan(playerEntity)){
            setMagan((int) getMagan(playerEntity) + (getBaseRegenValue(playerEntity) * modifier), playerEntity);
        }
    }
    public static void setMagan(int value, PlayerEntity player){
        ((IElemStats) player).setMagan(MathHelper.clamp(value, 0, getMaxMagan(player)));
    }

    public static float getMagan(PlayerEntity player){
        return ((IElemStats) player).getMagan();
    }

    public static AscensionPath getAscensionPath(PlayerEntity playerEntity){
        try{
            return AscensionPath.PATHS.get(((IElemStats) playerEntity).getAscensionPath());
        }catch (NullPointerException e){
            ((IElemStats) playerEntity).setAscensionPath("standard");
            return ModRegistry.STANDARD_PATH;
        }
    }

    public static byte getAscensionStage(PlayerEntity playerEntity){
        return ((IElemStats) playerEntity).getAscensionStage();
    }

    public static int getMaxMagan(PlayerEntity playerEntity){
        return getAscensionPath(playerEntity).getMaxMagan(playerEntity, (IElemStats) playerEntity);
    }

    public static int getBaseRegenValue(PlayerEntity playerEntity){
        return getAscensionPath(playerEntity).getStandardRegenFactor(playerEntity, (IElemStats) playerEntity);
    }
}
