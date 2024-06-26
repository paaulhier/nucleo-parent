package de.keeeks.nucleo.modules.scoreboard.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.keeeks.nucleo.core.api.Module;
import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AutoUpdatingScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.DynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.ScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoAnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoAutoUpdatingScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoDynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoStaticScoreboardLine;
import fr.mrmicky.fastboard.adventure.FastBoard;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import java.util.logging.Logger;

@Getter
public class NucleoScoreboard implements Scoreboard {
    private static final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();
    private static final ScoreboardSpigotModule module = Module.module(ScoreboardSpigotModule.class);
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    private static final Logger logger = module.logger();

    private final AtomicInteger lineCounter = new AtomicInteger(0);
    private final List<ScoreboardLine> lines = new LinkedList<>();

    private final FastBoard fastBoard;
    private final UUID playerId;

    public NucleoScoreboard(Player player) {
        this.fastBoard = new FastBoard(player);
        this.playerId = player.getUniqueId();
    }

    @Override
    public Scoreboard title(Component title) {
        fastBoard.updateTitle(title);
        return this;
    }

    @Override
    public List<ScoreboardLine> lines() {
        return List.copyOf(lines).stream().filter(Objects::nonNull).toList();
    }

    @Override
    public AutoUpdatingScoreboardLine autoUpdatingLine(int tickInterval, Supplier<Component> component) {
        return createLine(new NucleoAutoUpdatingScoreboardLine(
                this,
                lineCounter.getAndIncrement(),
                tickInterval,
                component
        ));
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
    public AnimatedScoreboardLine animatedLine(int tickInterval, Supplier<List<Component>> lines) {
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
    public void destroy() {
        if (fastBoard.isDeleted()) return;
        fastBoard.delete();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NucleoScoreboard that = (NucleoScoreboard) object;
        return Objects.equals(playerId, that.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(playerId);
    }

    private <T extends ScoreboardLine> T createLine(T line) {
        lines.add(line);
        return line;
    }
}