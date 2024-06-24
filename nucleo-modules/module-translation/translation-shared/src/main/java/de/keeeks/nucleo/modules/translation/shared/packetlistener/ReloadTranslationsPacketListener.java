package de.keeeks.nucleo.modules.translation.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.packet.ReloadTranslationsPacket;
import io.nats.client.Message;

@ListenerChannel(TranslationApi.CHANNEL)
public class ReloadTranslationsPacketListener extends PacketListener<ReloadTranslationsPacket> {
    private final DefaultTranslationApi translationApi;

    public ReloadTranslationsPacketListener(DefaultTranslationApi translationApi) {
        super(ReloadTranslationsPacket.class);
        this.translationApi = translationApi;
    }

    @Override
    public void receive(ReloadTranslationsPacket reloadTranslationsPacket, Message message) {
        translationApi.reloadLocal();
    }
}