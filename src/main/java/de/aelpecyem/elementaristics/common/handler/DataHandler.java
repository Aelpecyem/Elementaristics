package de.aelpecyem.elementaristics.common.handler;

import com.google.gson.*;
import de.aelpecyem.elementaristics.common.feature.DataSerializable;
import de.aelpecyem.elementaristics.common.feature.alchemy.AlchemyItem;
import de.aelpecyem.elementaristics.lib.Constants;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class DataHandler extends JsonDataLoader {
    public static final Map<Identifier, DataSerializable.DataReader> DATA_READERS = new HashMap<>();
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static final Logger LOGGER = LogManager.getLogger();

    public DataHandler() {
        super(GSON, Constants.MOD_ID);
        DATA_READERS.put(new Identifier(Constants.MOD_ID, "alchemy_item"), new AlchemyItem.Serializer());
    }

    @Override
    protected void apply(Map<Identifier, JsonElement> loader, ResourceManager manager, Profiler profiler) {
        loader.forEach((id, element) -> {
            String type = JsonHelper.getString((JsonObject) element, "type");
            try {
                DataSerializable.DataReader reader = DATA_READERS.get(new Identifier(type));
                reader.getAccessMap().put(id, reader.readFromJson(id, (JsonObject) element));
            } catch (IllegalArgumentException | JsonParseException | NullPointerException exception) {
                LOGGER.error("Parsing error loading data module {} for Elementaristics", id, exception);
                exception.printStackTrace();
            }
        });
    }
}
