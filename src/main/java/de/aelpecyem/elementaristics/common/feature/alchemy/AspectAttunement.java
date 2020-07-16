package de.aelpecyem.elementaristics.common.feature.alchemy;

import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.Constants.IDs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.MathHelper;

public class AspectAttunement {
    public int[] aspects = new int[6]; //aether, air, earth, fire, water, potential;
    public static final int ATTUNEMENT_CAP = 5;

    public AspectAttunement(int aether, int fire, int water, int earth, int air, int potential) {
        setAether(aether);
        setFire(fire);
        setWater(water);
        setEarth(earth);
        setAir(air);
        setPotential(potential);
    }

    public AspectAttunement(int[] aspects) {
        for (int i = 0; i < aspects.length; i++) {
            this.aspects[i] = aspects[i];
        }
    }

    public AspectAttunement() {
        this(0, 0, 0, 0, 0, 0);
    }


    public AspectAttunement addAspects(AspectAttunement aspectAttunement) {
        setAether(getAether() + aspectAttunement.getAether());
        setFire(getFire() + aspectAttunement.getFire());
        setWater(getWater() + aspectAttunement.getWater());
        setEarth(getEarth() + aspectAttunement.getEarth());
        setAir(getAir() + aspectAttunement.getAir());
        setPotential(getPotential() + aspectAttunement.getPotential());
        return this;
    }

    public int getAether() {
        return aspects[IDs.AETHER_ID];
    }

    public AspectAttunement setAether(int value) {
        this.aspects[IDs.AETHER_ID] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public int getAir() {
        return aspects[IDs.AIR_ID];
    }

    public AspectAttunement setAir(int value) {
        this.aspects[IDs.AIR_ID] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public int getEarth() {
        return aspects[IDs.EARTH_ID];
    }

    public AspectAttunement setEarth(int value) {
        this.aspects[IDs.EARTH_ID] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public int getFire() {
        return aspects[IDs.FIRE_ID];
    }

    public AspectAttunement setFire(int value) {
        this.aspects[IDs.FIRE_ID] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public int getWater() {
        return aspects[IDs.WATER_ID];
    }

    public AspectAttunement setWater(int value) {
        this.aspects[IDs.WATER_ID] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public int getPotential() {
        return aspects[IDs.POTENTIAL_ID];
    }

    public AspectAttunement setPotential(int value) {
        this.aspects[IDs.POTENTIAL_ID] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public CompoundTag serialize(CompoundTag tag) {
        tag.putIntArray("Aspects", aspects);
        return tag;
    }

    public static AspectAttunement deserialize(CompoundTag tag) {
        return new AspectAttunement(tag.getIntArray("Aspects"));
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

    @Override
    public String toString() {
        return String.format("AETHER %d FIRE %d WATER %d EARTH %d AIR %d POTENTIAL %d", aspects[0], aspects[1], aspects[2], aspects[3], aspects[4], aspects[5]);
    }
}
