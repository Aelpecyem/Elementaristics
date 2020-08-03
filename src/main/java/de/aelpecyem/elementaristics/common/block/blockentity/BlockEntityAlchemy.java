package de.aelpecyem.elementaristics.common.block.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;

import javax.annotation.Nullable;

import static de.aelpecyem.elementaristics.lib.Constants.NBTTags.TICK_COUNT;

public abstract class BlockEntityAlchemy extends BlockEntityBase implements Tickable, ImplementedInventory, BlockEntityClientSerializable {
    public int tickCount;

    public BlockEntityAlchemy(BlockEntityType<?> type) {
        super(type);
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt(TICK_COUNT, tickCount);
        return super.toTag(tag);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        tickCount = tag.getInt(TICK_COUNT);
    }

    @Override
    public void tick() {
        if (canWork() && isActivated()) {
            work();
            if (tickCount >= getWorkTime()) {
                finish();
            }
        } else if (!canWork() && world.isClient) {
            spawnDistressSignal();
        }
    }

    private void finish() {

    }

    public void work() {
        tickCount++;
    }

    public int getWorkTime() {
        return 100;
    }


    @Environment(EnvType.CLIENT)
    public void spawnDistressSignal() {
        world.addParticle(ParticleTypes.SMOKE, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 0, 0, 0);
    }

    private boolean isActivated() {
        return world.isReceivingRedstonePower(pos);
    }

    public boolean canWork() {
        return true;
    }

    @Override
    public void fromClientTag(CompoundTag compoundTag) {
        fromTag(world.getBlockState(pos), compoundTag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag compoundTag) {
        toTag(compoundTag);
        return compoundTag;
    }


    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(pos, -1, toClientTag(new CompoundTag()));
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (!world.isClient) sync();
    }
}
