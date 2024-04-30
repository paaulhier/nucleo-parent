package de.keeeks.nucleo.modules.translation.velocity.commands;

import com.velocitypowered.api.proxy.Player;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.Style;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.velocity.annotation.CommandPermission;

import static net.kyori.adventure.text.Component.text;

@Command({"reloadtranslations"})
@CommandPermission("nucleo.commands.translation.reload")
public class ReloadTranslationsCommand {
    private final TranslationApi translationApi = ServiceRegistry.service(TranslationApi.class);

    @DefaultFor("reloadtranslations")
    public void reloadTranslations(Player player) {
        translationApi.reload();
        player.sendMessage(text("Die Übersetzungen werden netzwerkweit neu geladen!", Style.style(NamedTextColor.GREEN)));
    }
}