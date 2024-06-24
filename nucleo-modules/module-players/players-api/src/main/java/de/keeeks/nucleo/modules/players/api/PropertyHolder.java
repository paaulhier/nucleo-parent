package de.keeeks.nucleo.modules.players.api;

import java.util.Collection;
import java.util.Optional;

public interface PropertyHolder {

    /**
     * Returns the property with the given key or the default value if the property does not exist.
     *
     * @param key          the key of the property
     * @param defaultValue the default value
     * @param <T>          the type of the property
     * @return the property with the given key or the default value if the property does not exist
     */
    <T> T property(String key, T defaultValue);

    /**
     * Returns the property with the given key.
     *
     * @param key the key of the property
     * @param <T> the type of the property
     * @return the property with the given key
     */
    <T> T property(String key);

    /**
     * Returns the property with the given key as an optional.
     *
     * @param key the key of the property
     * @param <T> the type of the property
     * @return the property with the given key as an optional
     */
    <T> Optional<T> optionalProperty(String key);

    /**
     * Sets the property with the given key to the given value.
     *
     * @param key   the key of the property
     * @param value the value of the property
     * @param <T>   the type of the property
     * @return this property holder
     */
    <T> PropertyHolder setProperty(String key, T value);

    /**
     * Removes the property with the given key.
     *
     * @param key the key of the property
     * @return this property holder
     */
    PropertyHolder removeProperty(String key);

    /**
     * Clears all properties.
     *
     * @return this property holder
     */
    PropertyHolder clearProperties();

    /**
     * Sets all properties to the given properties.
     *
     * @param properties the properties to set
     * @return this property holder
     */
    PropertyHolder setProperties(PropertyHolder properties);

    /**
     * Returns all keys of the properties.
     *
     * @return all keys of the properties
     */
    Collection<String> keys();

    /**
     * Returns all values of the properties.
     *
     * @return all values of the properties
     */
    Collection<Object> values();

}