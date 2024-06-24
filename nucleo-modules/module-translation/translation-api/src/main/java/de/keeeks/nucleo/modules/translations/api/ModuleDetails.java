package de.keeeks.nucleo.modules.translations.api;

import java.time.Instant;

public interface ModuleDetails {

    /**
     * Returns the id of the module.
     * @return the id of the module
     */
    int id();

    /**
     * Returns the name of the module.
     * @return the name of the module
     */
    String name();

    /**
     * Returns the date when the module was created.
     * @return the date when the module was created
     */
    Instant createdAt();

    /**
     * Returns the date when the module was last updated.
     * @return the date when the module was last updated
     */
    Instant updatedAt();
}