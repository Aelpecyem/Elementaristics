package de.aelpecyem.elementaristics.common.mixin;

import de.aelpecyem.elementaristics.client.ClientProxy;
import de.aelpecyem.elementaristics.common.feature.stats.IElemStats;
import de.aelpecyem.elementaristics.common.handler.MeditationHandler;
import de.aelpecyem.elementaristics.common.handler.networking.PacketHandler;
import de.aelpecyem.elementaristics.lib.Constants;
import de.aelpecyem.elementaristics.lib.StatHelper;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static de.aelpecyem.elementaristics.lib.Constants.DataTrackers;
import static de.aelpecyem.elementaristics.lib.Constants.NBTTags.*;

@Mixin(PlayerEntity.class)
public abstract class PlayerMixin extends LivingEntity implements IElemStats {
    public int meditationTicks;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(at = @At("TAIL"), method = "tick()V")
    private void updateElemInfo(CallbackInfo info) {
        if (getSleepTimer() > 0 && getSleepTimer() % 10 == 0) {
            StatHelper.regenMagan((PlayerEntity) (Object) this, 10);
        }
        if (world.isClient && ClientProxy.meditate.wasPressed()) {
            if (canMeditate()) {
                PacketByteBuf data = new PacketByteBuf(Unpooled.buffer());
                boolean meditate = !isMeditating();
                data.writeBoolean(meditate);
                ClientSidePacketRegistry.INSTANCE.sendToServer(PacketHandler.MEDITATE_PACKET, data);
                updateNeeded = true;
            } else {
                ((PlayerEntity) (Object) this).sendMessage(new TranslatableText(Constants.MOD_ID + ".meditate.denied"), true);
            }
        }
        if (!canMeditate()) setMeditating(false);
        if (isMeditating()) {
            meditationTicks++;
            MeditationHandler.tick(meditationTicks, ((PlayerEntity) (Object) this));
            setHeadYaw(bodyYaw);
        }
    }

    @Inject(at = @At("HEAD"), method = "isImmobile()Z", cancellable = true)
    private void immobilize(CallbackInfoReturnable<Boolean> info) {
        if (isMeditating()) {
            info.setReturnValue(true);
            info.cancel();
        }
    }

    private boolean canMeditate() {
        return !this.isSleeping() && getPose() == EntityPose.STANDING;
    }

    @Shadow
    public abstract int getSleepTimer();

    @Override
    public int getMagan() {
        return dataTracker.get(DataTrackers.MAGAN);
    }

    @Override
    public void setMagan(int magan) {
        if (magan >= 0)
            dataTracker.set(DataTrackers.MAGAN, magan);
    }

    public void setMeditating(boolean value) {
        dataTracker.set(DataTrackers.MEDITATING, value);
    }

    public boolean isMeditating() {
        return dataTracker.get(DataTrackers.MEDITATING);
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

    @Inject(at = @At("RETURN"), method = "writeCustomDataToTag(Lnet/minecraft/nbt/CompoundTag;)V")
    private void writeNBT(CompoundTag dataTag, CallbackInfo info) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(MEDITATING, isMeditating());
        tag.putInt(MEDITATE_TICKS, meditationTicks);
        tag.putInt(MAGAN_TAG, getMagan());
        tag.putByte(ASCENSION_STAGE, getAscensionStage());
        if (getAscensionPath().isEmpty()) setAscensionPath("standard");
        tag.putString(ASCENSION_PATH, getAscensionPath());
        dataTag.put(ELEM_DATA, tag);
    }

    @Inject(at = @At("RETURN"), method = "readCustomDataFromTag(Lnet/minecraft/nbt/CompoundTag;)V")
    private void readNBT(CompoundTag dataTag, CallbackInfo info) {
        CompoundTag tag = (CompoundTag) dataTag.get(ELEM_DATA);
        setMeditating(tag.getBoolean(MEDITATING));
        meditationTicks = tag.getInt(MEDITATE_TICKS);
        setMagan(tag.getInt(MAGAN_TAG));
        setAscensionStage(tag.getByte(ASCENSION_STAGE));
        String s = tag.getString(ASCENSION_PATH);
        if (s.isEmpty()) s = "standard";
        setAscensionPath(s);
    }

    @Inject(at = @At("HEAD") , method = "initDataTracker()V")
    private void addTrackedData(CallbackInfo info){
        dataTracker.startTracking(DataTrackers.MAGAN, 0);
        dataTracker.startTracking(DataTrackers.ASCENSION_STAGE, (byte) 0);
        dataTracker.startTracking(DataTrackers.ASCENSION_PATH, "standard");
        dataTracker.startTracking(DataTrackers.MEDITATING, false);
    }
}
