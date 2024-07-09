package de.keeeks.nucleo.modules.translation.spigot.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;

public class EquipmentTranslationAdapter extends PacketAdapter implements TranslationAdapter {
    public EquipmentTranslationAdapter(SpigotModule module) {
        super(module.plugin(), ListenerPriority.LOWEST, PacketType.Play.Server.ENTITY_EQUIPMENT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        if (event.getPacket().getItemModifier().size() == 0) return;
        handleItem(event.getPlayer(), event.getPacket(), 0);
    }
}