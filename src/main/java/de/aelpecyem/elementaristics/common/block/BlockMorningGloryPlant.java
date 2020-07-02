package de.aelpecyem.elementaristics.common.block;

import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.block.*;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.ZombieEntity;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Map;
import java.util.Random;

import static net.minecraft.block.ConnectingBlock.*;

public class BlockMorningGloryPlant extends PlantBlock {
    public static final BooleanProperty MATURE = BooleanProperty.of("mature");
    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = ConnectingBlock.FACING_PROPERTIES.entrySet().stream().filter((entry) -> entry.getKey() != Direction.DOWN && entry.getKey() != Direction.UP).collect(Util.toMap());

    public BlockMorningGloryPlant() {
        super(AbstractBlock.Settings.of(Material.PLANT).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
        this.setDefaultState(this.stateManager.getDefaultState().with(SOUTH, false).with(NORTH, false).with(EAST, false).with(WEST, false).with(MATURE, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(MATURE, EAST, SOUTH, WEST, NORTH);
    }


    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (isMature(state) && player.getStackInHand(hand).getItem() instanceof ShearsItem && world instanceof ServerWorld) {
            world.playSound(null, pos, SoundEvents.ENTITY_SHEEP_SHEAR, SoundCategory.BLOCKS, 0.8F, 0.9F);
            LootTable table = world.getServer().getLootManager().getTable(new Identifier(Constants.MODID, "blocks/morning_glory_sheared"));
            LootContext.Builder builder = (new LootContext.Builder((ServerWorld) world)).random(world.random).parameter(LootContextParameters.POSITION, pos).parameter(LootContextParameters.TOOL, player.getStackInHand(hand)).optionalParameter(LootContextParameters.THIS_ENTITY, player).parameter(LootContextParameters.BLOCK_STATE, state);
            table.generateLoot(builder.build(LootContextTypes.BLOCK)).forEach(stack -> dropStack(world, pos, stack));
            world.setBlockState(pos, state.with(MATURE, false));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return updateAttachedSides(getDefaultState().with(MATURE, false), ctx.getWorld(), ctx.getBlockPos());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
        return super.getStateForNeighborUpdate(updateAttachedSides(state, world, pos), direction, newState, world, pos, posFrom);
    }

    public BlockState updateAttachedSides(BlockState state, WorldAccess world, BlockPos pos){
        for (Direction direction : FACING_PROPERTIES.keySet()) state = state.with(FACING_PROPERTIES.get(direction), isBlockQualifiedForAttachment(world, world.getBlockState(pos.offset(direction)), pos, direction));
        return state;
    }

    public boolean isBlockQualifiedForAttachment(WorldAccess world, BlockState state, BlockPos pos, Direction direction){
        return state.isSolidBlock(world, pos) && state.isSideSolidFullSquare(world, pos, direction);
    }

    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (isLightRight(world, pos)) {
            if (!isMature(state) && isAttachedToSolidBlock(state)){
                world.setBlockState(pos, state.with(MATURE, true));
            }
        }
    }

    public boolean isAttachedToSolidBlock(BlockState state) {
        return this.getAdjacentBlockCount(state) > 0;
    }

    private int getAdjacentBlockCount(BlockState state) {
        int i = 0;
        for (BooleanProperty booleanProperty : FACING_PROPERTIES.values()) {
            if (state.get(booleanProperty)) {
                ++i;
            }
        }
        return i;
    }

    public boolean isMature(BlockState state){
        return state.get(MATURE);
    }

    public boolean isLightRight(World world, BlockPos pos){
        return world.getBaseLightLevel(pos, 0) >= 5 && world.getBaseLightLevel(pos, 0) <= 12;
    }
}
