package de.keeeks.nucleo.core.api;

public interface ConditionTester {
    boolean test();

    default ConditionTester and(ConditionTester other) {
        return () -> test() && other.test();
    }

    default ConditionTester or(ConditionTester other) {
        return () -> test() || other.test();
    }
}