package com.tdd.di;

import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ContainerTest {

    ContextConfig config;

    @BeforeEach
    public void setUp() {
        config = new ContextConfig();
    }

    @Nested
    public class ConpomentConstruction {
        @Test
        public void should_bind_type_to_a_specific_instance() {
            Component instance = new Component() {

            };
            config.bind(Component.class, instance);

            Context context = config.getContext();
            assertSame(instance, context.get(Component.class).get());

        }

        //todo: interface
        //todo: abstract class
        @Nested
        public class ConstructorInjection {
            @Test
            public void should_bind_type_to_a_class_with_default_constructor() {
                config.bind(Component.class, ComponentWithDefaultConstructor.class);

                Component instance = config.getContext().get(Component.class).get();

                assertNotNull(instance);
                assertTrue(instance instanceof ComponentWithDefaultConstructor);

            }

            @Test
            public void should_return_empty_if_compoenent_not_defined(){
                Optional<Component> component = config.getContext().get(Component.class);
                assertTrue(component.isEmpty());
            }

            @Test
            public void should_bind_type_to_a_class_with_inject_constructor() {
                Dependency dependency = new Dependency() {
                };
                config.bind(Component.class, ComponentWithInjectConstructor.class);
                config.bind(Dependency.class, dependency);

                Component instance = config.getContext().get(Component.class).get();

                assertNotNull(instance);
                assertSame(dependency, ((ComponentWithInjectConstructor) instance).getDependency());
            }

            //todo: A -> B -> C
            @Test
            public void should_bind_type_to_a_class_with_transitive_dependencies() {

                config.bind(Component.class, ComponentWithInjectConstructor.class);
                config.bind(Dependency.class, DependencyWithInjectConstructor.class);
                config.bind(String.class, "indirect dependency");

                Component instance = config.getContext().get(Component.class).get();
                assertNotNull(instance);

                Dependency dependency = ((ComponentWithInjectConstructor) instance).getDependency();
                assertNotNull(dependency);
                assertSame("indirect dependency", ((DependencyWithInjectConstructor) dependency).getDependency());
            }

            @Test
            public void should_throw_exception_if_multi_inject_constructors_provided() {
                assertThrows(IllegalComponentException.class, () -> {
                    config.bind(Component.class, ComponentWithInjectMultiInjectConstructor.class);});

            }

            @Test
            public void should_throw_exception_if_no_inject_nor_default_constructor_provided() {
                assertThrows(IllegalComponentException.class, () -> {
                    config.bind(Component.class, ComponentWithNoInjectConstructorNorDefaultConstructor.class);
                });
            }

            @Test
            public void should_throw_exception_if_dependencies_not_exist() {
                config.bind(Component.class, ComponentWithInjectConstructor.class);
                DependencyNotFoundException exception = assertThrows(DependencyNotFoundException.class, () -> {
                    config.getContext();
                });
                assertEquals(Dependency.class, exception.getDependency());
                assertEquals(Component.class, exception.getComponent());

            }

            @Test
            public void should_throw_exception_if_transitive_dependencies_not_exist() {
                config.bind(Component.class, ComponentWithInjectConstructor.class);
                config.bind(Dependency.class, DependencyWithInjectConstructor.class);
                DependencyNotFoundException exception = assertThrows(DependencyNotFoundException.class, () -> {
                    config.getContext();
                });
                assertEquals(String.class, exception.getDependency());
                assertEquals(Dependency.class, exception.getComponent());

            }

            @Test
            public void should_throw_exception_if_cyclic_dependencies_found(){
                config.bind(Component.class, ComponentWithInjectConstructor.class);
                config.bind(Dependency.class, DependencyDependOnComponent.class);

                CyclicDependenciesFoundException exception = assertThrows(CyclicDependenciesFoundException.class, () -> config.getContext());

                List<Class<?>> classes = Arrays.stream(exception.getComponents()).toList();
                assertEquals(2, classes.size());
                assertTrue(classes.contains(Component.class));
                assertTrue(classes.contains(Dependency.class));
            }

            @Test
            public void should_throw_exception_if_transitive_cyclic_dependencies_found(){
                config.bind(Component.class, ComponentWithInjectConstructor.class);
                config.bind(Dependency.class, DependencyDependOnAnotherDependency.class);
                config.bind(AnotherDependency.class, AnotherDependencyDependOnComponent.class);

                CyclicDependenciesFoundException exception = assertThrows(CyclicDependenciesFoundException.class, () -> config.getContext());
                List<Class<?>> classes = Arrays.stream(exception.getComponents()).toList();
                assertEquals(3, classes.size());
                assertTrue(classes.contains(Component.class));
                assertTrue(classes.contains(Dependency.class));
                assertTrue(classes.contains(AnotherDependency.class));
            }
        }

        @Nested
        public class FieldInjection {

        }

        @Nested
        public class MethodInjection {

        }

    }

    @Nested
    public class DependenciesSelection {

    }

    @Nested
    public class LifecycleManagement {

    }

}

interface Component {

}

interface Dependency {

}

interface AnotherDependency {

}

class ComponentWithDefaultConstructor implements Component {
    public ComponentWithDefaultConstructor() {

    }
}

class ComponentWithInjectConstructor implements Component {
    private Dependency dependency;

    @Inject
    public ComponentWithInjectConstructor(Dependency dependency) {
        this.dependency = dependency;
    }

    public Dependency getDependency() {
        return dependency;
    }
}

class ComponentWithInjectMultiInjectConstructor implements Component {

    @Inject
    public ComponentWithInjectMultiInjectConstructor(String name, Double value) {

    }

    @Inject
    public ComponentWithInjectMultiInjectConstructor(String name) {

    }
}

class ComponentWithNoInjectConstructorNorDefaultConstructor implements Component {
    public ComponentWithNoInjectConstructorNorDefaultConstructor(String name) {

    }
}

class DependencyWithInjectConstructor implements Dependency {
    private String Dependency;

    @Inject
    public DependencyWithInjectConstructor(String dependency) {
        Dependency = dependency;
    }

    public String getDependency() {
        return Dependency;
    }
}

class DependencyDependOnComponent implements Dependency {
    private Component component;

    @Inject
    public DependencyDependOnComponent(Component component) {
        this.component = component;
    }
}

class AnotherDependencyDependOnComponent implements AnotherDependency{
    private Component component;

    @Inject
    public AnotherDependencyDependOnComponent(Component component) {
        this.component = component;
    }
}

class DependencyDependOnAnotherDependency implements Dependency{
    private AnotherDependency anotherDependency;

    @Inject
    public DependencyDependOnAnotherDependency(AnotherDependency anotherDependency) {
        this.anotherDependency = anotherDependency;
    }
}