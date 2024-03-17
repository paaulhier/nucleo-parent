package de.keeeks.nucleo.modules.translation.global;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.translation.global.configuration.ConfigurationTranslationEntry;
import de.keeeks.nucleo.modules.translation.global.configuration.TranslationEntryConfiguration;
import lombok.Getter;
import net.kyori.adventure.translation.GlobalTranslator;
import net.kyori.adventure.translation.Translator;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Logger;

@Getter
public abstract class TranslationRegistry {
    private static final List<TranslationRegistry> translationRegistries = new LinkedList<>();

    private final List<TranslationEntry> translationEntries = new LinkedList<>();

    private final Translator translator = new ComponentTranslator(this::translationEntryAsString);

    private final File translationsFolder;
    private final Logger logger;

    public TranslationRegistry(Module module) {
        this.logger = module.logger();

        translationsFolder = new File(
                module.dataFolder(),
                "translations"
        );
        load();
        GlobalTranslator.translator().addSource(translator);
    }

    public void load() {
        if (!translationsFolder.exists()) {
            translationsFolder.mkdirs();
        }

        File[] availableFiles = translationsFolder.listFiles(
                (dir, name) -> name.endsWith(".json")
        );
        if (availableFiles == null) {
            logger.warning("No translations found");
            return;
        }

        for (File file : availableFiles) {
            logger.info("Loading translations from %s".formatted(file.getName()));
            loadFromJson(file);
        }
    }

    private void loadFromJson(File file) {
        JsonConfiguration jsonConfiguration = JsonConfiguration.create(file);

        String[] fileNameParts = file.getName().substring(
                0,
                file.getName().length() - 5
        ).split("_");

        Locale locale;
        if (fileNameParts.length == 1) {
            locale = Locale.of(fileNameParts[0]);
        } else if (fileNameParts.length == 2) {
            locale = Locale.of(fileNameParts[0], fileNameParts[1]);
        } else if (fileNameParts.length == 3) {
            locale = Locale.of(fileNameParts[0], fileNameParts[1], fileNameParts[2]);
        } else {
            locale = Locale.of("en", "US");
        }

        for (ConfigurationTranslationEntry translationEntry : jsonConfiguration.loadObject(
                TranslationEntryConfiguration.class,
                TranslationEntryConfiguration.createDefault()
        ).translations()) {
            registerEntry(TranslationEntry.of(
                    translationEntry,
                    locale
            ));
        }
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

    public void registerEntry(TranslationEntry translationEntry) {
        logger.info("Registering %s for %s".formatted(translationEntry.key(), translationEntry.locale()));
        translationEntries.add(translationEntry);
    }

    @Deprecated
    public void register(TranslationEntry translationEntry) {
        logger.info("Registering %s for %s".formatted(translationEntry.key(), translationEntry.locale()));
        translationEntries.add(translationEntry);
    }

    public static <T extends TranslationRegistry> T initializeRegistry(T translationRegistry) {
        translationRegistries.add(translationRegistry);
        return translationRegistry;
    }
}