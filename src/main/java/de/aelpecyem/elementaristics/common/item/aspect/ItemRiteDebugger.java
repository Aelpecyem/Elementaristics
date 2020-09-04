package de.aelpecyem.elementaristics.common.item.aspect;

import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.common.handler.AlchemyHandler;
import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Arrays;

public class ItemRiteDebugger extends ItemRiteItem {
    public ItemRiteDebugger() {
        super(new Settings().group(Constants.ELEMENTARISTICS_GROUP), new AspectAttunement(0, 0, 0, 0, 0, 0), 0);
    }

    @Override
    public boolean addAspects(ItemStack stack, World world, LivingEntity user, EntityNexus nexus) {
        int type = user.getHorizontalFacing().getId();
        if (Math.abs(user.pitch) > 50) type = user.pitch < 0 ? 0 : 1;
        int[] values = new int[6];
        Arrays.fill(values, 0);
        values[type] = type == 5 ? nexus.getAttunement().getPotential() + (user.isSneaking() ? -1 : 1) : (user.isSneaking() ? -1 : 1);
        int[] origValues = nexus.getAttunement().getAspects();
        nexus.setAttunement(new AspectAttunement(origValues[0] + values[0], origValues[1] + values[1], origValues[2] + values[2], origValues[3] + values[3], origValues[4] + values[4], origValues[5] + values[5]));
        return true;
    }

    @Override
    public boolean doesConsume(ItemStack stack) {
        return false;
    }

    @Override
    public void sendChangeSignal(ItemStack stack, World world, LivingEntity user, EntityNexus nexus) {
        int type = user.getHorizontalFacing().getId();
        if (Math.abs(user.pitch) > 50) type = user.pitch < 0 ? 0 : 1;
        if (type < 5)
            nexus.targetrgb = AlchemyHandler.ASPECT_LIST.get(type).getColor();
    }
}
