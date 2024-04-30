package de.keeeks.nucleo.modules.translations.api.packet.translationentry;

import de.keeeks.nucleo.modules.translations.api.TranslationEntry;

public class TranslationEntryCreatePacket extends TranslationEntryUpdatePacket{
    public TranslationEntryCreatePacket(TranslationEntry translationEntry) {
        super(translationEntry);
    }
}