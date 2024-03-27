package de.keeeks.nucleo.modules.economy.api;

import java.util.List;
import java.util.Optional;

public interface EconomyApi {

    String CHANNEL = "nucleo:economies";

    void refresh();

    List<Economy> economies();

    default Optional<Economy> economy(int id) {
        return economies().stream().filter(e -> e.id() == id).findFirst();
    }

    default Optional<Economy> economy(String name) {
        return economies().stream().filter(e -> e.name().equals(name)).findFirst();
    }

    Economy create(String name);

    default void delete(int id) {
        economy(id).ifPresent(this::delete);
    }

    default void delete(String name) {
        economy(name).ifPresent(this::delete);
    }

    default void delete(Economy economy) {
        economy.delete();
    }
}