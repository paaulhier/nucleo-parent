package de.keeeks.nucleo.core.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public final class FileUtils {

    public static List<File> listJarFiles(File folder) throws IOException {
        return listJarFiles(folder, 1);
    }

    public static List<File> listJarFiles(File folder, int depth) throws IOException {
        var folderPath = folder.toPath();

        if (!Files.exists(folderPath)) {
            Files.createDirectory(folderPath);
        }

        try (var fileStream = Files.walk(folderPath, depth)) {
            return fileStream
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".jar"))
                    .collect(Collectors.toList());
        }
    }
}