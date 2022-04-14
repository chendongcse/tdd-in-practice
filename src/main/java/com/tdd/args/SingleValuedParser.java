package com.tdd.args;

import java.util.List;
import java.util.function.Function;

class SingleValuedParser<T> implements OptionParser {
    Function<String, T> valueParser;

    SingleValuedParser(Function<String, T> valueParser) {
        this.valueParser = valueParser;
    }

    @Override
    public Object parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        String value = arguments.get(index + 1);
        return valueParser.apply(value);
    }

}