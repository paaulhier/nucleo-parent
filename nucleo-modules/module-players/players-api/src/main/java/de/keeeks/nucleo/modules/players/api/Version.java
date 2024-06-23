package de.keeeks.nucleo.modules.players.api;

import lombok.Getter;

@Getter
public enum Version {

    VERSION_1_20_1(
            "1.20.0/1.20.1",
            "1.20.0",
            "1.20.1",
            763
    ),
    VERSION_1_20_2("1.20.2", 764),
    VERSION_1_20_3(
            "1.20.3/1.20.4",
            "1.20.3",
            "1.20.4",
            765
    ),
    VERSION_1_20_5(
            "1.20.5/1.20.6",
            "1.20.5",
            "1.20.6",
            766
    ),
    VERSION_1_21(
            "1.21",
            767
    );

    private final String version;
    private final String lowestIncludedVersion;
    private final String highestIncludedVersion;
    private final int protocol;

    Version(String version, String lowestIncludedVersion, String highestIncludedVersion, int protocol) {
        this.version = version;
        this.lowestIncludedVersion = lowestIncludedVersion;
        this.highestIncludedVersion = highestIncludedVersion;
        this.protocol = protocol;
    }

    Version(String version, int protocol) {
        this.version = version;
        this.lowestIncludedVersion = version;
        this.highestIncludedVersion = version;
        this.protocol = protocol;
    }

    public static String supportedVersionsAsString() {
        return lowestVersion().lowestIncludedVersion() + " - " + highestVersion().highestIncludedVersion();
    }

    public static Version lowestVersion() {
        return values()[0];
    }

    public static Version highestVersion() {
        return values()[values().length - 1];
    }

    public static Version getByName(String name) {
        for (Version version : values()) {
            if (version.version().equalsIgnoreCase(name)) {
                return version;
            }
        }
        return null;
    }

    public static Version byProtocol(int protocol) {
        for (Version version : values()) {
            if (version.protocol() == protocol) {
                return version;
            }
        }
        return null;
    }
}