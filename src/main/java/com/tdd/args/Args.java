
package com.tdd.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Args {
    public static <T> T parse(Class<T> optionsClass, String... args) {
        Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
        List<String> arguments = Arrays.asList(args);
        Object[] values = Arrays.stream(constructor.getParameters()).map(it -> parseOption(it, arguments)).toArray();

        try {
            return (T) constructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private static Object parseOption(Parameter parameter, List<String> arguments) {
        if (!parameter.isAnnotationPresent(Option.class))
            throw new IllegalOptionException(parameter.getName());
        return PARSERS.get(parameter.getType()).parse(arguments, parameter.getAnnotation(Option.class));
    }

    private static Map<Class<?>, OptionParser> PARSERS = Map.of(boolean.class, OptionParsers.bool(),
            int.class, OptionParsers.unary(0, Integer::valueOf),
            String.class, OptionParsers.unary("", String::valueOf),
            String[].class, OptionParsers.list(new String[]{}, String[]::new, String::valueOf),
            Integer[].class, OptionParsers.list(new Integer[]{}, Integer[]::new, Integer::parseInt)
    );

}
