package de.aelpecyem.elementaristics.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class Util {
    public static <T> T register(Registry<? super T> registry, String name, T entry) {
        return Registry.register(registry, new Identifier(Constants.MOD_ID, name), entry);
    }

    public static Block registerStandardBlock(String name, Block block) {
        Registry.register(Registry.ITEM, new Identifier(Constants.MOD_ID, name), new BlockItem(block, new Item.Settings().group(Constants.ELEMENTARISTICS_GROUP)));
        return Registry.register(Registry.BLOCK, new Identifier(Constants.MOD_ID, name), block);
    }

    public static void giveItem(World world, PlayerEntity player, ItemStack item) {
        if (!world.isClient)
            world.spawnEntity(new ItemEntity(world, player.getX(), player.getY() + 0.5, player.getZ(), item)); //might spawn those more efficiently
    }
}
