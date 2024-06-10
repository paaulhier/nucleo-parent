package de.keeeks.nucleo.modules.moderation.tools.shared.broadcast;

import de.keeeks.nucleo.modules.moderation.tools.broadcast.BroadcastOptions;
import lombok.Getter;

@Getter
public class NucleoBroadcastOptions implements BroadcastOptions {
    public static BroadcastOptions EMPTY = new NucleoBroadcastOptions();

    private String permission;
    private String server;

    @Override
    public BroadcastOptions permission(String permission) {
        this.permission = permission;
        return this;
    }

    @Override
    public BroadcastOptions server(String server) {
        this.server = server;
        return this;
    }
}