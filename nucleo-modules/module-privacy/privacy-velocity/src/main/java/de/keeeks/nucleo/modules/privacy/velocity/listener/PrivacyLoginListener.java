package de.keeeks.nucleo.modules.privacy.velocity.listener;

import com.velocitypowered.api.event.EventTask;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.modules.privacy.api.PrivacyApi;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static net.kyori.adventure.text.Component.translatable;

@RequiredArgsConstructor
public class PrivacyLoginListener {

    private final PrivacyApi privacyApi;

    @Subscribe
    public EventTask handleLogin(LoginEvent event) {
        Player player = event.getPlayer();

        return EventTask.async(() -> privacyApi.privacyInformation(player.getUniqueId()).or(
                () -> Optional.of(privacyApi.createPrivacyInformation(player.getUniqueId()))
        ).ifPresent(privacyInformation -> {
            if (!privacyInformation.accepted()) {
                player.sendMessage(translatable("privacy.information"));
            }
        }));
    }
}