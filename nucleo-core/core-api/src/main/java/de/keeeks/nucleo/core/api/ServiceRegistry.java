package de.keeeks.nucleo.core.api;

import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class ServiceRegistry {
    private static final Map<String, Object> services = new HashMap<>();

    @Getter
    private static String serviceName;

    static {
        File nucleoPropertiesFile = new File("nucleo.properties");
        if (!nucleoPropertiesFile.exists()) {
            try {
                nucleoPropertiesFile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try (var inputStream = new FileInputStream("nucleo.properties")) {
            Properties properties = new Properties();
            properties.load(inputStream);
            serviceName = properties.getProperty("service.name");
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load nucleo.properties", e);
        }
    }

    public static <T> T registerService(Class<T> clazz, T service) {
        registerService(clazz.getName(), service);
        Class<?> serviceClass = service.getClass();
        if (serviceClass != clazz) {
            registerService(serviceClass.getName(), service);
        }
        return service;
    }

    public static <T> T registerService(String name, T service) {
        services.put(name, service);
        return service;
    }

    public static <T> T service(String name) {
        return (T) services.get(name);
    }

    public static <T> T service(Class<T> clazz) {
        return (T) services.get(clazz.getName());
    }

    public static boolean isRegistered(String name) {
        return services.containsKey(name);
    }

    public static boolean isRegistered(Class<?> clazz) {
        return isRegistered(clazz.getName());
    }
}