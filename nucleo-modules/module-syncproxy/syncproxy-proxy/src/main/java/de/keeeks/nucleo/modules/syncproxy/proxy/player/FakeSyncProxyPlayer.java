package de.keeeks.nucleo.modules.syncproxy.proxy.player;

import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class FakeSyncProxyPlayer implements ProxiedPlayer {

    private final PendingConnection pendingConnection;
    private final UUID uuid;
    private final String name;

    public FakeSyncProxyPlayer(PendingConnection pendingConnection) {
        this.pendingConnection = pendingConnection;
        this.uuid = pendingConnection.getUniqueId();
        this.name = pendingConnection.getName();
    }

    @Override
    public String getDisplayName() {
        return null;
    }

    @Override
    public void setDisplayName(String s) {

    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent... baseComponents) {

    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent baseComponent) {

    }

    @Override
    public void sendMessage(UUID uuid, BaseComponent... baseComponents) {

    }

    @Override
    public void sendMessage(UUID uuid, BaseComponent baseComponent) {

    }

    @Override
    public void connect(ServerInfo serverInfo) {

    }

    @Override
    public void connect(ServerInfo serverInfo, ServerConnectEvent.Reason reason) {

    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback) {

    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, boolean b) {

    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, boolean b, int i) {

    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {

    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, boolean b, ServerConnectEvent.Reason reason, int i) {

    }

    @Override
    public void connect(ServerConnectRequest serverConnectRequest) {

    }

    @Override
    public Server getServer() {
        return null;
    }

    @Override
    public int getPing() {
        return 0;
    }

    @Override
    public void sendData(String s, byte[] bytes) {

    }

    @Override
    public PendingConnection getPendingConnection() {
        return pendingConnection;
    }

    @Override
    public void chat(String s) {

    }

    @Override
    public ServerInfo getReconnectServer() {
        return null;
    }

    @Override
    public void setReconnectServer(ServerInfo serverInfo) {

    }

    @Override
    public String getUUID() {
        return uuid.toString();
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public Locale getLocale() {
        return Locale.ENGLISH;
    }

    @Override
    public byte getViewDistance() {
        return 0;
    }

    @Override
    public ChatMode getChatMode() {
        return null;
    }

    @Override
    public boolean hasChatColors() {
        return false;
    }

    @Override
    public SkinConfiguration getSkinParts() {
        return null;
    }

    @Override
    public MainHand getMainHand() {
        return null;
    }

    @Override
    public void setTabHeader(BaseComponent baseComponent, BaseComponent baseComponent1) {

    }

    @Override
    public void setTabHeader(BaseComponent[] baseComponents, BaseComponent[] baseComponents1) {

    }

    @Override
    public void resetTabHeader() {

    }

    @Override
    public void sendTitle(Title title) {

    }

    @Override
    public boolean isForgeUser() {
        return false;
    }

    @Override
    public Map<String, String> getModList() {
        return null;
    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void sendMessage(String s) {

    }

    @Override
    public void sendMessages(String... strings) {

    }

    @Override
    public void sendMessage(BaseComponent... baseComponents) {

    }

    @Override
    public void sendMessage(BaseComponent baseComponent) {

    }

    @Override
    public Collection<String> getGroups() {
        return null;
    }

    @Override
    public void addGroups(String... strings) {

    }

    @Override
    public void removeGroups(String... strings) {

    }

    @Override
    public boolean hasPermission(String s) {
        return false;
    }

    @Override
    public void setPermission(String s, boolean b) {

    }

    @Override
    public Collection<String> getPermissions() {
        return null;
    }

    @Override
    public InetSocketAddress getAddress() {
        return null;
    }

    @Override
    public SocketAddress getSocketAddress() {
        return null;
    }

    @Override
    public void disconnect(String s) {

    }

    @Override
    public void disconnect(BaseComponent... baseComponents) {

    }

    @Override
    public void disconnect(BaseComponent baseComponent) {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public Unsafe unsafe() {
        return null;
    }
}