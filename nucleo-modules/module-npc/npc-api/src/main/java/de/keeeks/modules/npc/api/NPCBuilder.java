package de.keeeks.modules.npc.api;

import lombok.Setter;

@Setter
public class NPCBuilder {

    private String name;
    private NPCSkinModel skinModel;

    public static NPCBuilder builder() {
        return new NPCBuilder();
    }
}