package de.aelpecyem.elementaristics.common.item.aspect;

import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.common.feature.alchemy.IAspectedItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.Comparator;
import java.util.List;

public class ItemRiteItem extends Item implements IAspectedItem {
    private AspectAttunement attunement;

    public ItemRiteItem(Settings settings, AspectAttunement attunement) {
        super(settings);
        this.attunement = attunement;
    }


    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        List<EntityNexus> nexusList = world.getEntities(EntityNexus.class, user.getBoundingBox().expand(10, 10, 10), nexus -> nexus.distanceTo(user) < 20);
        if (!nexusList.isEmpty()) {
            nexusList.sort(Comparator.comparingDouble(nexus -> nexus.distanceTo(user)));
            EntityNexus nexus = nexusList.stream().findFirst().get();
            ItemStack stack = user.getStackInHand(hand);
            if (addAspects(stack, world, user, nexus)) {
                return TypedActionResult.consume(stack);
            }
        }
        return super.use(world, user, hand);
    }

    //might do this on the server only, then spawn particles with a packet (?)
    public boolean addAspects(ItemStack stack, World world, PlayerEntity user, EntityNexus nexus) {
        nexus.addAttunement(getAttunement(stack));
        return true;
    }

    @Override
    public AspectAttunement getAttunement(ItemStack stack) {
        return attunement;
    }
}
