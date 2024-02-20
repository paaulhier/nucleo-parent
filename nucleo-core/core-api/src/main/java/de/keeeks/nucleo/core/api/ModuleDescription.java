package de.keeeks.nucleo.core.api;

import org.intellij.lang.annotations.Subst;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleDescription {

    String name();

    String description() default "";

    String[] authors() default "Keeeks Development Team";

    String[] depends() default "";

    String[] softDepends() default "";

    String version() default "1.0-SNAPSHOT";

}