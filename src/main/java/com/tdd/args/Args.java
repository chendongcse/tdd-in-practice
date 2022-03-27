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

        Object value = null;

        if(parameter.getType() == boolean.class){
            value = arguments.contains("-" + option.value());
        }

        if(parameter.getType() == int.class){
            value = Integer.valueOf(args[1]);
        }

        if(parameter.getType() == String.class){
            value = args[1];
        }

        try {
            return (T) constructor.newInstance(value);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
