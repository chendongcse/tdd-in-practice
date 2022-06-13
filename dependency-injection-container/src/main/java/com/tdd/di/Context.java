package com.tdd.di;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Context {
    Map<Class<?>, Object> components = new HashMap<>();
    Map<Class<?>, Class<?>> componentImplementations = new HashMap<>();

    public <componentType> void bind(Class<componentType> type, componentType instance) {
        components.put(type, instance);
    }

    public <componentType> componentType get(Class<componentType> type) {
        if (components.containsKey(type))
            return (componentType) components.get(type);
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

