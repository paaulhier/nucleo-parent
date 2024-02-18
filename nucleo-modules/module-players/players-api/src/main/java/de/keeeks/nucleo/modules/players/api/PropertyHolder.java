package de.keeeks.nucleo.modules.players.api;

import java.util.Collection;
import java.util.Optional;

public interface PropertyHolder {

    <T> T property(String key, T defaultValue);

    <T> T property(String key);

    <T> Optional<T> optionalProperty(String key);

    <T> PropertyHolder setProperty(String key, T value);

    PropertyHolder removeProperty(String key);

    PropertyHolder clearProperties();

    PropertyHolder setProperties(PropertyHolder properties);

    Collection<String> keys();

    Collection<Object> values();

}