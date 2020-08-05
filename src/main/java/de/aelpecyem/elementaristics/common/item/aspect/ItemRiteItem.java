package de.aelpecyem.elementaristics.common.item.aspect;

import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.common.feature.alchemy.IAspectedItem;
import de.aelpecyem.elementaristics.lib.RenderHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class ItemRiteItem extends Item implements IAspectedItem {
    private final AspectAttunement attunement;
    private final int color;

    public ItemRiteItem(Settings settings, AspectAttunement attunement, int color) {
        super(settings);
        this.attunement = attunement;
        this.color = color;
    }


    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        List<EntityNexus> nexusList = world.getEntities(EntityNexus.class, user.getBoundingBox().expand(10, 10, 10), nexus -> nexus.distanceTo(user) < 20);
        if (!nexusList.isEmpty()) {
            nexusList.sort(Comparator.comparingDouble(nexus -> nexus.distanceTo(user)));
            EntityNexus nexus = nexusList.stream().findFirst().get();
            if (addAspects(stack, world, user, nexus)) {
                sendChangeSignal(stack, world, user, nexus);
                if (doesConsume(stack)) stack.decrement(1);
                return stack;
            }
        }
        return super.finishUsing(stack, world, user);
    }

    public void sendChangeSignal(ItemStack stack, World world, LivingEntity user, EntityNexus nexus) {
        //also particles
        nexus.targetrgb = RenderHelper.toRGB(color);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    //might do this on the server only, then spawn particles with a packet (?)
    public boolean addAspects(ItemStack stack, World world, LivingEntity user, EntityNexus nexus) {
        nexus.addAttunement(getAttunement(stack));
        return true;
    }

    @Override
    public AspectAttunement getAttunement(ItemStack stack) {
        return attunement;
    }
}
