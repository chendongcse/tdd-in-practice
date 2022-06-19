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

public class ContextConfig{
    private Map<Class<?>, Provider<?>> providers = new HashMap<>();
    private Map<Class<?>, ComponentProvider<?>> componentProviders = new HashMap<>();

    public <Type> void bind(Class<Type> type, Type instance) {
        providers.put(type, (Provider<Type>) () -> instance);
        componentProviders.put(type, context -> instance);
    }

    public <Type, Implementation extends Type>
    void bind(Class<Type> type, Class<Implementation> implementation) {
        Constructor<Implementation> injectConstructor = getInjectConstructor(implementation);
        providers.put(type, new ConstructorInjectionProvider(type, injectConstructor));
        componentProviders.put(type, new ConstructorInjectionProvider(type, injectConstructor));
    }

    public Context getContext(){
        // 在这里做检查
        return new Context() {
            @Override
            public <Type> Optional<Type> get(Class<Type> type) {
                return Optional.ofNullable(componentProviders.get(type)).map(provider -> (Type) provider.get(this));
            }
        };
    }

    interface ComponentProvider<T>{
        T get(Context context);
    }

    class ConstructorInjectionProvider<T> implements Provider<T>, ComponentProvider<T> {
        private Class<?> componentType;
        private Constructor<T> injectConstructor;
        private boolean constructing = false;

        public ConstructorInjectionProvider(Class<?> componentType, Constructor<T> injectConstructor) {
            this.componentType = componentType;
            this.injectConstructor = injectConstructor;
        }

        @Override
        public T get() {
            return getT(getContext());
        }

        private T getT(Context context) {
            if (constructing) throw new CyclicDependenciesFoundException(componentType);
            try {
                constructing = true;
                Object[] dependencies = stream(injectConstructor.getParameters())
                        .map(p -> context.get(p.getType())
                                .orElseThrow(() -> new DependencyNotFoundException(componentType,p.getType())))
                        .toArray(Object[]::new);
                return injectConstructor.newInstance(dependencies);
            } catch (CyclicDependenciesFoundException e) {
                throw new CyclicDependenciesFoundException(componentType, e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                constructing = false;
            }
        }

        @Override
        public T get(Context context) {
            if (constructing) throw new CyclicDependenciesFoundException(componentType);
            try {
                constructing = true;
                Object[] dependencies = stream(injectConstructor.getParameters())
                        .map(p -> getContext().get(p.getType())
                                .orElseThrow(() -> new DependencyNotFoundException(componentType,p.getType())))
                        .toArray(Object[]::new);
                return injectConstructor.newInstance(dependencies);
            } catch (CyclicDependenciesFoundException e) {
                throw new CyclicDependenciesFoundException(componentType, e);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                constructing = false;
            }
        }
    }

    private <Type> Constructor<Type> getInjectConstructor(Class<Type> implementation) {
        List<Constructor<?>> injectConstructors = stream(implementation.getConstructors())
                .filter(c -> c.isAnnotationPresent(Inject.class)).collect(Collectors.toList());
        if (injectConstructors.size() > 1) throw new IllegalComponentException();
        return (Constructor<Type>) injectConstructors.stream().findFirst().orElseGet(() -> {
            try {
                return implementation.getConstructor();
            } catch (NoSuchMethodException e) {
                throw new IllegalComponentException();
            }
        });
    }
}

