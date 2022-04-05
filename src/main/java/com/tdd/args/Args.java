
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
        return getOptionParser(parameter.getType()).parse(arguments, parameter.getAnnotation(Option.class));
    }

    private static Map<Class<?>, OptionParser> PARSERS = Map.of(boolean.class, new BooleanParser(),
            int.class, new IntParser(),
            String.class, new StringParser());

    private static OptionParser getOptionParser(Class<?> type) {
        return PARSERS.get(type);
    }

    interface OptionParser{
        Object parse(List<String> arguments, Option option);
    }

    static class BooleanParser implements OptionParser {
        @Override
        public Object parse(List<String> arguments, Option option) {
            return arguments.contains("-" + option.value());
        }
    }

    static class IntParser implements OptionParser{

        @Override
        public Object parse(List<String> arguments, Option option) {
            int index = arguments.indexOf("-" + option.value());
            return Integer.valueOf(arguments.get(index + 1));
        }
    }

    static class StringParser implements OptionParser{

        @Override
        public Object parse(List<String> arguments, Option option) {
            int index = arguments.indexOf("-" + option.value());
            return arguments.get(index + 1);
        }
    }
}
