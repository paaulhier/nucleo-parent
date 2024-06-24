package de.keeeks.nucleo.modules.moderation.tools.broadcast;

public interface BroadcastOptions {

    /**
     * Sets the permission for the broadcast.
     *
     * @param permission the permission to set
     * @return the options
     */
    BroadcastOptions permission(String permission);

    /**
     * Sets the server for the broadcast.
     *
     * @param server the server to set
     * @return the options
     */
    BroadcastOptions server(String server);

    /**
     * Gets the permission for the broadcast.
     *
     * @return the permission
     */
    String permission();

    /**
     * Gets the server for the broadcast.
     *
     * @return the server
     */
    String server();
}