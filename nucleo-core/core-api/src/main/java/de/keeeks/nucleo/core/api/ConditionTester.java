package de.keeeks.nucleo.core.api;

public interface ConditionTester {
    boolean test();

    default ConditionTester and(ConditionTester other) {
        return () -> test() && other.test();
    }

    default ConditionTester or(ConditionTester other) {
        return () -> test() || other.test();
    }

    default ConditionTester not() {
        return () -> !test();
    }

    default ConditionTester xor(ConditionTester other) {
        return () -> test() ^ other.test();
    }

    default ConditionTester nand(ConditionTester other) {
        return () -> !(test() && other.test());
    }

    default ConditionTester nor(ConditionTester other) {
        return () -> !(test() || other.test());
    }

    static ConditionTester of(boolean value) {
        return () -> value;
    }
}