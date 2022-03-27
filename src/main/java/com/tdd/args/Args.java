package com.tdd.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class Args {
    public static <T> T parse(Class<T> optionsClass, String... args) {
        Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];
        Parameter parameter = constructor.getParameters()[0];
        Option option = parameter.getAnnotation(Option.class);
        List<String> arguments = Arrays.asList(args);

        try {
            return (T) constructor.newInstance(arguments.contains("-" + option.value()));
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
