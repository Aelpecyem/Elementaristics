package de.aelpecyem.elementaristics.lib;

import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import de.aelpecyem.elementaristics.common.feature.ascpects.AspectAttunement;
import de.aelpecyem.elementaristics.registry.ModObjects;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.UUID;

public class Constants {
    public static String MODID = "elementaristics";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final ItemGroup ELEMENTARISTICS_GROUP = FabricItemGroupBuilder.build(
            new Identifier(MODID, "group"),
            () -> new ItemStack(ModObjects.LIBER_ELEMENTIUM));

    public static class IDs {
        public static final int AETHER_ID = 0;
        public static final int FIRE_ID = 1;
        public static final int WATER_ID = 2;
        public static final int EARTH_ID = 3;
        public static final int AIR_ID = 4;
        public static final int POTENTIAL_ID = 5;
    }


    public static class NBTTags {
        public static final String MEDITATING = "Meditating";
        public static final String MEDITATE_TICKS = "Ticks Meditating";
        public static final String MAGAN_TAG = "Magan";

        public static final String ASCENSION_PATH = "Ascension Path";
        public static final String ASCENSION_STAGE = "Ascension Stage";

        public static final String INSTABILITY_TAG = "Instability";
        public static final String OWNER_UUID_TAG = "Owner UUID";
        public static final String CURRENT_RITE = "Current Rite";
        public static final String RITE_MODE = "Rite Mode";

        public static final String COLOR_TAG = "Color";
    }

    public static class DataTrackers {
        public static final TrackedDataHandler<AspectAttunement> ATTUNEMENT_TRACKER = new TrackedDataHandler<AspectAttunement>() {
            @Override
            public void write(PacketByteBuf data, AspectAttunement attunement) {
                data.writeByteArray(attunement.aspects);
            }

            @Override
            public AspectAttunement read(PacketByteBuf packetByteBuf) {
                return new AspectAttunement(packetByteBuf.readByteArray());
            }

            @Override
            public AspectAttunement copy(AspectAttunement attunement) {
                return attunement;
            }
        };

        public static final TrackedData<Integer> MAGAN = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.INTEGER);
        public static final TrackedData<Byte> ASCENSION_STAGE = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BYTE);
        public static final TrackedData<String> ASCENSION_PATH = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.STRING);
        public static final TrackedData<Boolean> MEDITATING = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

        public static final TrackedData<AspectAttunement> ATTUNEMENT = DataTracker.registerData(EntityNexus.class, ATTUNEMENT_TRACKER);
        public static final TrackedData<Float> INSTABILITY = DataTracker.registerData(EntityNexus.class, TrackedDataHandlerRegistry.FLOAT);
        public static final TrackedData<String> CURRENT_RITE = DataTracker.registerData(EntityNexus.class, TrackedDataHandlerRegistry.STRING);
        public static final TrackedData<Optional<UUID>> OWNER_UUID = DataTracker.registerData(EntityNexus.class, TrackedDataHandlerRegistry.OPTIONAL_UUID);
    }

    public static class Colors {
        public static final int MAGAN_COLOR = 0xE39400;

        public static final int AETHER_COLOR = 0x9855FF;
        public static final int FIRE_COLOR = 0xFF5500;
        public static final int WATER_COLOR = 0x0000EA;
        public static final int EARTH_COLOR = 0x008700;
        public static final int AIR_COLOR = 0x00FFFF;
        public static final int POTENTIAL_COLOR = 0xE1E1E1;
    }

    public static class Misc {
        public static final EntityDimensions MEDITATING_DIMENSIONS = new EntityDimensions(0.75F, 1.2F, true);
    }
}
