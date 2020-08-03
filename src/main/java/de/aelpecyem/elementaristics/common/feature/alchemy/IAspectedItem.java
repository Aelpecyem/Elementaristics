package de.aelpecyem.elementaristics.common.feature.alchemy;

import net.minecraft.item.ItemStack;

public interface IAspectedItem {
    AspectAttunement getAttunement(ItemStack stack);

    default boolean doesConsume(ItemStack stack) {
        return true;
    }
}
