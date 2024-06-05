package de.keeeks.nucleo.core.api.utils;

import java.util.Collection;

public interface ListModifier<T> {
    void modify(Collection<T> list);
}