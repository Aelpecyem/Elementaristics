package de.aelpecyem.elementaristics.common.block;

import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.registry.ModObjects;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ShearsItem;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Random;

import static net.minecraft.block.ConnectingBlock.*;

public class BlockMorningGloryVine extends BlockMorningGloryPlant implements Fertilizable {
    public static final BooleanProperty FINISHED = BooleanProperty.of("finished");

    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);

    public BlockMorningGloryVine() {
        super();
        this.setDefaultState(this.stateManager.getDefaultState().with(SOUTH, false).with(NORTH, false).with(EAST, false).with(WEST, false).with(AGE, 0).with(FINISHED, false));
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (player.getStackInHand(hand).getItem() instanceof ShearsItem && world instanceof ServerWorld) {
            if (isMature(state)) {
                LootTable table = world.getServer().getLootManager().getTable(new Identifier(Constants.MODID, "blocks/morning_glory_sheared"));
                LootContext.Builder builder = (new LootContext.Builder((ServerWorld) world)).random(world.random).parameter(LootContextParameters.POSITION, pos).parameter(LootContextParameters.TOOL, player.getStackInHand(hand)).optionalParameter(LootContextParameters.THIS_ENTITY, player).parameter(LootContextParameters.BLOCK_STATE, state);
                table.generateLoot(builder.build(LootContextTypes.BLOCK)).forEach(stack -> dropStack(world, pos, stack));
                world.setBlockState(pos, state.with(AGE, 0));
                world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.8F, 0.9F);
                return ActionResult.SUCCESS;
            } else if (!state.get(FINISHED)) {
                world.setBlockState(pos, state.with(FINISHED, true));
                world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.8F, 0.9F);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return (floor.isOf(ModObjects.MORNING_GLORY) && BlockMorningGloryPlant.isMature(floor)) || floor.isOf(this);
    }

    @Override
    public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return super.canPlaceAt(state, world, pos) && isAttachedToSolidBlock(updateAttachedSides(state, world, pos));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FINISHED);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        VoxelShape voxelShape = VoxelShapes.empty();
        if (state.get(NORTH)) {
            voxelShape = VoxelShapes.union(voxelShape, NORTH_SHAPE);
        }

        if (state.get(EAST)) {
            voxelShape = VoxelShapes.union(voxelShape, EAST_SHAPE);
        }

        if (state.get(SOUTH)) {
            voxelShape = VoxelShapes.union(voxelShape, SOUTH_SHAPE);
        }

        if (state.get(WEST)) {
            voxelShape = VoxelShapes.union(voxelShape, WEST_SHAPE);
        }

        return voxelShape;
    }

    @Override
    protected boolean canGrowVine(BlockState state, BlockView world, BlockPos pos) {
        return super.canGrowVine(state, world, pos) && !state.get(FINISHED);
    }
}
