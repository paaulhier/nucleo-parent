package de.keeeks.nucleo.modules.economy.api;

import java.util.List;
import java.util.UUID;

public interface Economy {

    /**
     * Returns the id of this economy.
     *
     * @return The id
     */
    int id();

    /**
     * Returns the name of this economy.
     *
     * @return The name
     */
    String name();

    /**
     * Returns the display name of this economy.
     *
     * @param uuid     The uuid of the player
     * @param modifier The modifier see {@link EconomyBalanceModifier}
     */
    void modify(UUID uuid, EconomyBalanceModifier modifier);

    /**
     * Returns the top 10 players of this economy.
     *
     * @return The top 10 players
     */
    List<UUID> top();

    /**
     * Returns the balance of a player.
     *
     * @param uuid The uuid of the player
     * @return The balance of the player
     */
    double balance(UUID uuid);

    /**
     * Deposits money to a player.
     *
     * @param uuid   The uuid of the player
     * @param amount The amount to deposit
     */
    void deposit(UUID uuid, double amount);

    /**
     * Withdraws money from a player.
     *
     * @param uuid   The uuid of the player
     * @param amount The amount to withdraw
     */
    void withdraw(UUID uuid, double amount);

    /**
     * Transfers money from one player to another.
     *
     * @param from   The uuid of the player to transfer from
     * @param to     The uuid of the player to transfer to
     * @param amount The amount to transfer
     */
    void transfer(UUID from, UUID to, double amount);

    /**
     * Sets the balance of a player.
     *
     * @param uuid   The uuid of the player
     * @param amount The amount to set the balance to
     */
    void setBalance(UUID uuid, double amount);

    /**
     * Deletes this economy.
     */
    void delete();

}