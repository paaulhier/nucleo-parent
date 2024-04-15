package de.keeeks.nucleo.core.api;

import java.util.HashMap;
import java.util.Map;

public final class ServiceRegistry {
    private static final Map<String, Object> services = new HashMap<>();

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
}