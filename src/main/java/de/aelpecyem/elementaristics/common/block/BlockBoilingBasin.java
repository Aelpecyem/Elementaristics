package de.aelpecyem.elementaristics.common.block;

import de.aelpecyem.elementaristics.common.block.blockentity.BlockEntityBoilingBasin;
import de.aelpecyem.elementaristics.lib.Util;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import javax.annotation.Nullable;
import java.util.Random;

import static net.minecraft.state.property.Properties.LIT;
import static net.minecraft.state.property.Properties.WATERLOGGED;

public class BlockBoilingBasin extends HorizontalFacingBlock implements Waterloggable, BlockEntityProvider {
    public static final VoxelShape SHAPE = createCuboidShape(3, 0, 3, 13, 11, 13);

    public BlockBoilingBasin() {
        super(Settings.of(Material.METAL).nonOpaque().requiresTool().strength(1F, 4F)
                .allowsSpawning((state, world, pos, type) -> false).solidBlock((state, world, pos) -> false)
                .suffocates((state, world, pos) -> false)
                .blockVision((state, world, pos) -> false)
                .lightLevel(state -> state.get(LIT) ? 10 : 0));
        setDefaultState(getStateManager().getDefaultState().with(FACING, Direction.NORTH).with(LIT, false).with(WATERLOGGED, false));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Environment(EnvType.CLIENT)
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT, FACING, WATERLOGGED);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.isOf(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof BlockEntityBoilingBasin) {
                if (!(((BlockEntityBoilingBasin) blockEntity).getStack(0).isEmpty() || ((BlockEntityBoilingBasin) blockEntity).getStack(0).getItem().getRecipeRemainder() != null))
                    ItemScatterer.spawn(world, pos, ((BlockEntityBoilingBasin) blockEntity).getItems());
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack stack = player.getStackInHand(hand);
        BlockEntityBoilingBasin blockEntity = (BlockEntityBoilingBasin) world.getBlockEntity(pos);
        if (stack.getItem() instanceof FlintAndSteelItem && !state.get(LIT) && !state.get(WATERLOGGED)) {
            stack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
            world.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.4F + 0.8F);
            world.setBlockState(pos, state.with(LIT, blockEntity.canBeLit(player)));
            return ActionResult.SUCCESS;
        } else {
            if (player.isSneaking() && blockEntity.isLit()) {
                world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.6F, 0.5F + world.random.nextFloat());
                world.setBlockState(pos, state.with(LIT, false));
                blockEntity.cooldownTicks = 0;
                blockEntity.markDirty();
            } else if (!blockEntity.getStack(0).isEmpty() && Util.tryExtractItemWithContainer(player, hand, blockEntity, 0)) {
                return ActionResult.SUCCESS;
            } else if (blockEntity.canPlayerUse(player)) {
                ItemStack resultStack = blockEntity.tryAddItem(player, stack);
                if (resultStack.equals(stack)) return ActionResult.FAIL;
                player.setStackInHand(hand, resultStack);
                blockEntity.markDirty();
                return ActionResult.CONSUME;
            }
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        final BlockState state = this.getDefaultState().with(FACING, ctx.getPlayerFacing());
        if (state.contains(WATERLOGGED)) {
            final FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
            final boolean source = fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8;
            return state.with(WATERLOGGED, source);
        }
        return state;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        if (state.contains(WATERLOGGED) && state.get(WATERLOGGED)) {
            world.getFluidTickScheduler().schedule(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
        }

        return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (state.get(LIT)) {
            if (random.nextBoolean()) {
                world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5F + random.nextGaussian() / 3F, pos.getY() + 0.5F + random.nextGaussian() / 4F, pos.getZ() + 0.5F + random.nextGaussian() / 3F, 0, 0, 0);
            } else {
                BlockEntity basin = world.getBlockEntity(pos);
                if (basin instanceof BlockEntityBoilingBasin && ((BlockEntityBoilingBasin) basin).getAspects().getAspectSaturation() > 0) {
                    ((BlockEntityBoilingBasin) basin).doParticles();
                }
            }
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.contains(WATERLOGGED) && state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        if (!state.get(Properties.WATERLOGGED) && fluidState.getFluid() == Fluids.WATER) {
            if (!world.isClient()) {
                world.setBlockState(pos, state.with(Properties.WATERLOGGED, true).with(LIT, false), 3);
                world.getFluidTickScheduler().schedule(pos, fluidState.getFluid(), fluidState.getFluid().getTickRate(world));
            }
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockView world) {
        return new BlockEntityBoilingBasin();
    }
}
