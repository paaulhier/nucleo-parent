package de.keeeks.nucleo.core.api;

public @interface Dependency {
    String name();
    boolean required() default false;
}