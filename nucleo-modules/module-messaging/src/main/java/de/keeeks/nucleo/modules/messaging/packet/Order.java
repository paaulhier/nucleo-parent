package de.keeeks.nucleo.modules.messaging.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static de.keeeks.nucleo.modules.messaging.packet.ListenerOrder.NORMAL;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    ListenerOrder value() default NORMAL;
}