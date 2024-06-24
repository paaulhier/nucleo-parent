package de.keeeks.nucleo.modules.moderation.tools.cps;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public interface ClickCheckApi {

    String CHANNEL = "nucleo:clickcheck";

    /**
     * Returns a list of all click check informations.
     *
     * @return The list of click check informations
     */
    List<ClickCheckInformation> clickChecks();

    /**
     * Creates a new click check listener.
     *
     * @param consumer The consumer that will be called when a click check is created
     * @return The current instance of the click check API
     */
    ClickCheckApi createListener(Consumer<ClickCheckInformation> consumer);

    /**
     * Deletes a click check listener.
     *
     * @param consumer The consumer that will be deleted
     * @return The current instance of the click check API
     */
    ClickCheckApi deleteListener(Consumer<ClickCheckInformation> consumer);

    /**
     * Returns the number of click checks.
     *
     * @return The number of click checks
     */
    default int clickChecksCount() {
        return clickChecks().size();
    }

    /**
     * Creates a new click check. If a click check already exists for the viewer, it will be replaced.
     *
     * @param viewer The viewer's UUID
     * @param target The target's UUID
     * @return The click check information
     */
    Optional<ClickCheckInformation> createClickCheck(UUID viewer, UUID target);

    /**
     * Returns the click check information for the specified viewer.
     *
     * @param viewer The viewer's UUID
     * @return The click check information
     */
    Optional<ClickCheckInformation> clickCheck(UUID viewer);

    /**
     * Returns the click check information for the specified target.
     *
     * @param target The target's UUID
     * @return The click check information
     */
    List<ClickCheckInformation> clickCheckByTarget(UUID target);

    /**
     * Removes the click check for the specified viewer.
     *
     * @param viewer The viewer's UUID
     */
    void removeClickCheck(UUID viewer);

    /**
     * Removes the click check for the specified target.
     *
     * @param target The target's UUID
     */
    void removeClickCheckByTarget(UUID target);

    /**
     * Removes the click check for the specified click check information.
     *
     * @param clickCheckInformation The click check information
     */
    void removeClickCheck(ClickCheckInformation clickCheckInformation);

}