package de.keeeks.nucleo.modules.translation.shared.packetlistener;

import de.keeeks.nucleo.modules.messaging.packet.ListenerChannel;
import de.keeeks.nucleo.modules.messaging.packet.PacketListener;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.packet.module.ModuleDetailsUpdatePacket;
import io.nats.client.Message;

@ListenerChannel(TranslationApi.CHANNEL)
public class ModuleDetailsUpdatePacketListener extends PacketListener<ModuleDetailsUpdatePacket> {
    private final DefaultTranslationApi translationApi;

    public ModuleDetailsUpdatePacketListener(DefaultTranslationApi translationApi) {
        super(ModuleDetailsUpdatePacket.class);
        this.translationApi = translationApi;
    }

    @Override
    public void receive(
            ModuleDetailsUpdatePacket moduleDetailsUpdatePacket,
            Message message
    ) {
        translationApi.modifyModuleDetails(list -> {
            list.remove(moduleDetailsUpdatePacket.moduleDetails());
            list.add(moduleDetailsUpdatePacket.moduleDetails());
        });
    }
}