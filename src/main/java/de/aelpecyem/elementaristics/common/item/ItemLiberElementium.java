package de.aelpecyem.elementaristics.common.item;

import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ItemLiberElementium extends Item {
    public ItemLiberElementium() {
        super(new Settings().group(Constants.ELEMENTARISTICS_GROUP).maxCount(1)); //some way of displaying the current strength (via HUD?)
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return super.use(world, user, hand);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.NONE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 140;
    }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (user instanceof PlayerEntity) ((PlayerEntity) user).getItemCooldownManager().set(this, 20);

        return super.finishUsing(stack, world, user);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (user instanceof PlayerEntity) ((PlayerEntity) user).getItemCooldownManager().set(this, 20);
//spawn stuff
        super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }
}
