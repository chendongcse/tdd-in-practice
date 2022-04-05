
package com.tdd.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

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
        return getOptionParser(parameter).parse(arguments, parameter.getAnnotation(Option.class));
    }

    private static OptionParser getOptionParser(Parameter parameter) {
        OptionParser parser = null;
        Class<?> type = parameter.getType();
        if (type == boolean.class) {
            parser = new BooleanParser();
        }

        if (type == int.class) {
            parser = new IntParser();
        }

        if (type == String.class) {
            parser = new StringParser();
        }
        return parser;
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
