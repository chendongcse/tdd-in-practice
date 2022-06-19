package com.tdd.di;

import jakarta.inject.Inject;
import jakarta.inject.Provider;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class Context {
    Map<Class<?>, Provider<?>> providers = new HashMap<>();

    public <Type> void bind(Class<Type> type, Type instance) {
        providers.put(type, (Provider<Type>) () -> instance);
    }

    public <Type, Implementation extends Type>
    void bind(Class<Type> type, Class<Implementation> implementation) {
        Constructor<Implementation> injectConstructor = getInjectConstructor(implementation);
        providers.put(type, new ConstructorInjectionProvider(injectConstructor));
    }

    class ConstructorInjectionProvider<T> implements Provider<T> {
        private Constructor<T> injectConstructor;
        private boolean constructing = false;

        public ConstructorInjectionProvider(Constructor<T> injectConstructor) {
            this.injectConstructor = injectConstructor;
        }

        @Override
        public T get() {
            if (constructing)
                throw new CyclicDependenciesFound();
            try {
                constructing = true;
                Object[] dependencies = stream(injectConstructor.getParameters())
                        .map(p -> Context.this.get(p.getType()).orElseThrow(DependencyNotFoundException::new))
                        .toArray(Object[]::new);
                return injectConstructor.newInstance(dependencies);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                constructing = true;
            }
        }
    }

    private <Type> Constructor<Type> getInjectConstructor(Class<Type> implementation) {
        List<Constructor<?>> injectConstructors = stream(implementation.getConstructors()).filter(c -> c.isAnnotationPresent(Inject.class)).collect(Collectors.toList());
        if (injectConstructors.size() > 1)
            throw new IllegalComponentException();
        return (Constructor<Type>) injectConstructors.stream().findFirst().orElseGet(() -> {
            try {
                return implementation.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalComponentException();
            }
        });
    }

    public <Type> Optional<Type> get(Class<Type> type) {
        return  Optional.ofNullable(providers.get(type)).map(provider -> (Type) provider.get());
    }
}

