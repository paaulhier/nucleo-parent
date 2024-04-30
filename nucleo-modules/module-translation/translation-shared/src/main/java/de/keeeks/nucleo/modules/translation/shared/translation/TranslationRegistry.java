package de.keeeks.nucleo.modules.translation.shared.translation;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class TranslationRegistry {
    private static final List<TranslationRegistry> translationRegistries = new LinkedList<>();
    private static final TranslationApi translationApi = ServiceRegistry.service(TranslationApi.class);

    private final List<TranslationEntry> translationEntries = new LinkedList<>();

    private final Module module;

    public TranslationRegistry(Module module) {
        this.module = module;
        translationRegistries.add(this);
        Translator translator = new ComponentTranslator(this::translationEntryAsString);
        GlobalTranslator.translator().addSource(translator);
        load();
    }

    public void reloadAsync() {
        Scheduler.runAsync(() -> {
            translationEntries.clear();
            load();
        });
    }

    private void load() {
        translationEntries.addAll(translationApi.translations(module.description().name()));
        module.logger().info("Loaded %d translations".formatted(translationEntries.size()));
    }

    public String translationEntryAsString(String key, Locale locale) {
        return translationEntries.stream()
                .filter(translationEntry -> translationEntry.key().equals(key))
                .filter(translationEntry -> translationEntry.locale().toString().equals(locale.toString()))
                .map(TranslationEntry::value)
                .findFirst()
                .orElseGet(() -> {
                    if (locale.equals(Locale.GERMANY)) return null;
                    return translationEntryAsString(
                            key,
                            Locale.GERMANY
                    );
                });
    }

    public static List<TranslationRegistry> translationRegistries() {
        return List.copyOf(translationRegistries);
    }

    public static void create(Module module) {
        new TranslationRegistry(module);
    }
}