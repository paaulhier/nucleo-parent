package de.keeeks.nucleo.core.spigot.json;

import com.google.gson.*;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import org.apache.commons.lang3.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.lang.reflect.Type;

public class LocationSerializer extends JsonSerializer<Location> {

    public LocationSerializer() {
        super(Location.class);
    }

    @Override
    public Location deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        float yaw = jsonObject.has("yaw")
                ? jsonObject.get("yaw").getAsFloat()
                : 0.0F;
        float pitch = jsonObject.has("pitch")
                ? jsonObject.get("pitch").getAsFloat()
                : 90.0F;

        World world = Bukkit.getWorld(jsonObject.get("world").getAsString());
        Validate.notNull(world, "World can not be null!");

        return new Location(
                world,
                jsonObject.get("x").getAsDouble(),
                jsonObject.get("y").getAsDouble(),
                jsonObject.get("z").getAsDouble(),
                yaw,
                pitch
        );
    }

    @Override
    public JsonElement serialize(Location location, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.add("world", jsonSerializationContext.serialize(location.getWorld(), World.class));
        jsonObject.addProperty("x", location.getX());
        jsonObject.addProperty("y", location.getY());
        jsonObject.addProperty("z", location.getZ());
        jsonObject.addProperty("yaw", location.getYaw());
        jsonObject.addProperty("pitch", location.getPitch());

        return jsonObject;
    }
}