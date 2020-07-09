package de.aelpecyem.elementaristics.common.mixin;

import de.aelpecyem.elementaristics.common.feature.stats.IElemStats;
import de.aelpecyem.elementaristics.lib.StatHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static de.aelpecyem.elementaristics.lib.Constants.DataTrackers;
import static de.aelpecyem.elementaristics.lib.Constants.NBTTags;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements IElemStats {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("HEAD") , method = "tick()V")
    private void updateElemInfo(CallbackInfo info){
        if (getSleepTimer() > 0 && getSleepTimer() % 10 == 0){
            StatHelper.regenMagan((PlayerEntity) (Object) this, 10);
        }
    }
    @Shadow
    public abstract int getSleepTimer();

    @Override
    public int getMagan(){
        return dataTracker.get(DataTrackers.MAGAN);
    }

    @Override
    public void setMagan(int magan){
        if (magan >= 0)
            dataTracker.set(DataTrackers.MAGAN, magan);
    }

    @Override
    public byte getAscensionStage() {
        return dataTracker.get(DataTrackers.ASCENSION_STAGE);
    }

    @Override
    public void setAscensionStage(byte stage) {
        dataTracker.set(DataTrackers.ASCENSION_STAGE, stage);
    }

    @Override
    public void setAscensionPath(String path) {
        dataTracker.set(DataTrackers.ASCENSION_PATH, path);
    }

    @Override
    public String getAscensionPath() {
        return dataTracker.get(DataTrackers.ASCENSION_PATH);
    }

    @Inject(at = @At("RETURN") , method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
    private void writeNBT(CompoundTag tag, CallbackInfo info){
        tag.putInt(NBTTags.MAGAN_TAG, getMagan());
        tag.putByte(NBTTags.ASCENSION_STAGE, getAscensionStage());
        if (getAscensionPath().isEmpty()) setAscensionPath("standard");
        tag.putString(NBTTags.ASCENSION_PATH, getAscensionPath());
    }

    @Inject(at = @At("RETURN") , method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
    private void readNBT(CompoundTag tag, CallbackInfo info){
        setMagan(tag.getInt(NBTTags.MAGAN_TAG));
        setAscensionStage(tag.getByte(NBTTags.ASCENSION_STAGE));
        String s = tag.getString(NBTTags.ASCENSION_PATH);
        if (s.isEmpty()) s = "standard";
        setAscensionPath(s);
    }

    @Inject(at = @At("HEAD") , method = "initDataTracker()V")
    private void addTrackedData(CallbackInfo info){
        dataTracker.startTracking(DataTrackers.MAGAN, 0);
        dataTracker.startTracking(DataTrackers.ASCENSION_STAGE, (byte) 0);
        dataTracker.startTracking(DataTrackers.ASCENSION_PATH, "standard");
    }
}
