package de.keeeks.nucleo.modules.config.json;

import com.google.gson.Gson;
import de.keeeks.nucleo.core.api.json.GsonBuilder;
import de.keeeks.nucleo.modules.config.api.Configuration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.logging.Level;

public class JsonConfiguration extends Configuration<Gson> {

    public JsonConfiguration(File file, Gson handle) {
        super(file, handle);
    }

    public JsonConfiguration(File file) {
        this(file, GsonBuilder.globalGson());
    }

    @Override
    public <T> T loadObject(Class<T> clazz) {
        checkFile();
        try (var fileReader = new FileReader(file)) {
            return handle().fromJson(fileReader, clazz);
        } catch (Throwable e) {
            printException("Failed to read object %s from %s.", clazz.getName(), e);
        }
        return null;
    }

    @Override
    public <T> T saveObject(T t) {
        checkFile();
        try (var fileWriter = new FileWriter(file)) {
            handle().toJson(t, fileWriter);
        } catch (Throwable e) {
            printException("Failed to save object %s to file %s.", t.getClass().getName(), e);
        }
        return t;
    }

    private void printException(String message, String clazz, Throwable e) {
        module.logger().log(
                Level.SEVERE,
                message.formatted(clazz, file.getPath()),
                e
        );
    }

    public static JsonConfiguration create(File file, Gson handle) {
        return new JsonConfiguration(file, handle);
    }

    public static JsonConfiguration create(File file) {
        return new JsonConfiguration(file);
    }

    public static JsonConfiguration create(File parent, String name) {
        return new JsonConfiguration(new File(parent, name + ".json"));
    }

    public static JsonConfiguration create(String path) {
        return new JsonConfiguration(new File(path));
    }
}