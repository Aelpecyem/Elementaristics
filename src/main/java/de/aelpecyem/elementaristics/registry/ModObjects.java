package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.block.BlockBoilingBasin;
import de.aelpecyem.elementaristics.common.block.BlockMorningGloryPlant;
import de.aelpecyem.elementaristics.common.block.BlockMorningGloryVine;
import de.aelpecyem.elementaristics.common.block.blockentity.BlockEntityBoilingBasin;
import de.aelpecyem.elementaristics.common.item.ItemLiberElementium;
import de.aelpecyem.elementaristics.common.item.aspect.ItemAlchemy;
import de.aelpecyem.elementaristics.common.item.aspect.ItemRiteDebugger;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.Util;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Item LIBER_ELEMENTIUM = new ItemLiberElementium();

    public static final Item ALCHEMICAL_MATTER = new ItemAlchemy(new Item.Settings().group(Constants.ELEMENTARISTICS_GROUP));
    public static final Item SULFUR = new ItemAlchemy(new Item.Settings().group(Constants.ELEMENTARISTICS_GROUP));
    public static final Item MERCURY = new ItemAlchemy(new Item.Settings().group(Constants.ELEMENTARISTICS_GROUP).recipeRemainder(Items.GLASS_BOTTLE));
    public static final Item ASBESTOS = new ItemAlchemy(new Item.Settings().group(Constants.ELEMENTARISTICS_GROUP));

    public static final Item RITE_DEBUGGER = new ItemRiteDebugger();

    public static final Block MORNING_GLORY = new BlockMorningGloryPlant();
    public static final Block MORNING_GLORY_VINES = new BlockMorningGloryVine();

    public static final Block BOILING_BASIN = new BlockBoilingBasin();
    public static final BlockEntityType<BlockEntityBoilingBasin> BOILING_BASIN_BLOCK_ENTITY_TYPE = BlockEntityType.Builder.create(BlockEntityBoilingBasin::new, BOILING_BASIN).build(null);


    public static void init() {
        Util.register(Registry.ITEM, "alchemical_matter", ALCHEMICAL_MATTER);
        Util.register(Registry.ITEM, "liber_elementium", LIBER_ELEMENTIUM);
        Util.register(Registry.ITEM, "sulfur", SULFUR);
        Util.register(Registry.ITEM, "mercury", MERCURY);
        Util.register(Registry.ITEM, "asbestos", ASBESTOS);
        Util.register(Registry.ITEM, "rite_debugger", RITE_DEBUGGER);

        Util.register(Registry.BLOCK_ENTITY_TYPE, "boiling_basin", BOILING_BASIN_BLOCK_ENTITY_TYPE);
        Util.registerStandardBlock("boiling_basin", BOILING_BASIN);
        Util.registerStandardBlock("morning_glory", MORNING_GLORY);
        Util.registerStandardBlock("morning_glory_vines", MORNING_GLORY_VINES);
    }
}
