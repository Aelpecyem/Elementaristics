package de.aelpecyem.elementaristics.registry;

import de.aelpecyem.elementaristics.common.item.ItemLiberElementium;
import de.aelpecyem.elementaristics.lib.Util;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public class ModObjects {
    public static final Item LIBER_ELEMENTIUM = new ItemLiberElementium();
    public static void init(){
        Util.register(Registry.ITEM, "liber_elementium", LIBER_ELEMENTIUM);
    }
}
