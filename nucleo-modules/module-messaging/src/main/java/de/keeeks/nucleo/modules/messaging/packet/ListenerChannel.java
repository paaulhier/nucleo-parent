package de.keeeks.nucleo.modules.messaging.packet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define the channel a packet listener listens to.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ListenerChannel {

    /**
     * The channel the packet listener listens to.
     *
     * @return The channel the packet listener listens to.
     */
    String value();

    /**
     * The priority of the packet listener. The lower the priority, the earlier the listener is called.
     *
     * @return The priority of the packet listener.
     * @deprecated Use {@link Order} instead.
     */
    @Deprecated
    int priority() default 0;

}