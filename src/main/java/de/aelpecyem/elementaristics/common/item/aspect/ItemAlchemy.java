package de.aelpecyem.elementaristics.common.item.aspect;

import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAlchemy extends Item {
    public ItemAlchemy() {
        super(new Settings().group(Constants.ELEMENTARISTICS_GROUP));
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasTag())
            //todo write aspects in tooltip lul
            super.appendTooltip(stack, world, tooltip, context);
    }
}
