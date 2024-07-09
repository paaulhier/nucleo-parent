package de.keeeks.nucleo.modules.translation.spigot.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;

public class WindowItemsTranslationAdapter extends PacketAdapter implements TranslationAdapter {
    public WindowItemsTranslationAdapter(SpigotModule module) {
        super(module.plugin(), ListenerPriority.LOWEST, PacketType.Play.Server.WINDOW_ITEMS);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        handleItemList(event.getPlayer(), event.getPacket(), 0);
    }
}