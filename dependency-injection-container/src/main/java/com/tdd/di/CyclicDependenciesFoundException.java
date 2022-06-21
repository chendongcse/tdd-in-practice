package com.tdd.di;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class CyclicDependenciesFoundException extends RuntimeException{
    private Set<Class<?>> components = new HashSet<>();

    public CyclicDependenciesFoundException(Class<?> component) {
        components.add(component);
    }

    public CyclicDependenciesFoundException(Class<?> component, CyclicDependenciesFoundException e) {
        components.add(component);
        components.addAll(e.components);
    }

    public CyclicDependenciesFoundException(List<Class<?>> visisting) {
        components.addAll(visisting);
    }

    public Class<?>[] getComponents() {
        return components.toArray(Class<?>[]::new);
    }
}
