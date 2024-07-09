package de.keeeks.nucleo.modules.translation.spigot.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;

public class OpenWindowTranslationAdapter extends PacketAdapter implements TranslationAdapter {
    public OpenWindowTranslationAdapter(SpigotModule module) {
        super(module.plugin(), ListenerPriority.LOWEST, PacketType.Play.Server.OPEN_WINDOW);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        handleChatComponent(event.getPlayer(), event.getPacket(), 0);
    }
}