package com.tdd.args;

//BooleanOptionParserTest:
// sad path:
// TODO: -bool -l t / -l t f
// default:
// TODO: - bool : false

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BooleanOptionParserTest {

    @Test
    public void should_not_accept_extra_argument_for_boolean_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
                new BooleanParser().parse(Arrays.asList("-l", "t"), option("l")));
        assertEquals("l", e.getOption());
    }



    private Option option(String value) {
        return new Option(){

            @Override
            public Class<? extends Annotation> annotationType() {
                return Option.class;
            }

            @Override
            public String value() {
                return value;
            }
        };

    }


}
