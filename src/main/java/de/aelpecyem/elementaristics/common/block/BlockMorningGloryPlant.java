package de.aelpecyem.elementaristics.common.block;

import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.registry.ModObjects;
import de.aelpecyem.elementaristics.registry.ModRegistry;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ShearsItem;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;

import java.util.Map;
import java.util.Random;

import static net.minecraft.block.ConnectingBlock.*;

public class BlockMorningGloryPlant extends FlowerBlock implements Fertilizable{
    public static final IntProperty AGE = IntProperty.of("age", 0, 4);
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES.entrySet().stream().filter((entry) -> entry.getKey() != Direction.DOWN && entry.getKey() != Direction.UP).collect(Util.toMap());
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(0, 0, 0, 16, 3, 16),
            Block.createCuboidShape(0, 0, 0, 16, 5, 16),
            Block.createCuboidShape(0, 0, 0, 16, 8, 16),
            Block.createCuboidShape(0, 0, 0, 16, 8, 16),
            Block.createCuboidShape(0, 0, 0, 16, 8, 16)
    };

    public BlockMorningGloryPlant() {
        super(ModRegistry.INTOXICATED, 20, AbstractBlock.Settings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
        this.setDefaultState(this.stateManager.getDefaultState().with(SOUTH, false).with(NORTH, false).with(EAST, false).with(WEST, false).with(AGE, 0));
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return AGE_TO_SHAPE[getAge(state)];
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.NONE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(AGE, EAST, SOUTH, WEST, NORTH);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (isMature(state) && player.getStackInHand(hand).getItem() instanceof ShearsItem && world instanceof ServerWorld) {
            world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.8F, 0.9F);
            LootTable table = world.getServer().getLootManager().getTable(new Identifier(Constants.MOD_ID, "blocks/morning_glory_sheared"));
            LootContext.Builder builder = (new LootContext.Builder((ServerWorld) world)).random(world.random).parameter(LootContextParameters.POSITION, pos).parameter(LootContextParameters.TOOL, player.getStackInHand(hand)).optionalParameter(LootContextParameters.THIS_ENTITY, player).parameter(LootContextParameters.BLOCK_STATE, state);
            table.generateLoot(builder.build(LootContextTypes.BLOCK)).forEach(stack -> dropStack(world, pos, stack));
            world.setBlockState(pos, state.with(AGE, 0));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateAttachedSides(getDefaultState().with(AGE, 0), ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return super.getStateForNeighborUpdate(updateAttachedSides(state, world, pos), direction, newState, world, pos, posFrom);
    }

    public BlockState updateAttachedSides(BlockState state, WorldView world, BlockPos pos){
        for (Direction direction : FACING_PROPERTIES.keySet()) state = state.with(FACING_PROPERTIES.get(direction), isBlockQualifiedForAttachment(world, world.getBlockState(pos.offset(direction)), pos, direction));
        return state;
    }

    public boolean isBlockQualifiedForAttachment(WorldView world, BlockState state, BlockPos pos, Direction direction){
        return state.isSideSolidFullSquare(world, pos, direction);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isLightRight(world, pos) && random.nextBoolean()) {
            if (!isMature(state)){
                world.setBlockState(pos, state.with(AGE, getAge(state) + 1));
            } else if (canGrowVine(state, world, pos)){
                growVine(state, world, pos, random);
            }
        }
    }

    protected boolean canGrowVine(BlockState state, BlockView world, BlockPos pos){
        return world.getBlockState(pos.up()).isAir() && isAttachedToSolidBlock(state);
    }
    protected boolean isAttachedToSolidBlock(BlockState state) {
        return this.getAdjacentBlockCount(state) > 0;
    }

    protected int getAdjacentBlockCount(BlockState state) {
        int i = 0;
        for (BooleanProperty booleanProperty : FACING_PROPERTIES.values()) {
            if (state.get(booleanProperty)) {
                ++i;
            }
        }
        return i;
    }

    public static int getAge(BlockState state){
        return state.get(AGE);
    }

    public static boolean isMature(BlockState state){
        return state.get(AGE) >= 4;
    }

    public static boolean isLightRight(World world, BlockPos pos){
        return world.getBaseLightLevel(pos, 0) >= 8;
    }

    @Override
    public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
        return (!isMature(state) || canGrowVine(state, world, pos));
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return (!isMature(state) || canGrowVine(state, world, pos)) && random.nextBoolean();
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (!isMature(state))
        world.setBlockState(pos, state.with(AGE, getAge(state) + 1));
        else if (random.nextDouble() < 0.25 && canGrowVine(state, world, pos)){
            growVine(state, world, pos, random);
        }
    }

    protected void growVine(BlockState originState, ServerWorld world, BlockPos pos, Random random){
        BlockPos targetPos = pos.up();
        BlockState placedState = updateAttachedSides(ModObjects.MORNING_GLORY_VINES.getDefaultState().with(AGE, 0).with(BlockMorningGloryVine.FINISHED, world.random.nextFloat() < 0.3F || !world.getBlockState(targetPos.up()).isAir()), world, targetPos);
        if (isAttachedToSolidBlock(placedState)) world.setBlockState(targetPos, placedState);
    }
}
