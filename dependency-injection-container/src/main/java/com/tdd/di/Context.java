package com.tdd.di;

public class Context {
    public <componentType> void bind(Class<componentType> type, componentType instance) {
    }

    public <componentType> componentType get(Class<componentType> typeClass) {
        return null;
    }
}
