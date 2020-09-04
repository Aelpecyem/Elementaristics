package de.aelpecyem.elementaristics.common.feature.alchemy;

import com.google.gson.JsonObject;
import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.MathHelper;

import java.util.*;

import static de.aelpecyem.elementaristics.common.handler.AlchemyHandler.*;

public class AspectAttunement {
    private int[] aspects = new int[6]; //aether, air, earth, fire, water, potential;
    public static final int ATTUNEMENT_CAP = 5;

    public AspectAttunement(int aether, int fire, int water, int earth, int air, int potential) {
        setAspect(AETHER, aether);
        setAspect(FIRE, fire);
        setAspect(WATER, water);
        setAspect(EARTH, earth);
        setAspect(AIR, air);
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

    public static AspectAttunement read(JsonObject object) {
        return new AspectAttunement(JsonHelper.getInt(object, "aether"), JsonHelper.getInt(object, "fire"), JsonHelper.getInt(object, "water"), JsonHelper.getInt(object, "earth"), JsonHelper.getInt(object, "air"), JsonHelper.getInt(object, "potential"));
    }


    public AspectAttunement addAspects(AspectAttunement aspectAttunement) {
        ASPECT_LIST.forEach(aspect -> setAspect(aspect, getAspect(aspect) + aspectAttunement.getAspect(aspect)));
        setPotential(Math.max(aspectAttunement.getPotential(), getPotential()));
        return this;
    }

    public int getAspect(Aspect aspect) {
        return aspects[aspect.getId()];
    }

    public AspectAttunement setAspect(Aspect aspect, int value) {
        this.aspects[aspect.getId()] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public int getPotential() {
        return aspects[5];
    }

    public AspectAttunement setPotential(int value) {
        this.aspects[5] = MathHelper.clamp(value, 0, ATTUNEMENT_CAP);
        return this;
    }

    public CompoundTag serialize(CompoundTag tag) {
        tag.putIntArray(Constants.NBTTags.ASPECTS, aspects);
        return tag;
    }

    public static AspectAttunement deserialize(CompoundTag tag) {
        return new AspectAttunement(tag.getIntArray(Constants.NBTTags.ASPECTS));
    }

    @Override
    public String toString() {
        return String.format("AETHER %d FIRE %d WATER %d EARTH %d AIR %d POTENTIAL %d", aspects[0], aspects[1], aspects[2], aspects[3], aspects[4], aspects[5]);
    }

    protected static final String[] arabicToRomanNumeral = {" - ", " I ", " I I ", " III ", " I V ", " V "};

    public Text toText() {
        LiteralText resultText = new LiteralText("");
        ASPECT_LIST.forEach(aspect -> {
            LiteralText text = new LiteralText(arabicToRomanNumeral[getAspect(aspect)]);
            text.fillStyle(Style.EMPTY.withColor(TextColor.fromRgb(aspect.getColor())));
            resultText.append(text);
        });
        resultText.fillStyle(Style.EMPTY.withColor(TextColor.fromRgb(Constants.Colors.POTENTIAL_COLOR)));
        resultText.append(arabicToRomanNumeral[getPotential()]);
        return resultText.fillStyle(resultText.getStyle().withBold(true));
    }

    public int[] getAspects() {
        return aspects;
    }

    public float getAspectSaturation() {
        int totalAspect = 0;
        for (Aspect aspect : ASPECT_LIST) {
            totalAspect += getAspect(aspect);
        }
        return totalAspect / 25F;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AspectAttunement that = (AspectAttunement) o;
        return Arrays.equals(aspects, that.aspects);
    }

    public Aspect getDominantAspect() {
        return sortyByPrevalence().get(0);
    }

    public List<Aspect> sortyByPrevalence() {
        List<Aspect> list = new LinkedList<>(ASPECT_LIST);
        list.sort(Comparator.comparingInt(aspect -> getAspect((Aspect) aspect)).reversed());
        return list;
    }

    public Integer[] getColorCycle() {
        List<Integer> cycleList = new ArrayList<>();
        ASPECT_LIST.forEach(aspect -> {
            for (int i = 0; i < getAspect(aspect); i++) {
                cycleList.add(aspect.getColor(getPotential()));
            }
        });
        return cycleList.toArray(new Integer[cycleList.size()]);
    }
}
