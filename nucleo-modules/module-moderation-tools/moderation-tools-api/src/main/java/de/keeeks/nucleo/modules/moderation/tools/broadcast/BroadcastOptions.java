package de.keeeks.nucleo.modules.moderation.tools.broadcast;

public interface BroadcastOptions {
    BroadcastOptions permission(String permission);

    BroadcastOptions server(String server);

    String permission();

    String server();
}