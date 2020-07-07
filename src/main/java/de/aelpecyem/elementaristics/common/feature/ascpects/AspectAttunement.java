package de.aelpecyem.elementaristics.common.feature.ascpects;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MathHelper;

import static de.aelpecyem.elementaristics.lib.Constants.NBTTags.*;

public class AspectAttunement {
    private byte aether, air, earth, fire, water, potential;
    public static final int ATTUNEMENT_CAP = 5;

    public AspectAttunement(byte aether, byte air, byte earth, byte fire, byte water, byte potential) {
        this.aether = aether;
        this.air = air;
        this.earth = earth;
        this.fire = fire;
        this.water = water;
        this.potential = potential;
    }

    public AspectAttunement() {
        this((byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0);
    }

    public byte getAether() {
        return aether;
    }

    public AspectAttunement setAether(byte value) {
        this.aether = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getAir() {
        return air;
    }

    public AspectAttunement setAir(byte value) {
        this.air = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getEarth() {
        return earth;
    }

    public AspectAttunement setEarth(byte value) {
        this.earth = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getFire() {
        return fire;
    }

    public AspectAttunement setFire(byte value) {
        this.fire = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getWater() {
        return water;
    }

    public AspectAttunement setWater(byte value) {
        this.water = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public byte getPotential() {
        return potential;
    }

    public AspectAttunement setPotential(byte value) {
        this.potential = (byte) MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public static AspectAttunement deserialize(CompoundTag tag) {
        return new AspectAttunement(tag.getByte(AETHER_TAG), tag.getByte(AIR_TAG), tag.getByte(EARTH_TAG), tag.getByte(FIRE_TAG), tag.getByte(WATER_TAG), tag.getByte(POTENTIAL_TAG));
    }

    public CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putByte(AETHER_TAG, aether);
        tag.putByte(AIR_TAG, air);
        tag.putByte(EARTH_TAG, earth);
        tag.putByte(FIRE_TAG, fire);
        tag.putByte(WATER_TAG, water);
        tag.putByte(POTENTIAL_TAG, potential);
        return tag;
    }
}
