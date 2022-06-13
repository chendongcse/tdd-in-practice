package com.tdd.di;

import jakarta.inject.Provider;

import java.util.HashMap;
import java.util.Map;

public class Context {
    Map<Class<?>, Class<?>> componentImplementations = new HashMap<>();
    Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <componentType> void bind(Class<componentType> type, componentType instance) {
        providers.put(type, (Provider<componentType>) () -> instance);
    }

    public <componentType> componentType get(Class<componentType> type) {
        if (providers.containsKey(type))
            return (componentType) providers.get(type).get();
        Class<?> implementation = componentImplementations.get(type);
        try {
            return (componentType) implementation.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <ComponentType, ComponentImpl extends ComponentType>
    void bind(Class<ComponentType> type, Class<ComponentImpl> implementation) {
        componentImplementations.put(type, implementation);
    }
}

