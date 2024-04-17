package de.keeeks.nucleo.modules.scoreboard.spigot;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import de.keeeks.nucleo.modules.scoreboard.api.Scoreboard;
import de.keeeks.nucleo.modules.scoreboard.api.lines.AnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.DynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.api.lines.ScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoAnimatedScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoDynamicScoreboardLine;
import de.keeeks.nucleo.modules.scoreboard.spigot.lines.NucleoStaticScoreboardLine;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Team;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static net.kyori.adventure.text.Component.text;

@Getter
public class NucleoScoreboard implements Scoreboard {
    private static final GsonComponentSerializer gsonComponentSerializer = GsonComponentSerializer.gson();
    private static final ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

    private final List<ScoreboardLine> lines = new LinkedList<>();
    private final List<UUID> viewer = new LinkedList<>();

    private final org.bukkit.scoreboard.Scoreboard scoreboard;

    private final UUID uuid;

    public NucleoScoreboard(Player player) {
        this.scoreboard = player.getScoreboard();
        this.uuid = UUID.randomUUID();
    }

    public NucleoScoreboard(org.bukkit.scoreboard.Scoreboard legacyBoard) {
        this.scoreboard = legacyBoard;
        this.uuid = UUID.randomUUID();
    }

    @Override
    public Scoreboard title(Component title) {
        objective().ifPresent(objective -> objective.displayName(title));
        return this;
    }

    @Override
    public List<ScoreboardLine> lines() {
        return lines;
    }

    @Override
    public DynamicScoreboardLine dynamicLine(int index, Supplier<Component> supplier) {
        return createLine(new NucleoDynamicScoreboardLine(
                this,
                index,
                supplier
        ));
    }

    @Override
    public ScoreboardLine staticLine(int index, Component component) {
        return createLine(new NucleoStaticScoreboardLine(
                this,
                index,
                component
        ));
    }

    @Override
    public AnimatedScoreboardLine animatedLine(int index, int tickInterval, List<Component> lines) {
        return createLine(new NucleoAnimatedScoreboardLine(
                this,
                index,
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
        player.setScoreboard(scoreboard);
        renderAll();
    }

    public final Optional<Objective> objective() {
        return Optional.ofNullable(scoreboard.getObjective("nucleo-scoreboard")).or(() -> {
            Objective objective = scoreboard.registerNewObjective(
                    "nucleo-scoreboard",
                    Criteria.DUMMY,
                    text("nucleo-scoreboard")
            );
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            return Optional.of(objective);
        });
    }

    public final Optional<Team> team(int index) {
        return Optional.ofNullable(scoreboard.getTeam(teamName(index))).or(() -> {
            Team team = scoreboard.registerNewTeam(teamName(index));
            team.addEntry(teamName(index));
            return Optional.of(team);
        });
    }

    public final String teamName(int index) {
        return "nucleo-line-" + index;
    }

    private <T extends ScoreboardLine> T createLine(T line) {
        lines.add(line);
        return line;
    }
}