package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.block.BlockMorningGloryPlant;
import de.aelpecyem.elementaristics.common.block.BlockMorningGloryVine;
import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.common.item.ItemLiberElementium;
import de.aelpecyem.elementaristics.common.item.rite.ItemRiteItem;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Item LIBER_ELEMENTIUM = new ItemLiberElementium();

    public static final Item ESSENCE_NOTHINGNESS = new ItemRiteItem(new Item.Settings().group(Constants.ELEMENTARISTICS_GROUP), new AspectAttunement(0, 0, 0, 0, 0, 1));

    public static final Block MORNING_GLORY = new BlockMorningGloryPlant();
    public static final Block MORNING_GLORY_VINES = new BlockMorningGloryVine();

    public static void init() {
        Util.register(Registry.ITEM, "essence_nothingness", ESSENCE_NOTHINGNESS); //really need to add a suitable debug item that can add any aspect
        Util.register(Registry.ITEM, "liber_elementium", LIBER_ELEMENTIUM);
        Util.registerStandardBlock("morning_glory", MORNING_GLORY);
        Util.registerStandardBlock("morning_glory_vines", MORNING_GLORY_VINES);
    }
}
