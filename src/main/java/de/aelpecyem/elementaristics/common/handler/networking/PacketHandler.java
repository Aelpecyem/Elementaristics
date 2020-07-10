package de.aelpecyem.elementaristics.common.handler.networking;

import de.aelpecyem.elementaristics.common.feature.stats.IElemStats;
import de.aelpecyem.elementaristics.lib.Constants;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.util.Identifier;

public class PacketHandler {
    public static final Identifier MEDITATE_PACKET = new Identifier(Constants.MODID, "meditate");

    public static void registerClientToServerPackets() {
        ServerSidePacketRegistry.INSTANCE.register(MEDITATE_PACKET, (packetContext, packetByteBuf) -> {
            boolean meditates = packetByteBuf.readBoolean();
            packetContext.getTaskQueue().execute(() -> ((IElemStats) packetContext.getPlayer()).setMeditating(meditates));
        });
    }

    public static void registerServerToClientPackets() {

    }
}
