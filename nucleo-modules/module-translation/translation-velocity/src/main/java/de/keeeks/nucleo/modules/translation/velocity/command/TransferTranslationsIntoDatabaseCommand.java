package de.keeeks.nucleo.modules.translation.velocity.command;

import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.translation.global.TranslationEntry;
import de.keeeks.nucleo.modules.translation.global.TranslationRegistry;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.DefaultFor;
import revxrsal.commands.velocity.VelocityCommandActor;
import revxrsal.commands.velocity.annotation.CommandPermission;

import java.util.concurrent.atomic.AtomicInteger;

import static net.kyori.adventure.text.Component.text;

@Command({"transferTranslationsIntoDatabase"})
@CommandPermission("nucleo.translation.admin")
public class TransferTranslationsIntoDatabaseCommand {

    private final TranslationApi translationApi = ServiceRegistry.service(TranslationApi.class);

    @DefaultFor("transferTranslationsIntoDatabase")
    public void translateConfigurationsIntoDatabase(VelocityCommandActor actor) {
        actor.getSource().sendMessage(text("Transferring translations into database..."));

        Scheduler.runAsync(() -> {
            AtomicInteger count = new AtomicInteger(0);

            for (TranslationRegistry translationRegistry : TranslationRegistry.translationRegistries()) {
                for (TranslationEntry translationEntry : translationRegistry.translationEntries()) {
                    translationApi.module(translationRegistry.module().description().name()).ifPresentOrElse(
                            moduleDetails -> {
                                de.keeeks.nucleo.modules.translations.api.TranslationEntry entry = translationApi.createTranslationEntry(
                                        moduleDetails,
                                        translationEntry.locale(),
                                        translationEntry.key(),
                                        translationEntry.value()
                                );
                                count.incrementAndGet();
                                System.out.println("Created translation entry: " + entry);
                            },
                            () -> System.err.println("Module not found: " + translationRegistry.module().description().name())
                    );
                }
            }
            actor.getSource().sendMessage(text(count.get() + " Translations have been transferred into the database"));
        });
    }
}