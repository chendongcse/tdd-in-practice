package com.tdd.args;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

public class OptionParsers {

    public static OptionParser<Boolean> bool() {
        return ((arguments, option) -> values(arguments,option,0).map(it -> true).orElse(false));
    }

    public static <T> OptionParser<T> unary(T defaultValue, Function<String, T> valueParser) {
        return (arguments, option) -> values(arguments, option, 1).map(it -> parse(it.get(0), valueParser)).orElse(defaultValue);
    }

    private static  Optional<List<String>> values(List<String> arguments, Option option, int expectedSize) {
        int index = arguments.indexOf("-" + option.value());
        if (index == -1) {
            return Optional.empty();
        }
        List<String> values = values(arguments, index);

        if (values.size() < expectedSize) {
            throw new InsufficientArgumentsException(option.value());
        }
        if (values.size() > expectedSize) {
            throw new TooManyArgumentsException(option.value());
        }
        return Optional.of(values);

    }

    private static <T> T parse(String value, Function<String, T> valueParser) {
        return valueParser.apply(value);
    }

    private static List<String> values(List<String> arguments, int index) {
        int followingFlag = IntStream.range(index + 1, arguments.size()).filter(it -> arguments.get(it).startsWith("-")).findFirst().orElse(arguments.size());
        List<String> values = arguments.subList(index + 1, followingFlag);
        return values;
    }

}
