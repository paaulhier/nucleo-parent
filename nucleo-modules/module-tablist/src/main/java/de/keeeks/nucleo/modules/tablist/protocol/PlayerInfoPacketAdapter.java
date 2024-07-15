package de.keeeks.nucleo.modules.tablist.protocol;

import com.comphenix.packetwrapper.wrappers.play.clientbound.WrapperPlayServerPlayerInfo;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import de.keeeks.nucleo.modules.tablist.event.PlayerTabListForPlayerEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.comphenix.protocol.PacketType.Play.Server.PLAYER_INFO;
import static de.keeeks.nucleo.modules.tablist.event.PlayerTabListForPlayerEvent.TargetType.TAB_LIST;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.space;

public class PlayerInfoPacketAdapter extends PacketAdapter {
    private static final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();

    /**
     * Initialize a packet listener with the given parameters.
     *
     * @param plugin - the plugin.
     */
    public PlayerInfoPacketAdapter(Plugin plugin) {
        super(plugin, ListenerPriority.HIGHEST, PLAYER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrapperPlayServerPlayerInfo wrapperPlayServerPlayerInfo = new WrapperPlayServerPlayerInfo(event.getPacket());

        List<PlayerInfoData> modifiedPlayerInfoData = new ArrayList<>();

        for (PlayerInfoData playerInfoData : wrapperPlayServerPlayerInfo.getEntries()) {
            UUID profileId = playerInfoData.getProfileId();
            Player player = Bukkit.getPlayer(profileId);
            //TODO: Implement handling offline players
            if (player == null) {
                modifiedPlayerInfoData.add(playerInfoData);
                continue;
            }

            // Create a new event for the player and viewer and call it
            PlayerTabListForPlayerEvent tabListForPlayerEvent = new PlayerTabListForPlayerEvent(
                    player,
                    event.getPlayer(),
                    TAB_LIST
            );
            Bukkit.getPluginManager().callEvent(tabListForPlayerEvent);

            // If the event is cancelled, skip the player
            if (tabListForPlayerEvent.isCancelled()) {
                modifiedPlayerInfoData.add(playerInfoData);
                continue;
            }

            Component prefix = tabListForPlayerEvent.prefix();
            Component suffix = tabListForPlayerEvent.suffix();

            // Create a new display name with the prefix and suffix

            Component displayName = null;
            if (!prefix.equals(empty())) {
                displayName = prefix;
            }

            if (displayName == null) {
                displayName = empty();
            }

            Style coloredStyle = Style.style(
                    tabListForPlayerEvent.color()
            );
            displayName = displayName.append(
                    player.displayName().style(coloredStyle)
            ).style(coloredStyle);

            if (!suffix.equals(empty())) {
                displayName = displayName.append(space().append(suffix));
            }

            PlayerInfoData modifiedData = new PlayerInfoData(
                    playerInfoData.getProfile(),
                    playerInfoData.getLatency(),
                    playerInfoData.getGameMode(),
                    WrappedChatComponent.fromJson(gsonComponentSerializer.serialize(displayName)),
                    playerInfoData.getProfileKeyData()
            );
            modifiedPlayerInfoData.add(modifiedData);
        }

        wrapperPlayServerPlayerInfo.setEntries(modifiedPlayerInfoData);
        event.setPacket(wrapperPlayServerPlayerInfo.getHandle());
    }
}