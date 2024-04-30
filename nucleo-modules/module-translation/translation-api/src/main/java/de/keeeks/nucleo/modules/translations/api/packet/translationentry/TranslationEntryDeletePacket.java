package de.keeeks.nucleo.modules.translations.api.packet.translationentry;

import de.keeeks.nucleo.modules.messaging.packet.Packet;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TranslationEntryDeletePacket extends Packet {
    private final TranslationEntry translationEntry;
}