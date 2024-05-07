package de.keeeks.nucleo.modules.scoreboard.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.DynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.ScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoAnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoDynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoStaticScoreboardLine;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

@Getter
public class NucleoScoreboard implements Scoreboard {
    private static final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();
    private static final ScoreboardSpigotModule module = Module.module(ScoreboardSpigotModule.class);
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private final AtomicInteger lineCounter = new AtomicInteger(0);
    private final List<ScoreboardLine> lines = new LinkedList<>();
    private final List<UUID> viewer = new LinkedList<>();

    private final FastBoard fastBoard;
    private final Player player;
    private final UUID uuid;

    public NucleoScoreboard(Player player) {
        this.fastBoard = new FastBoard(player);
        this.player = player;
        this.uuid = UUID.randomUUID();
        player.setMetadata("scoreboardId", new FixedMetadataValue(
                module.plugin(),
                uuid.toString()
        ));
    }

    @Override
    public Scoreboard title(Component title) {
        fastBoard.updateTitle(title);
        return this;
    }

    @Override
    public List<ScoreboardLine> lines() {
        return lines;
    }

    @Override
    public DynamicScoreboardLine dynamicLine(Supplier<Component> supplier) {
        return createLine(new NucleoDynamicScoreboardLine(
                this,
                lineCounter.getAndIncrement(),
                supplier
        ));
    }

    @Override
    public ScoreboardLine staticLine(Component component) {
        return createLine(new NucleoStaticScoreboardLine(
                this,
                lineCounter.getAndIncrement(),
                component
        ));
    }

    @Override
    public AnimatedScoreboardLine animatedLine(int tickInterval, List<Component> lines) {
        return createLine(new NucleoAnimatedScoreboardLine(
                this,
                lineCounter.getAndIncrement(),
                tickInterval,
                lines
        ));
    }

    @Override
    public void renderAll() {
        lines.forEach(ScoreboardLine::render);
    }

    @Override
    public void addPlayer(Player player) {
        viewer.add(player.getUniqueId());
        player.setMetadata("scoreboardId", new FixedMetadataValue(
                module.plugin(),
                uuid.toString()
        ));
        renderAll();
    }

    @Override
    public void destroy() {
        player.removeMetadata("scoreboardId", module.plugin());
        fastBoard.delete();
    }

    private <T extends ScoreboardLine> T createLine(T line) {
        lines.add(line);
        return line;
    }
}