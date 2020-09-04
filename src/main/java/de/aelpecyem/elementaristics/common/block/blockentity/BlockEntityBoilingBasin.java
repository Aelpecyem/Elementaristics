package de.aelpecyem.elementaristics.common.block.blockentity;

import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.common.handler.AlchemyHandler;
import de.aelpecyem.elementaristics.lib.Util;
import de.aelpecyem.elementaristics.registry.ModObjects;
import de.aelpecyem.elementaristics.registry.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

import static de.aelpecyem.elementaristics.lib.Constants.NBTTags.*;

public class BlockEntityBoilingBasin extends BlockEntityBase implements ImplementedInventory, Tickable {
    public static final int ITEM_COUNT_CAP = 5;
    private final DefaultedList<ItemStack> ITEMS = DefaultedList.ofSize(1, ItemStack.EMPTY);
    private AspectAttunement aspects = new AspectAttunement();
    public int itemCount;
    public int cooldownTicks;
    @Environment(EnvType.CLIENT)
    public int currentColor = 0xFFFFFF;
    public static final int MAX_COOLDOWN_TICKS = 60;

    public BlockEntityBoilingBasin() {
        super(ModObjects.BOILING_BASIN_BLOCK_ENTITY_TYPE);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        Inventories.toTag(tag, ITEMS);
        aspects.serialize(tag);
        tag.putInt(ITEM_COUNT, itemCount);
        tag.putInt(TICK_COUNT, cooldownTicks);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        Inventories.fromTag(tag, ITEMS);
        aspects = AspectAttunement.deserialize(tag);
        itemCount = tag.getInt(ITEM_COUNT);
        cooldownTicks = tag.getInt(TICK_COUNT);
        super.fromTag(state, tag);
    }

    @Override
    public void tick() {
        if (getStack(0).isEmpty() && !isLit() && aspects.getAspectSaturation() > 0 && aspects.getPotential() > 0) {
            cooldownTicks++;
            if (world.isClient) doParticles();
            if (cooldownTicks >= MAX_COOLDOWN_TICKS) {
                cleanUp();
            }
            markDirty();
        }
    }

    public void doParticles() {
        if (world.random.nextBoolean())
            ModParticles.Helper.spawnBlockParticles(world, pos.getX() + 0.5F, pos.getY() + 0.7F, pos.getZ() + 0.5F, 0.5F, currentColor, 0.15F, 0.1F, world.random.nextGaussian() * 0.01D, world.random.nextFloat() * 0.03D, world.random.nextGaussian() * 0.01D);
    }

    private void cleanUp() {
        setStack(0, AlchemyHandler.Helper.stabilize(AlchemyHandler.Helper.createAlchemicalMatter(new ItemStack(ModObjects.ALCHEMICAL_MATTER), aspects)));
        aspects = new AspectAttunement();
        cooldownTicks = 0;
        itemCount = 0;
    }

    public ItemStack tryAddItem(PlayerEntity player, ItemStack stack) {
        ItemStack remainderStack = ItemStack.EMPTY;
        if (stack.getItem().getRecipeRemainder() != null)
            remainderStack = new ItemStack(stack.getItem().getRecipeRemainder());
        if (isLit() && (!AlchemyHandler.Helper.convertToAlchemyItem(stack).equals(stack) || (stack.hasTag() && stack.getTag().contains(ELEM_DATA))) && itemCount < ITEM_COUNT_CAP) {
            aspects.addAspects(AlchemyHandler.Helper.getAttunement(stack));
            if (itemCount < 1 && world.isClient)
                currentColor = AlchemyHandler.Helper.getColorForWorldTick(aspects, world.getLevelProperties().getTime());
            stack.decrement(1);
            if (!remainderStack.isEmpty())
                Util.giveItem(world, player, remainderStack);
            itemCount++;
            markDirty();
        }
        return stack;
    }

    public boolean canBeLit(PlayerEntity player) {
        return getStack(0).isEmpty();
    }

    public boolean isLit() {
        return world.getBlockState(pos).get(Properties.LIT);
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return !isLit();
    }

    @Override
    public DefaultedList<ItemStack> getItems() {
        return ITEMS;
    }

    public AspectAttunement getAspects() {
        return aspects;
    }
}
