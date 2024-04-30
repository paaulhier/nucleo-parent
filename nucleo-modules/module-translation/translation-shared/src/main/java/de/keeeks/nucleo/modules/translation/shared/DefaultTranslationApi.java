package de.keeeks.nucleo.modules.translation.shared;

import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.ServiceRegistry;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.utils.ListModifier;
import de.keeeks.nucleo.modules.config.json.JsonConfiguration;
import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.database.sql.MysqlCredentials;
import de.keeeks.nucleo.modules.messaging.NatsConnection;
import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.translation.shared.json.ModuleDetailsSerializer;
import de.keeeks.nucleo.modules.translation.shared.json.TranslationEntrySerializer;
import de.keeeks.nucleo.modules.translation.shared.packetlistener.ModuleDetailsUpdatePacketListener;
import de.keeeks.nucleo.modules.translation.shared.packetlistener.TranslationEntryUpdatePacketListener;
import de.keeeks.nucleo.modules.translation.shared.sql.ModuleDetailsRepository;
import de.keeeks.nucleo.modules.translation.shared.sql.TranslationEntryRepository;
import de.keeeks.nucleo.modules.translations.api.ModuleDetails;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;
import de.keeeks.nucleo.modules.translations.api.packet.module.ModuleDetailsCreatePacket;
import de.keeeks.nucleo.modules.translations.api.packet.translationentry.TranslationEntryCreatePacket;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DefaultTranslationApi implements TranslationApi {
    private final List<ModuleDetails> moduleDetails = new ArrayList<>();
    private final List<TranslationEntry> translationEntries = new ArrayList<>();

    private final NatsConnection natsConnection = ServiceRegistry.service(NatsConnection.class);

    private final TranslationEntryRepository translationEntryRepository;
    private final ModuleDetailsRepository moduleDetailsRepository;

    public DefaultTranslationApi(Module module) {
        MysqlConnection mysqlConnection = MysqlConnection.create(JsonConfiguration.create(
                module.dataFolder(),
                "mysql"
        ).loadObject(MysqlCredentials.class, MysqlCredentials.defaultCredentials()));

        GsonBuilder.registerSerializer(
                new ModuleDetailsSerializer(),
                new TranslationEntrySerializer()
        );

        this.translationEntryRepository = new TranslationEntryRepository(this, mysqlConnection);
        this.moduleDetailsRepository = new ModuleDetailsRepository(mysqlConnection);
        natsConnection.registerPacketListener(
                new ModuleDetailsUpdatePacketListener(this),
                new TranslationEntryUpdatePacketListener(this)
        );

        moduleDetails.addAll(moduleDetailsRepository.moduleDetails());
        translationEntries.addAll(translationEntryRepository.translationEntries());
    }

    public void modifyModuleDetails(ListModifier<ModuleDetails> modifier) {
        modifier.modify(moduleDetails);
    }

    public void modifyTranslationEntries(ListModifier<TranslationEntry> modifier) {
        modifier.modify(translationEntries);
    }

    @Override
    public ModuleDetails createModule(String name) {
        return module(name).orElseGet(() -> {
            ModuleDetails moduleDetails = moduleDetailsRepository.createModule(name);
            publishPacket(new ModuleDetailsCreatePacket(moduleDetails));
            return moduleDetails;
        });
    }

    @Override
    public List<ModuleDetails> modules() {
        return List.copyOf(moduleDetails);
    }

    @Override
    public TranslationEntry createTranslationEntry(ModuleDetails moduleDetails, Locale locale, String key, String value) {
        return translationEntry(
                moduleDetails,
                key,
                locale
        ).orElseGet(() -> {
            TranslationEntry translationEntry = translationEntryRepository.createTranslationEntry(
                    moduleDetails,
                    locale,
                    key,
                    value
            );
            translationEntries.add(translationEntry);
            publishPacket(new TranslationEntryCreatePacket(translationEntry));
            return translationEntry;
        });
    }

    @Override
    public List<TranslationEntry> translations() {
        return List.copyOf(translationEntries);
    }

    private void publishPacket(Packet packet) {
        Scheduler.runAsync(() -> natsConnection.publishPacket(
                CHANNEL,
                packet
        ));
    }
}