package de.keeeks.modules.npc.spigot;

import com.comphenix.packetwrapper.wrappers.play.clientbound.*;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import de.keeeks.modules.npc.api.Animation;
import de.keeeks.modules.npc.api.NPC;
import de.keeeks.modules.npc.api.NPCSkinModel;
import de.keeeks.modules.npc.spigot.pathfinder.NPCPathfinder;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.core.api.scheduler.Scheduler;
import de.keeeks.nucleo.core.api.version.VersionAccessor;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Getter
public class NucleoNPC implements NPC {
    private static final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();
    private static final ProtocolManager protocolManger = ProtocolLibrary.getProtocolManager();
    private static final AtomicInteger entityIdCounter = new AtomicInteger(15000);
    private static final VersionAccessor versionAccessor = VersionAccessor.versionAccessor();
    private static final Logger logger = Module.module("npc").logger();
    private static final NPCPathfinder pathfinder = new NPCPathfinder();
    private static final Random random = new Random();

    private final Component displayName;
    private final UUID uniqueId;
    private final int entityId;

    private NPCSkinModel skinModel;
    private Location location;

    public NucleoNPC(NPCSkinModel skinModel, Component name, Location location) {
        this.skinModel = skinModel;
        this.uniqueId = new UUID(random.nextLong(), 0);
        this.entityId = entityIdCounter.getAndIncrement();

        this.displayName = name;
        this.location = location;
    }

    @Override
    public void teleport(Location location) {
        this.location = location;
        protocolManger.broadcastServerPacket(teleportPacket(location));
        sendLookToPacket(location, protocolManger::broadcastServerPacket);
    }

    @Override
    public void move(Location location, Runnable moveComplete) {
        Scheduler.runAsync(() -> {
            List<Location> locations = pathfinder.findPath(
                    this.location,
                    location
            );

            if (locations == null) {
                logger.warning("Could not find path for NPC from %s to %s".formatted(
                        readableLocation(this.location),
                        readableLocation(location)
                ));
                return;
            }

            for (Location pathLocation : locations) {
                pathLocation.setYaw((float) calculateYaw(this.location, pathLocation));
                pathLocation.setPitch((float) calculatePitch(this.location, pathLocation));
                protocolManger.broadcastServerPacket(movePacket(pathLocation));
                teleport(pathLocation);
                sendLookToPacket(pathLocation, protocolManger::broadcastServerPacket);
                this.location = pathLocation;
                Thread.sleep(200);
            }
            sendLookToPacket(location, protocolManger::broadcastServerPacket);
            moveComplete.run();
        });
    }

    private String readableLocation(Location location) {
        return "%s, %s, %s".formatted(
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
    }

    @Override
    public void playAnimation(Animation animation) {
        PacketContainer packetContainer = switch (animation) {
            case SWING_MAIN_ARM -> animationPacket(WrapperPlayServerAnimation.Animation.SWING_MAIN_ARM);
            case TAKE_DAMAGE -> animationPacket(WrapperPlayServerAnimation.Animation.TAKE_DAMAGE);
            case LEAVE_BED -> animationPacket(WrapperPlayServerAnimation.Animation.LEAVE_BED);
            case SWING_OFFHAND -> animationPacket(WrapperPlayServerAnimation.Animation.SWING_OFFHAND);
            case CRITICAL_EFFECT -> animationPacket(WrapperPlayServerAnimation.Animation.CRITICAL_EFFECT);
            case MAGICAL_CRITICAL_EFFECT ->
                    animationPacket(WrapperPlayServerAnimation.Animation.MAGICAL_CRITICAL_EFFECT);
        };

        protocolManger.broadcastServerPacket(packetContainer);
    }

    @Override
    public void spawn() {
        protocolManger.broadcastServerPacket(playerInfoAddPacket());
        protocolManger.broadcastServerPacket(spawnPacket());
        protocolManger.broadcastServerPacket(metaDataPacket());
        Scheduler.runAsyncLater(
                () -> protocolManger.broadcastServerPacket(playerInfoRemovePacket()),
                150
        );
        sendLookToPacket(location, protocolManger::broadcastServerPacket);
    }

    @Override
    public void spawnForPlayer(Player player) {
        protocolManger.sendServerPacket(player, playerInfoAddPacket());
        protocolManger.sendServerPacket(player, spawnPacket());
        protocolManger.sendServerPacket(player, metaDataPacket());
        protocolManger.broadcastServerPacket(playerInfoRemovePacket());
        sendLookToPacket(location, protocolManger::broadcastServerPacket);
    }

    @Override
    public void remove() {
        protocolManger.broadcastServerPacket(removePacket());
    }

    @Override
    public void removeForPlayer(Player player) {
        protocolManger.sendServerPacket(player, removePacket());
    }

    @Override
    public void setSkin(NPCSkinModel skinModel) {
        remove();
        this.skinModel = skinModel;
        Scheduler.runAsyncLater(this::spawn, 1000);
    }

    private PacketContainer animationPacket(WrapperPlayServerAnimation.Animation animation) {
        WrapperPlayServerAnimation animationPacket = new WrapperPlayServerAnimation();
        animationPacket.setId(entityId);
        animationPacket.setAction(animation);
        return animationPacket.getHandle();
    }

    private PacketContainer playerInfoAddPacket() {
        WrapperPlayServerPlayerInfo playerInfo = new WrapperPlayServerPlayerInfo();
        playerInfo.setActions(Set.of(
                EnumWrappers.PlayerInfoAction.ADD_PLAYER,
                EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
                EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
                EnumWrappers.PlayerInfoAction.UPDATE_GAME_MODE,
                EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME
        ));

        WrappedGameProfile gameProfile = new WrappedGameProfile(uniqueId, "NPC-" + entityId);
        if (skinModel != null) {
            gameProfile.getProperties().put(
                    "textures",
                    new WrappedSignedProperty(
                            "textures",
                            skinModel.value(),
                            skinModel.signature()
                    )
            );
            System.out.println("Skin set");
        }

        playerInfo.setEntries(List.of(new PlayerInfoData(
                uniqueId,
                20,
                true,
                EnumWrappers.NativeGameMode.SURVIVAL,
                gameProfile,
                WrappedChatComponent.fromJson(gsonComponentSerializer.serialize(displayName))
        )));
        System.out.println("Player info added -> " + gsonComponentSerializer.serialize(displayName));

        return playerInfo.getHandle();
    }

    private PacketContainer playerInfoRemovePacket() {
        WrapperPlayServerPlayerInfoRemove playerInfo = new WrapperPlayServerPlayerInfoRemove();
        playerInfo.setProfileIds(List.of(uniqueId));
        return playerInfo.getHandle();
    }

    private PacketContainer metaDataPacket() {
        WrapperPlayServerEntityMetadata metadata = new WrapperPlayServerEntityMetadata();
        metadata.setId(entityId);
        metadata.setPackedItems(List.of(new WrappedDataValue(
                17,
                WrappedDataWatcher.Registry.get(Byte.class),
                (byte) 127
        )));
        return metadata.getHandle();
    }

    private PacketContainer spawnPacket() {
        WrapperPlayServerSpawnEntity spawnEntity = new WrapperPlayServerSpawnEntity();
        spawnEntity.setType(EntityType.PLAYER);
        spawnEntity.setId(entityId);
        spawnEntity.setUuid(uniqueId);
        spawnEntity.setX(location.getX());
        spawnEntity.setY(location.getY());
        spawnEntity.setZ(location.getZ());
        spawnEntity.setXRot((byte) (location.getYaw() * 256F / 360F));
        spawnEntity.setYRot((byte) (location.getPitch() * 256F / 360F));
        spawnEntity.setData(127);
        return spawnEntity.getHandle();
    }

    private PacketContainer removePacket() {
        WrapperPlayServerEntityDestroy destroyEntity = new WrapperPlayServerEntityDestroy();
        destroyEntity.setEntityIds(IntList.of(entityId));
        return destroyEntity.getHandle();
    }

    private PacketContainer teleportPacket(Location location) {
        WrapperPlayServerEntityTeleport wrapperPlayServerEntityTeleport = new WrapperPlayServerEntityTeleport();
        wrapperPlayServerEntityTeleport.setId(entityId);
        wrapperPlayServerEntityTeleport.setX(location.getX());
        wrapperPlayServerEntityTeleport.setY(location.getY());
        wrapperPlayServerEntityTeleport.setZ(location.getZ());
        wrapperPlayServerEntityTeleport.setYRot((byte) (location.getYaw() * 256F / 360F));
        wrapperPlayServerEntityTeleport.setXRot((byte) (location.getPitch() * 256F / 360F));
        return wrapperPlayServerEntityTeleport.getHandle();

    }

    private @NotNull PacketContainer movePacket(Location pathLocation) {
        WrapperPlayServerRelEntityMove wrapperPlayServerRelEntityMove = new WrapperPlayServerRelEntityMove();
        wrapperPlayServerRelEntityMove.setEntityId(entityId);
        wrapperPlayServerRelEntityMove.setDeltaX((short) (pathLocation.getX() - this.location.getX()));
        wrapperPlayServerRelEntityMove.setDeltaY((short) (pathLocation.getY() - this.location.getY()));
        wrapperPlayServerRelEntityMove.setDeltaZ((short) (pathLocation.getZ() - this.location.getZ()));
        return wrapperPlayServerRelEntityMove.getHandle();
    }

    private void sendLookToPacket(Location pathLocation, Consumer<PacketContainer> consumer) {
        consumer.accept(lookPacket(pathLocation));
        consumer.accept(headRotationPacket(pathLocation));
    }

    private PacketContainer lookPacket(Location pathLocation) {
        WrapperPlayServerEntityLook wrapperPlayServerEntityLook = new WrapperPlayServerEntityLook();
        wrapperPlayServerEntityLook.setEntityId(entityId);
        wrapperPlayServerEntityLook.setYaw(pathLocation.getYaw());
        wrapperPlayServerEntityLook.setPitch(pathLocation.getPitch());
        return wrapperPlayServerEntityLook.getHandle();
    }

    private PacketContainer headRotationPacket(Location pathLocation) {
        WrapperPlayServerEntityHeadRotation wrapperPlayServerEntityHeadRotation = new WrapperPlayServerEntityHeadRotation();
        wrapperPlayServerEntityHeadRotation.setEntityId(entityId);
        wrapperPlayServerEntityHeadRotation.setYHeadRotAngle(pathLocation.getYaw());
        return wrapperPlayServerEntityHeadRotation.getHandle();
    }

    private double calculateYaw(Location from, Location to) {
        double x = to.getX() - from.getX();
        double z = to.getZ() - from.getZ();
        double yaw = Math.toDegrees(Math.atan2(z, x)) - 90;
        return yaw < 0 ? yaw + 360 : yaw;
    }

    private double calculatePitch(Location from, Location to) {
        double x = to.getX() - from.getX();
        double y = to.getY() - from.getY();
        double z = to.getZ() - from.getZ();
        double distance = Math.sqrt(x * x + z * z);
        return -Math.toDegrees(Math.atan2(y, distance));
    }
}
