package com.tdd.args;

import java.util.List;

public interface OptionParser {
    Object parse(List<String> arguments, Option option);
}

