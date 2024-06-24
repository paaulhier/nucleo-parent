package de.keeeks.nucleo.modules.hologram.api;

public interface HologramLine<C> {

    /**
     * Returns the hologram of this hologram line.
     * @return the hologram of this hologram line
     */
    Hologram hologram();

    /**
     * Returns the content of this hologram line.
     * @return the content of this hologram line
     */
    C content();

    /**
     * Sets the content of this hologram line.
     * @param newContent the new content of this hologram line
     */
    void content(C newContent);

    /**
     * Updates the hologram line.
     */
    void update();

    /**
     * Removes the hologram line.
     */
    void remove();

}