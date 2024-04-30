package de.keeeks.nucleo.modules.translation.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.packet.translationentry.TranslationEntryUpdatePacket;
import io.nats.client.Message;

@ListenerChannel(TranslationApi.CHANNEL)
public class TranslationEntryUpdatePacketListener extends PacketListener<TranslationEntryUpdatePacket> {
    private final DefaultTranslationApi translationApi;

    public TranslationEntryUpdatePacketListener(DefaultTranslationApi translationApi) {
        super(TranslationEntryUpdatePacket.class);
        this.translationApi = translationApi;
    }

    @Override
    public void receive(
            TranslationEntryUpdatePacket translationEntryUpdatePacket,
            Message message
    ) {
        translationApi.modifyTranslationEntries(list -> {
            list.remove(translationEntryUpdatePacket.translationEntry());
            list.add(translationEntryUpdatePacket.translationEntry());
        });
    }
}