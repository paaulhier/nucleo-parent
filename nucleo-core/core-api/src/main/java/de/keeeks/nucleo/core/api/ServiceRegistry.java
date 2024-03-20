package de.keeeks.nucleo.core.api;

import java.util.HashMap;
import java.util.Map;

public final class ServiceRegistry {
    private static final Map<Class<?>, Object> services = new HashMap<>();

    public static <T> T registerService(Class<T> clazz, T service) {
        services.put(clazz, service);
        Class<?> serviceClass = service.getClass();
        if (serviceClass != clazz) {
            services.put(serviceClass, service);
        }
        return service;
    }

    public static <T> T service(Class<T> clazz) {
        return (T) services.get(clazz);
    }
}