package de.aelpecyem.elementaristics.common.entity;

import de.aelpecyem.elementaristics.common.feature.ascpects.AspectAttunement;
import de.aelpecyem.elementaristics.lib.ColorHelper;
import de.aelpecyem.elementaristics.lib.Constants.NBTTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;

import static de.aelpecyem.elementaristics.lib.Constants.DataTrackers.*;

public class EntityNexus extends Entity {
    @Environment(EnvType.CLIENT)
    public int[] rgb;
    public int[] targetrgb;

    public EntityNexus(EntityType<? extends Entity> entityType, World world) {
        super(entityType, world);
        targetrgb = new int[3];
        rgb = new int[3];
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(INSTABILITY, 0F);
        this.dataTracker.startTracking(CURRENT_RITE, "");
        this.dataTracker.startTracking(OWNER_UUID, Optional.empty());
        this.dataTracker.startTracking(ATTUNEMENT, new AspectAttunement((byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 0, (byte) 1));
    }


    @Override
    public void tick() {
        super.tick();
        if (submergedInWater) {
            targetrgb[0] = 0;
            targetrgb[1] = 0;
            targetrgb[2] = 255;
        } else {
            targetrgb[0] = 255;
            targetrgb[1] = 0;
            targetrgb[2] = 0;
        }
        if (world.isClient) {
            updateColors();
        }
    }

    @Environment(EnvType.CLIENT)//todo finish color compos method
    private void updateColors() {
        rgb = ColorHelper.blendTowards(rgb, targetrgb, 0.05D);
    }


    @Override
    protected void readCustomDataFromTag(CompoundTag tag) {
        setInstability(tag.getFloat(NBTTags.INSTABILITY_TAG));
        setCurrentRiteString(tag.getString(NBTTags.CURRENT_RITE));
        if (tag.containsUuid(NBTTags.OWNER_UUID_TAG))
            setOwnerUUID(tag.getUuid(NBTTags.OWNER_UUID_TAG));
        else
            dataTracker.set(OWNER_UUID, Optional.empty());
        setAttunement(AspectAttunement.deserialize(tag));
        System.out.println(tag);
        for (int i = 0; i < tag.getIntArray(NBTTags.COLOR_TAG).length; i++) {
            targetrgb[i] = tag.getIntArray(NBTTags.COLOR_TAG)[i];
        }

    }


    @Override
    protected void writeCustomDataToTag(CompoundTag tag) {
        tag.putFloat(NBTTags.INSTABILITY_TAG, getInstability());
        tag.putString(NBTTags.CURRENT_RITE, getCurrentRiteString());
        if (getOwnerUUID().isPresent())
            tag.putUuid(NBTTags.OWNER_UUID_TAG, getOwnerUUID().get());
        getAttunement().serialize(tag);
        tag.putIntArray(NBTTags.COLOR_TAG, targetrgb);
    }

    public int[] getColors() {
        return rgb;
    }

    public float getInstability() {
        return dataTracker.get(INSTABILITY);
    }

    public AspectAttunement getAttunement() {
        return dataTracker.get(ATTUNEMENT);
    }

    public boolean isOwner(PlayerEntity player) {
        Optional<UUID> ownerUUID = getOwnerUUID();
        return ownerUUID.isPresent() && player.getUuid().equals(ownerUUID.get());
    }

    @Nullable
    public PlayerEntity getOwner() {
        Optional<UUID> ownerUUID = getOwnerUUID();
        return ownerUUID.isPresent() ? world.getPlayerByUuid(ownerUUID.get()) : null;
    }

    public Optional<UUID> getOwnerUUID() {
        return dataTracker.get(OWNER_UUID);
    }

    public String getCurrentRiteString() {
        return dataTracker.get(CURRENT_RITE);
    }

    public void setCurrentRiteString(String rite) {
        dataTracker.set(CURRENT_RITE, rite);
    }

    public void setOwnerUUID(UUID ownerUUID) {
        dataTracker.set(OWNER_UUID, ownerUUID == null ? Optional.empty() : Optional.of(ownerUUID));
    }

    public AspectAttunement setAttunement(AspectAttunement attunement) {
        dataTracker.set(ATTUNEMENT, attunement);
        return getAttunement();
    }

    public void setInstability(float instability) {
        dataTracker.set(INSTABILITY, instability);
    }

    @Override
    public boolean doesRenderOnFire() {
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new EntitySpawnS2CPacket(this);
    }
}
