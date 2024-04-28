package de.keeeks.nucleo.modules.moderation.tools.chatclear.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ClearServerChatPacket extends ClearChatPacket {
    private final String server;
    private final UUID executor;
}