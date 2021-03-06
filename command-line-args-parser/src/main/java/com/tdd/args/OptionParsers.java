package com.tdd.args;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.stream.IntStream;

public class OptionParsers {

    public static OptionParser<Boolean> bool() {
        return ((arguments, option) -> values(arguments, option, 0).map(it -> true).orElse(false));
    }

    public static <T> OptionParser<T> unary(T defaultValue, Function<String, T> valueParser) {
        return (arguments, option) -> values(arguments, option, 1).map(it -> parse(it.get(0), valueParser, option)).orElse(defaultValue);
    }

    public static <T> OptionParser<T[]> list(T[] defaultValue, IntFunction<T[]> generator, Function<String, T> valueParser) {
        return (arguments, option) ->
                values(arguments, option)
                        .map(it -> it.stream().map(value -> parse(value, valueParser, option)).toArray(generator))
                        .orElse(defaultValue);
    }

    private static Optional<List<String>> values(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        return Optional.ofNullable(index == -1 ? null : values(arguments, index));
    }

    private static Optional<List<String>> values(List<String> arguments, Option option, int expectedSize) {
        return values(arguments, option).map(it -> checkSize(option, expectedSize, it));
    }

    private static List<String> checkSize(Option option, int expectedSize, List<String> values) {
        if (values.size() < expectedSize) {
            throw new InsufficientArgumentsException(option.value());
        }
        if (values.size() > expectedSize) {
            throw new TooManyArgumentsException(option.value());
        }
        return values;
    }

    private static <T> T parse(String value, Function<String, T> valueParser, Option option) {
        try {
            return valueParser.apply(value);
        } catch (Exception e) {
            throw new IllegalValueException(option.value(), value);
        }
    }

    private static List<String> values(List<String> arguments, int index) {
        int followingFlag = IntStream.range(index + 1, arguments.size()).filter(it -> arguments.get(it).matches("^-[a-zA-Z]+$")).findFirst().orElse(arguments.size());
        List<String> values = arguments.subList(index + 1, followingFlag);
        return values;
    }

}
