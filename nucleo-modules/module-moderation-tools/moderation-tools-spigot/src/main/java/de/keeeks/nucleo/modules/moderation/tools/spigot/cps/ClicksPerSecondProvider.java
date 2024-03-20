package de.keeeks.nucleo.modules.moderation.tools.spigot.cps;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public final class ClicksPerSecondProvider {
    private final List<ClicksPerSecondInformation> clicksPerSecondInformation = new LinkedList<>();

    public void handleClick(Player player, ClicksPerSecondInformation.ClickType clickType) {
        ClicksPerSecondInformation information = clicksPerSecondInformation.stream()
                .filter(info -> info.uuid().equals(player.getUniqueId()))
                .findFirst()
                .orElseGet(() -> createClicksPerSecondInformation(player));

        information.clicks().add(new ClicksPerSecondInformation.ClickDetails(
                clickType,
                Instant.now()
        ));
    }

    public Optional<ClicksPerSecondInformation> clicksPerSecondInformation(Player player) {
        return clicksPerSecondInformation.stream()
                .filter(info -> info.uuid().equals(player.getUniqueId()))
                .findFirst();
    }

    @NotNull
    private ClicksPerSecondInformation createClicksPerSecondInformation(Player player) {
        ClicksPerSecondInformation newInfo = new ClicksPerSecondInformation(
                player.getUniqueId(),
                new LinkedList<>()
        );
        clicksPerSecondInformation.add(newInfo);
        return newInfo;
    }
}