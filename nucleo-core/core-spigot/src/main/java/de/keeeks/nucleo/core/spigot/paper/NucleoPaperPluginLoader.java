package de.keeeks.nucleo.core.spigot.paper;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.JarLibrary;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class NucleoPaperPluginLoader implements PluginLoader {

    @Override
    public void classloader(@NotNull PluginClasspathBuilder classpathBuilder) {
        MavenLibraryResolver resolver = new MavenLibraryResolver();

        //Register all external registries
        resolver.addRepository(new RemoteRepository.Builder(
                "xenondevs",
                "default",
                "https://repo.xenondevs.xyz/releases/"
        ).build());

        //Register all dependencies
        resolver.addDependency(new Dependency(
                new DefaultArtifact("xyz.xenondevs.invui:invui:pom:1.33"),
                "compile",
                true
        ));

        classpathBuilder.addLibrary(resolver);
    }
}