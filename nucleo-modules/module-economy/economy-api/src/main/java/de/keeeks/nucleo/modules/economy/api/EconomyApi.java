package de.keeeks.nucleo.modules.economy.api;

import java.util.List;
import java.util.Optional;

public interface EconomyApi {

    String CHANNEL = "nucleo:economies";

    /**
     * Refreshes the economy list.
     */
    void refresh();

    /**
     * Returns a list of all economies.
     * @return List of economies
     */
    List<Economy> economies();

    /**
     * Creates a new economy.
     * @param name Name of the economy
     * @return The created economy
     */
    Economy create(String name);

    /**
     * Returns an economy by its id.
     * @param id The id of the searched economy
     * @return The economy or an empty optional if not found
     */
    default Optional<Economy> economy(int id) {
        return economies().stream().filter(e -> e.id() == id).findFirst();
    }

    /**
     * Returns an economy by its name.
     * @param name The name of the searched economy
     * @return The economy or an empty optional if not found
     */
    default Optional<Economy> economy(String name) {
        return economies().stream().filter(e -> e.name().equals(name)).findFirst();
    }

    /**
     * Deletes an economy by its id.
     * @param id The id of the economy to delete
     */
    default void delete(int id) {
        economy(id).ifPresent(this::delete);
    }

    /**
     * Deletes an economy by its name.
     * @param name The name of the economy to delete
     */
    default void delete(String name) {
        economy(name).ifPresent(this::delete);
    }

    /**
     * Deletes an economy by its instance.
     * @param economy The economy to delete
     */
    default void delete(Economy economy) {
        economy.delete();
    }
}