package de.keeeks.nucleo.modules.web.configuration;

import java.net.InetAddress;
import java.net.UnknownHostException;

public record WebConfiguration(
        String host,
        int port,
        boolean devLogging,
        AuthenticationType authenticationType
) {

    public static WebConfiguration defaultConfiguration() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return new WebConfiguration(localHost.getHostAddress(), 8080, true, AuthenticationType.NONE);
        } catch (UnknownHostException e) {
            return new WebConfiguration("localhost", 8080, true, AuthenticationType.NONE);
        }
    }

    public enum AuthenticationType {
        NONE,
        BASIC,
        API_KEY,
        CUSTOM
    }
}