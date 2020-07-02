package de.aelpecyem.elementaristics.lib;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Util {
    public static <T> T register(Registry<? super T> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Constants.MODID, name), entry);
    }

    public static Block registerStandardBlock(String name, Block block) {
        Registry.register(Registry.ITEM, new Identifier(Constants.MODID, name), new BlockItem(block, new Item.Settings().group(Constants.ELEMENTARISTICS_GROUP)));
        return Registry.register(Registry.BLOCK, new Identifier(Constants.MODID, name), block);
    }
}
