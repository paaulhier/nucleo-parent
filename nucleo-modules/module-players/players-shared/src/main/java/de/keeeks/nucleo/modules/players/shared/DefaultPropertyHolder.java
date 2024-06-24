package de.keeeks.nucleo.modules.players.shared;

import de.keeeks.nucleo.modules.players.api.PropertyHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DefaultPropertyHolder implements PropertyHolder {
    private final Map<String, Object> properties = new HashMap<>();

    public DefaultPropertyHolder(PropertyHolder propertyHolder) {
        setProperties(propertyHolder);
    }

    public DefaultPropertyHolder() {
    }

    @Override
    public <T> T property(String key, T defaultValue) {
        return (T) properties.getOrDefault(
                key,
                defaultValue
        );
    }

    @Override
    public <T> T property(String key) {
        return properties.containsKey(key) ? (T) properties.get(key) : null;
    }

    @Override
    public <T> Optional<T> optionalProperty(String key) {
        return Optional.ofNullable(property(key));
    }

    @Override
    public <T> PropertyHolder setProperty(String key, T value) {
        properties.put(
                key,
                value
        );
        return this;
    }

    @Override
    public PropertyHolder removeProperty(String key) {
        properties.remove(key);
        return this;
    }

    @Override
    public PropertyHolder clearProperties() {
        properties.clear();
        return this;
    }

    @Override
    public PropertyHolder setProperties(PropertyHolder properties) {
        if (properties == null) return this;
        for (String key : properties.keys()) {
            this.properties.put(key, properties.property(key));
        }
        return this;
    }

    @Override
    public Collection<String> keys() {
        return properties.keySet();
    }

    @Override
    public Collection<Object> values() {
        return properties.values();
    }
}