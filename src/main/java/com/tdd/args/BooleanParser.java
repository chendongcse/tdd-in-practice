package com.tdd.args;

import java.util.List;

import static com.tdd.args.SingleValuedParser.values;

class BooleanParser implements OptionParser<Boolean> {
    @Override
    public Boolean parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-"+ option.value());
        List<String> values = values(arguments, index);
        if(values.size() > 0 ){
            throw new TooManyArgumentsException(option.value());
        }
        return arguments.contains("-" + option.value());
    }
}
