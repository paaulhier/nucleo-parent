package de.keeeks.nucleo.modules.moderation.tools.chatclear.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class ClearPlayerChatPacket extends ClearChatPacket{
    private final UUID uuid;
    private final UUID executor;
}