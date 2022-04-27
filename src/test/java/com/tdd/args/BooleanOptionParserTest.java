package com.tdd.args;

//BooleanOptionParserTest:
// sad path:
// TODO: -bool -l t / -l t f
// default:
// TODO: - bool : false

import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BooleanOptionParserTest {

    //Sad path
    @Test
    public void should_not_accept_extra_argument_for_boolean_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
                new BooleanParser().parse(Arrays.asList("-l", "t"), option("l")));
        assertEquals("l", e.getOption());
    }

    //Default value
    @Test
    public void should_set_value_to_false_if_option_not_present() {
        assertFalse(new BooleanParser().parse(Arrays.asList(), option("l")));
    }
    //Happy path
    @Test
    public void should_set_bool_true_if_flag_present() {
        assertTrue(new BooleanParser().parse(Arrays.asList("-l"), option("l")));
    }


    public static Option option(String value) {
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
