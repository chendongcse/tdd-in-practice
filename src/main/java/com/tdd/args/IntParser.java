package com.tdd.args;

import java.util.List;

class IntParser implements OptionParser {

    @Override
    public Object parse(List<String> arguments, Option option) {
        int index = arguments.indexOf("-" + option.value());
        return Integer.valueOf(arguments.get(index + 1));
    }
}
