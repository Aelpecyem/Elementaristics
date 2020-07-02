package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.block.BlockMorningGloryPlant;
import de.aelpecyem.elementaristics.common.item.ItemLiberElementium;
import de.aelpecyem.elementaristics.lib.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Item LIBER_ELEMENTIUM = new ItemLiberElementium();
    public static final Block MORNING_GLORY = new BlockMorningGloryPlant();

    public static void init(){
        Util.register(Registry.ITEM, "liber_elementium", LIBER_ELEMENTIUM);
        Util.registerStandardBlock("morning_glory", MORNING_GLORY);
    }
}
