package de.keeeks.nucleo.modules.economy.api.packet;

import de.keeeks.nucleo.modules.economy.api.Economy;
import de.keeeks.nucleo.modules.messaging.packet.Packet;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class EconomyPacket extends Packet {
    protected final Economy economy;
}