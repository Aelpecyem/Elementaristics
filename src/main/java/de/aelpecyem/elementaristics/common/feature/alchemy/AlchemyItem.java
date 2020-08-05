package de.aelpecyem.elementaristics.common.feature.alchemy;

import com.google.gson.JsonObject;
import de.aelpecyem.elementaristics.common.feature.DataSerializable;
import de.aelpecyem.elementaristics.common.handler.AlchemyHandler;
import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.util.Map;

public class AlchemyItem implements DataSerializable<AlchemyItem> {
    private final Identifier id;
    private final AspectAttunement aspects;
    private final boolean stabilizable, destabilizable;
    private final Item stabilizeItem;
    private final Ingredient validItems;
    private final int color;

    public AlchemyItem(Identifier id, AspectAttunement aspects, boolean stabilizable, boolean destabilizable, int color, Item stabilizeItem, Ingredient validItems) {
        this.id = id;
        this.aspects = aspects;
        this.stabilizable = stabilizable;
        this.destabilizable = destabilizable;
        this.color = color;
        this.stabilizeItem = stabilizeItem;
        this.validItems = validItems;
    }

    public int getColor() {
        return color;
    }

    public AspectAttunement getAspects() {
        return aspects;
    }

    public Item getStabilizeItem() {
        return stabilizeItem;
    }

    public Ingredient getValidItems() {
        return validItems;
    }

    public boolean isStabilizable() {
        return stabilizable;
    }

    public boolean isDestabilizable() {
        return destabilizable;
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public static class Serializer extends DataReader<AlchemyItem> {

        @Override
        public AlchemyItem readFromJson(Identifier id, JsonObject json) {
            Item item = JsonHelper.getItem(json, "item");
            return new AlchemyItem(id, AspectAttunement.read(JsonHelper.getObject(json, "aspects")), JsonHelper.getBoolean(json, "stabilize", false), JsonHelper.getBoolean(json, "destabilize", true), JsonHelper.getInt(json, "color", Constants.Colors.MAGAN_COLOR), item, json.has("ingredient") ? Ingredient.fromJson(json.get("ingredient")) : Ingredient.ofItems(item));
        }

        @Override
        public Map<Identifier, AlchemyItem> getAccessMap() {
            return AlchemyHandler.ALCHEMY_ITEMS;
        }
    }
}
