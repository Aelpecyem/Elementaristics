package de.aelpecyem.elementaristics.lib;

import de.aelpecyem.elementaristics.registry.ModObjects;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Constants {
    public static String MODID = "elementaristics";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup ELEMENTARISTICS_GROUP = FabricItemGroupBuilder.build(
            new Identifier(MODID, "group"),
            () -> new ItemStack(ModObjects.LIBER_ELEMENTIUM));

    public static class NBTTags {
        public static final String MAGAN_TAG = "Magan";
        public static final String ASCENSION_PATH = "Ascension Path";
        public static final String ASCENSION_STAGE = "Ascension Stage";

        public static final String RITE_MODE = "Rite Mode";
    }

    public static class DataTrackers {
        public static final TrackedData<Integer> MAGAN = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Byte> ASCENSION_STAGE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BYTE);
        public static final TrackedData<String> ASCENSION_PATH = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
    }

    public static class Colors {
        public static final int MAGAN_COLOR = 0xE39400;
    }
}
