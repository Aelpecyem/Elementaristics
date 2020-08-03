package de.aelpecyem.elementaristics.common.handler;

import de.aelpecyem.elementaristics.common.feature.alchemy.AlchemyItem;
import de.aelpecyem.elementaristics.common.feature.alchemy.Aspect;
import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.registry.ModObjects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.stream.Collectors;

import static de.aelpecyem.elementaristics.lib.Constants.Colors.*;
import static de.aelpecyem.elementaristics.lib.Constants.NBTTags.*;

public class AlchemyHandler {
    public static final Random SELECTOR_RANDOM = new Random(26051967L);
    public static final List<Aspect> ASPECT_LIST = new LinkedList<>();
    public static final Map<Identifier, AlchemyItem> ALCHEMY_ITEMS = new HashMap<>();

    public static final Aspect AETHER = new Aspect(0, "aether", AETHER_COLOR, 1);
    public static final Aspect FIRE = new Aspect(1, "fire", FIRE_COLOR, 0);
    public static final Aspect WATER = new Aspect(2, "water", WATER_COLOR, 3);
    public static final Aspect EARTH = new Aspect(3, "earth", EARTH_COLOR, 4);
    public static final Aspect AIR = new Aspect(4, "air", AIR_COLOR, 2);

    public static void init() {
        ASPECT_LIST.add(AETHER.getId(), AETHER);
        ASPECT_LIST.add(FIRE.getId(), FIRE);
        ASPECT_LIST.add(WATER.getId(), WATER);
        ASPECT_LIST.add(EARTH.getId(), EARTH);
        ASPECT_LIST.add(AIR.getId(), AIR);
    }

    public static class Helper {
        public static AspectAttunement[] splitAttunement(AspectAttunement attunement) {
            AspectAttunement[] attunements = new AspectAttunement[5];
            ASPECT_LIST.forEach(aspect -> {
                int[] values = new int[6];
                Arrays.fill(values, 0);
                values[aspect.getId()] = attunement.getAspect(aspect);
                values[5] = attunement.getPotential();
            });
            return attunements;
        }

        public static AspectAttunement[] sortByBoil(AspectAttunement[] attunements) {
            Arrays.sort(attunements, Comparator.comparingInt(aspect -> aspect.getDominantAspect().getRelativeBoiling()));
            return attunements;
        }

        public static AspectAttunement mergeAttunement(AspectAttunement... attunement) {
            AspectAttunement addAttunement = new AspectAttunement();
            for (AspectAttunement aspectAttunement : attunement) {
                addAttunement.addAspects(aspectAttunement);
            }
            return addAttunement;
        }

        public static ItemStack convertToAlchemyItem(ItemStack stack) {
            ItemStack finalStack = stack;
            List<AlchemyItem> items = AlchemyHandler.ALCHEMY_ITEMS.values().stream().filter(alchemyItem -> alchemyItem.isDestabilizable() && alchemyItem.getValidItems().test(finalStack)).collect(Collectors.toList());
            if (!items.isEmpty()) {
                AlchemyItem item = items.get(SELECTOR_RANDOM.nextInt(items.size()));
                ItemStack originalStack = stack;
                stack = new ItemStack(ModObjects.ALCHEMICAL_MATTER, stack.getCount());
                stack.setTag(originalStack.getTag());
                setOriginStack(stack, originalStack);
                setAttunement(stack, item.getAspects());
            }
            return stack;
        }

        public static ItemStack convertToOriginalItem(ItemStack stack) {
            checkTag(stack);
            if (!getAlchemyData(stack).contains(ORIGIN_ITEM)) return stack;
            return getOriginStack(stack);
        }

        public static ItemStack stabilize(ItemStack stack) {
            checkTag(stack);
            List<AlchemyItem> items = AlchemyHandler.ALCHEMY_ITEMS.values().stream().filter(alchemyItem -> alchemyItem.isStabilizable() && alchemyItem.getAspects().equals(getAttunement(stack))).collect(Collectors.toList());
            if (!items.isEmpty()) {
                AlchemyItem item = items.get(SELECTOR_RANDOM.nextInt(items.size()));
                ItemStack stabilizeItem = new ItemStack(item.getStabilizeItem(), stack.getCount());
                stabilizeItem.setTag(stack.getTag());
                return stabilizeItem;
            }
            return stack;
        }

        public static void setOriginStack(ItemStack stack, ItemStack original) {
            checkTag(stack);
            getAlchemyData(stack).put(ORIGIN_ITEM, original.toTag(new CompoundTag()));
        }

        public static ItemStack getOriginStack(ItemStack stack) {
            checkTag(stack);
            if (!getAlchemyData(stack).contains(ORIGIN_ITEM)) return stack;
            return ItemStack.fromTag((CompoundTag) getAlchemyData(stack).get(ORIGIN_ITEM));
        }

        public static void setAttunement(ItemStack stack, AspectAttunement attunement) {
            checkTag(stack);
            attunement.serialize(getAlchemyData(stack));
        }

        public static AspectAttunement getAttunement(ItemStack stack) {
            checkTag(stack);
            if (!getAlchemyData(stack).contains(ASPECTS)) return new AspectAttunement();
            return AspectAttunement.deserialize(getAlchemyData(stack));
        }

        public static CompoundTag getAlchemyData(ItemStack stack) {
            return (CompoundTag) stack.getTag().get(ELEM_DATA);
        }


        public static void checkTag(ItemStack stack) {
            if (!stack.hasTag()) {
                stack.setTag(new CompoundTag());
                stack.getTag().put(ELEM_DATA, new CompoundTag());
            }
        }
    }
}
