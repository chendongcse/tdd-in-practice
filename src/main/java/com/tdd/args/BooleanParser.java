package com.tdd.args;

import java.util.List;

class BooleanParser implements OptionParser {
    @Override
    public Object parse(List<String> arguments, Option option) {
        return arguments.contains("-" + option.value());
    }
}
