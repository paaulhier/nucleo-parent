package de.keeeks.nucleo.modules.moderation.tools.chatclear.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ClearGlobalChatPacket extends ClearChatPacket {
    private final UUID executor;
}