package com.tdd.args;

import java.util.List;

import static com.tdd.args.SingleValuedParser.values;

class BooleanParser implements OptionParser<Boolean> {
    @Override
    public Boolean parse(List<String> arguments, Option option) {
        return SingleValuedParser.values(arguments,option,0).map(it -> true).orElse(false);
    }
}
