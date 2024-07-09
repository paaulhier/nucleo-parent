package de.keeeks.nucleo.modules.translation.spigot.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;

public class SetSlotTranslationAdapter extends PacketAdapter implements TranslationAdapter {
    public SetSlotTranslationAdapter(SpigotModule module) {
        super(module.plugin(), ListenerPriority.LOWEST, PacketType.Play.Server.SET_SLOT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        handleItem(event.getPlayer(), event.getPacket(), 0);
    }
}