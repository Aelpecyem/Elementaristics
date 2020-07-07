package de.aelpecyem.elementaristics.common.entity;

import de.aelpecyem.elementaristics.lib.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

import java.util.Optional;

public class EntityNexus extends Entity {

    @Environment(EnvType.CLIENT)
    public int[] rgb = new int[3];

    public EntityNexus(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
        rgb[0] = 0;
        rgb[1] = 255;
        rgb[2] = 255;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(Constants.DataTrackers.INSTABILITY, 0F);
        this.dataTracker.startTracking(Constants.DataTrackers.CURRENT_RITE, "");
        this.dataTracker.startTracking(Constants.DataTrackers.OWNER_UUID, Optional.empty());
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {

    }

    @Override
    public boolean shouldRender(double distance) {
        return super.shouldRender(distance);
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {

    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }


    public int[] getColors() {
        return rgb;
    }
}
