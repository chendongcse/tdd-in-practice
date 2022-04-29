package com.tdd.args;

import java.util.List;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SingleValuedParser<T> implements OptionParser<T> {
    Function<String, T> valueParser;

    T defaultValue;

    public SingleValuedParser(T defaultValue, Function<String, T> valueParser) {
        this.defaultValue = defaultValue;
        this.valueParser = valueParser;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        if(index == -1){
            return defaultValue;
        }
        List<String> values = values(arguments, index);
        if(values.size() < 1 ){
            throw new InsufficientArgumentsException(option.value());
        }
        if(values.size() > 1 ){
            throw new TooManyArgumentsException(option.value());
        }
        String value = values.get(0);
        return valueParser.apply(value);
    }

    static List<String> values(List<String> arguments, int index) {
        int followingFlag = IntStream.range(index + 1, arguments.size()).filter(it -> arguments.get(it).startsWith("-")).findFirst().orElse(arguments.size());
        List<String> values = arguments.subList(index + 1, followingFlag);
        return values;
    }

}
