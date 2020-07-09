package de.aelpecyem.elementaristics.common.feature.ascpects;

import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.Constants.IDs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MathHelper;

public class AspectAttunement {
    public byte[] aspects = new byte[6]; //aether, air, earth, fire, water, potential;
    public static final int ATTUNEMENT_CAP = 5;

    public AspectAttunement(byte aether, byte fire, byte water, byte earth, byte air, byte potential) {
        this.aspects[IDs.AETHER_ID] = aether;
        this.aspects[IDs.FIRE_ID] = fire;
        this.aspects[IDs.WATER_ID] = water;
        this.aspects[IDs.EARTH_ID] = earth;
        this.aspects[IDs.AIR_ID] = air;
        this.aspects[IDs.POTENTIAL_ID] = potential;
    }

    public AspectAttunement(byte[] aspects) {
        for (int i = 0; i < aspects.length; i++) {
            this.aspects[i] = aspects[i];
        }
    }

    public AspectAttunement() {
        this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
    }

    public byte getAether() {
        return aspects[IDs.AETHER_ID];
    }

    public AspectAttunement setAether(byte value) {
        this.aspects[IDs.AETHER_ID] = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getAir() {
        return aspects[IDs.AIR_ID];
    }

    public AspectAttunement setAir(byte value) {
        this.aspects[IDs.AIR_ID] = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getEarth() {
        return aspects[IDs.EARTH_ID];
    }

    public AspectAttunement setEarth(byte value) {
        this.aspects[IDs.EARTH_ID] = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getFire() {
        return aspects[IDs.FIRE_ID];
    }

    public AspectAttunement setFire(byte value) {
        this.aspects[IDs.FIRE_ID] = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getWater() {
        return aspects[IDs.WATER_ID];
    }

    public AspectAttunement setWater(byte value) {
        this.aspects[IDs.WATER_ID] = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getPotential() {
        return aspects[IDs.POTENTIAL_ID];
    }

    public AspectAttunement setPotential(byte value) {
        this.aspects[IDs.POTENTIAL_ID] = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public CompoundTag serialize(CompoundTag tag) {
        tag.putByteArray("Aspects", aspects);
        return tag;
    }

    public static AspectAttunement deserialize(CompoundTag tag) {
        return new AspectAttunement(tag.getByteArray("Aspects"));
    }

    public static int getAspectColor(int aspectId) {
        switch (aspectId) {
            case IDs.AETHER_ID:
                return Constants.Colors.AETHER_COLOR;
            case IDs.FIRE_ID:
                return Constants.Colors.FIRE_COLOR;
            case IDs.WATER_ID:
                return Constants.Colors.WATER_COLOR;
            case IDs.EARTH_ID:
                return Constants.Colors.EARTH_COLOR;
            case IDs.AIR_ID:
                return Constants.Colors.AIR_COLOR;
            default:
                return Constants.Colors.POTENTIAL_COLOR;

        }
    }
}
