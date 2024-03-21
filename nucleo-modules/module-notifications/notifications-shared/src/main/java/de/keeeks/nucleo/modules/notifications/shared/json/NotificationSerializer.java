package de.keeeks.nucleo.modules.notifications.shared.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import de.keeeks.nucleo.core.api.json.serializer.JsonSerializer;
import de.keeeks.nucleo.modules.notifications.api.Notification;
import de.keeeks.nucleo.modules.notifications.shared.NucleoNotification;

import java.lang.reflect.Type;

public class NotificationSerializer extends JsonSerializer<Notification> {

    public NotificationSerializer() {
        super(Notification.class);
    }

    @Override
    public Notification deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext
    ) throws JsonParseException {
        return jsonDeserializationContext.deserialize(jsonElement, NucleoNotification.class);
    }

    @Override
    public JsonElement serialize(
            Notification notification,
            Type type,
            JsonSerializationContext jsonSerializationContext
    ) {
        return jsonSerializationContext.serialize(notification, NucleoNotification.class);
    }
}