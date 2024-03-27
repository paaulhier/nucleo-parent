package de.keeeks.nucleo.modules.economy.api;

import java.util.UUID;

public interface EconomyBalanceModifier {
    double modifyBalance(double currentBalance);
}