package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.block.BlockMorningGloryPlant;
import de.aelpecyem.elementaristics.common.block.BlockMorningGloryVine;
import de.aelpecyem.elementaristics.common.item.ItemLiberElementium;
import de.aelpecyem.elementaristics.common.item.aspect.ItemAlchemy;
import de.aelpecyem.elementaristics.common.item.aspect.ItemRiteDebugger;
import de.aelpecyem.elementaristics.lib.Util;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Item LIBER_ELEMENTIUM = new ItemLiberElementium();

    public static final Item ALCHEMICAL_MATTER = new ItemAlchemy();

    public static final Item RITE_DEBUGGER = new ItemRiteDebugger();

    public static final Block MORNING_GLORY = new BlockMorningGloryPlant();
    public static final Block MORNING_GLORY_VINES = new BlockMorningGloryVine();

    public static void init() {
        Util.register(Registry.ITEM, "alchemical_matter", ALCHEMICAL_MATTER);
        Util.register(Registry.ITEM, "liber_elementium", LIBER_ELEMENTIUM);
        Util.register(Registry.ITEM, "rite_debugger", RITE_DEBUGGER);

        Util.registerStandardBlock("morning_glory", MORNING_GLORY);
        Util.registerStandardBlock("morning_glory_vines", MORNING_GLORY_VINES);
    }
}
