package de.keeeks.nucleo.modules.hologram.api;

public interface HologramLine<C> {

    Hologram hologram();

    C content();

    void content(C newContent);

    void update();

    void remove();

}