package de.keeeks.nucleo.core.api.utils;

import java.util.List;

public interface ListModifier<T> {

    List<T> modify(List<T> list);

}