package de.aelpecyem.elementaristics.common.item.aspect;

import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import de.aelpecyem.elementaristics.common.handler.AlchemyHandler;
import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemAlchemy extends Item {
    public ItemAlchemy(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (stack.hasTag() && stack.getTag().contains(Constants.NBTTags.ELEM_DATA))
            tooltip.add(AlchemyHandler.Helper.getAttunement(stack).toText());
        else {
            AspectAttunement attunement = AlchemyHandler.Helper.getStandardAttunement(stack);
            if (attunement != null) tooltip.add(attunement.toText());
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
