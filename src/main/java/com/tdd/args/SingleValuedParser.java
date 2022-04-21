package com.tdd.args;

import java.util.List;
import java.util.function.Function;

class SingleValuedParser<T> implements OptionParser<T> {
    Function<String, T> valueParser;

    SingleValuedParser(Function<String, T> valueParser) {
        this.valueParser = valueParser;
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        if(index+1 == arguments.size() || ( index+1 < arguments.size() &&  arguments.get(index+1).startsWith("-"))){
            throw new InsufficientArgumentsException(option.value());
        }
        if(index+2 < arguments.size() && !arguments.get(index+2).startsWith("-")){
            throw new TooManyArgumentsException(option.value());
        }
        String value = arguments.get(index + 1);
        return valueParser.apply(value);
    }

}
