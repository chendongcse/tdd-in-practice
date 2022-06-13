package com.tdd.di;

import java.util.HashMap;
import java.util.Map;

public class Context {
    Map<Class<?>, Object> components = new HashMap<>();

    public <componentType> void bind(Class<componentType> type, componentType instance) {
        components.put(type, instance);
    }

    public <componentType> componentType get(Class<componentType> typeClass) {
        return (componentType) components.get(typeClass);
    }
}

