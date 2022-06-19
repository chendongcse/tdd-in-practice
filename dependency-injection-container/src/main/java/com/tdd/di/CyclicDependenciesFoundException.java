package com.tdd.di;

import java.util.HashSet;
import java.util.Set;

public class CyclicDependenciesFoundException extends RuntimeException{
    private Set<Class<?>> components = new HashSet<>();

    public CyclicDependenciesFoundException(Class<?> component) {
        components.add(component);
    }

    public CyclicDependenciesFoundException(Class<?> component, CyclicDependenciesFoundException e) {
        components.add(component);
        components.addAll(e.components);
    }

    public Class<?>[] getComponents() {
        return components.toArray(Class<?>[]::new);
    }
}
