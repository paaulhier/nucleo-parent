package de.keeeks.modules.npc.api;

public record NPCSkinModel(
        String value,
        String signature
) {

    public static NPCSkinModel of(String value, String signature) {
        return new NPCSkinModel(value, signature);
    }

    public static NPCSkinModel of(String texture) {
        return new NPCSkinModel(texture, null);
    }
}