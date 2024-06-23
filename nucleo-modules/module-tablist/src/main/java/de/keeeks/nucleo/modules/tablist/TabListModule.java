package de.keeeks.nucleo.modules.tablist;

import com.comphenix.protocol.ProtocolLibrary;
import de.keeeks.nucleo.core.api.ModuleDescription;
import de.keeeks.nucleo.core.spigot.module.SpigotModule;
import de.keeeks.nucleo.modules.tablist.listener.TabListPlayerJoinListener;
import de.keeeks.nucleo.modules.tablist.protocol.PlayerInfoPacketAdapter;
import de.keeeks.nucleo.modules.tablist.service.TabListService;

@ModuleDescription(name = "tablist")
public final class TabListModule extends SpigotModule {

    @Override
    public void load() {
        registerService(TabListService.class, new TabListService());
    }

    @Override
    public void enable() {
        registerListener(new TabListPlayerJoinListener());
        ProtocolLibrary.getProtocolManager().addPacketListener(new PlayerInfoPacketAdapter(plugin));
    }
}