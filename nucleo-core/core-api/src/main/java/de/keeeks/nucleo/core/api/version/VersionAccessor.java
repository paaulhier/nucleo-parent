package de.keeeks.nucleo.core.api.version;

import de.keeeks.nucleo.core.api.ServiceRegistry;

public interface VersionAccessor {

    static VersionAccessor versionAccessor() {
        return ServiceRegistry.service(VersionAccessor.class);
    }

    int minecraftVersion();

    int minecraftPatchVersion();

    int minecraftPreReleaseVersion();

    int minecraftReleaseCandidateVersion();

    default boolean isVersion(int minor) {
        return minecraftVersion() == minor;
    }

    default boolean isVersion(int minor, int patch) {
        return isVersion(minor) && minecraftPatchVersion() == patch;
    }

    default boolean isVersion(int minor, int patch, int preRelease) {
        return isVersion(minor, patch) && minecraftPreReleaseVersion() == preRelease;
    }

    default boolean atLeast(int minor) {
        return minecraftVersion() >= minor;
    }

    default boolean atLeast(int minor, int patch) {
        return atLeast(minor) && minecraftPatchVersion() >= patch;
    }
}