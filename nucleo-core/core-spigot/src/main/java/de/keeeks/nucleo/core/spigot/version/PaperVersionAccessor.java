package de.keeeks.nucleo.core.spigot.version;

import de.keeeks.nucleo.core.api.version.VersionAccessor;
import lombok.Getter;
import org.bukkit.Bukkit;

import java.util.Locale;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class PaperVersionAccessor implements VersionAccessor {
    private static final Pattern versionPattern = Pattern.compile(
            "(?i)\\(MC: (\\d)\\.(\\d+)\\.?(\\d+?)?(?: (Pre-Release|Release Candidate) )?(\\d)?\\)"
    );

    private int minecraftReleaseCandidateVersion;
    private int minecraftPreReleaseVersion;
    private int minecraftPatchVersion;
    private int minecraftVersion;

    public PaperVersionAccessor() {
        this(Bukkit.getVersion());
    }

    private PaperVersionAccessor(String version) {
        Matcher matcher = versionPattern.matcher(version);
        if (matcher.find()) {
            MatchResult matchResult = matcher.toMatchResult();
            try {
                minecraftVersion = Integer.parseInt(matchResult.group(2), 10);
            } catch (Exception ignored) {
            }
            if (matchResult.groupCount() >= 3) {
                try {
                    minecraftPatchVersion = Integer.parseInt(matchResult.group(3), 10);
                } catch (Exception ignored) {
                }
            }
            if (matchResult.groupCount() >= 5) {
                try {
                    final int ver = Integer.parseInt(matcher.group(5));
                    if (matcher.group(4).toLowerCase(Locale.ENGLISH).contains("pre")) {
                        minecraftPreReleaseVersion = ver;
                    } else {
                        minecraftReleaseCandidateVersion = ver;
                    }
                } catch (Exception ignored) {
                }
            }
        }
    }
}