package de.aelpecyem.elementaristics.lib;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Hand;
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

    public static boolean tryExtractItemWithContainer(PlayerEntity player, Hand hand, Inventory inventory, int slot) {
        ItemStack targetStack = inventory.getStack(slot);
        if (targetStack.getItem().getRecipeRemainder() != null && !targetStack.getItem().getRecipeRemainder().equals(player.getStackInHand(hand).getItem())) {
            player.sendMessage(new TranslatableText("message.elementaristics.extract_item.requirement", targetStack.getItem().getRecipeRemainder().getName()), true);
            return false;
        } else if (targetStack.getItem().getRecipeRemainder() != null) player.getStackInHand(hand).decrement(1);
        Util.giveItem(player.world, player, inventory.removeStack(slot, 1));
        return true;
    }
}
