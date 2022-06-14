package com.tdd.di;

import jakarta.inject.Provider;

import java.util.HashMap;
import java.util.Map;

public class Context {
    Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <componentType> void bind(Class<componentType> type, componentType instance) {
        providers.put(type, (Provider<componentType>) () -> instance);
    }

    public <componentType> componentType get(Class<componentType> type) {
        return (componentType) providers.get(type).get();
    }

    public <ComponentType, ComponentImpl extends ComponentType>
    void bind(Class<ComponentType> type, Class<ComponentImpl> implementation) {
        providers.put(type, (Provider<ComponentImpl>) () -> {
            try {
                return (ComponentImpl) ((Class<?>) implementation).getConstructor().newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}

