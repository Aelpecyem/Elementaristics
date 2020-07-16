package de.aelpecyem.elementaristics.common.block.blockentity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Tickable;

import static de.aelpecyem.elementaristics.lib.Constants.NBTTags.TICK_COUNT;

public abstract class BlockEntityAlchemy extends BlockEntity implements Tickable, ImplementedInventory {
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

    /*
        Alchemy works like this:
        By default, all items tagged as alchemy item (no clue how to call that tag as of yet) can be decomposed or built (also no clue what tag that is)
        Alchemy Items will have a Predicate for their recipe:

        Predicate: Alchemy Conditions (contains temperature and other stuff, but also Attunement)
        By default, the Predicate will check if the Attunement matches the Attunement of the Item to be crafted;
        however, some (like Essence of Nothingness) have other conditions i.e. there being no aspects but Potency


        The Default Alchemy Procedure looks like this:
            -add parameters (items, usually raw Alchemical matter, and other things like adjusting temperature for some devices etc.)
            -activate (via redstone i guess)
            -results (this will go through all the recipes with the Alchemy Conditions, changing the Alchemical Matter to the Item in question)

        Some Items can be destabilized into Alchemical Matter with the according Attunement. Those items are tagged as such, and are IAspected
        (Side note: Ael might add ways to allow for compatibility via tags or the likes)
     */
}
