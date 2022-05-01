package com.tdd.args;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class OptionParsersTest {

    @Nested
    class UnaryOptionParser {
        //Sad path
        @Test
        public void should_not_accept_extra_argument_for_single_valued_option() {
            assertThrows(TooManyArgumentsException.class, () -> OptionParsers.unary(0, Integer::parseInt).parse(Arrays.asList("-p", "8080", "8081"), option("p")));
        }

        //Sad path
        @ParameterizedTest
        @ValueSource(strings = {"-p -t", "-p"})
        public void should_not_accept_insufficient_argument_for_single_valued_option(String argument) {
            InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> OptionParsers.unary(0, Integer::parseInt).parse(Arrays.asList(argument.split(" ")), option("p")));
            assertEquals("p", e.getOption());
        }

        //Default value
        @Test
        public void should_set_default_value_to_0_for_int_option() {
            Function<String, Object> whatever = (it) -> null;
            Object defaultValue = new Object();
            assertEquals(defaultValue, OptionParsers.unary(defaultValue, whatever).parse(Arrays.asList(), option("p")));
        }

        @Test
        //Happy path
        public void should_parse_string_to_option_value() {
            Object parsed = new Object();
            Function<String, Object> parse = (it) -> parsed;
            Object whatever = new Object();
            assertEquals(parsed, OptionParsers.unary(whatever, parse).parse(Arrays.asList("-p", "8080"), option("p")));
        }
    }

    @Nested
    class BooleanOptionParserTest {

        //Sad path
        @Test
        public void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () ->
                    OptionParsers.bool().parse(Arrays.asList("-l", "t"), option("l")));
            assertEquals("l", e.getOption());
        }

        //Default value
        @Test
        public void should_set_value_to_false_if_option_not_present() {
            assertFalse(OptionParsers.bool().parse(Arrays.asList(), option("l")));
        }
        //Happy path
        @Test
        public void should_set_bool_true_if_flag_present() {
            assertTrue(OptionParsers.bool().parse(Arrays.asList("-l"), option("l")));
        }
    }

    @Nested
    class ListOptionParserTest{
        //TODO: -g "this" "is" {"this", is"}
        @Test
        public void should_parse_list_value() {
            assertArrayEquals(new String[]{"this", "is"}, OptionParsers.list(new String[]{}, String[]::new, String::valueOf).parse(Arrays.asList("-g", "this", "is"), option("g")));
        }
        //TODO: default value []
        @Test
        public void should_use_empty_array_as_default_value() {
            assertArrayEquals(new String[]{}, OptionParsers.list(new String[]{}, String[]::new, String::valueOf).parse(Arrays.asList(), option("g")));
        }
        //TODO: -d a throw exception
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
