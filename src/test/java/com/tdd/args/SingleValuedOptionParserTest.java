package com.tdd.args;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;

import static com.tdd.args.BooleanOptionParserTest.option;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//SingleValuedOptionParserTest:
// sad path:
// TODO: - int -p/ -p 8080 8081
// TODO: - string -d/ -d /usr/logs /usr/vars
// default value:
// TODO: -int :0
// TODO: - string ""
public class SingleValuedOptionParserTest {

    @Test
    public void should_not_accept_extra_argument_for_single_valued_option(){
        assertThrows(TooManyArgumentsException.class, () -> new SingleValuedParser<Integer>(0, Integer::parseInt).parse(Arrays.asList("-p","8080","8081"),option("p")));
    }


    @ParameterizedTest
    @ValueSource(strings = {"-p -t","-p"})
    public void should_not_accept_insufficient_argument_for_single_valued_option(String argument){
        InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> new SingleValuedParser<Integer>(0, Integer::parseInt).parse(Arrays.asList(argument.split(" ")), option("p")));
        assertEquals("p",e.getOption());
    }

    @Test
    public void should_set_default_value_to_0_for_int_option(){
        assertEquals(0, new SingleValuedParser<Integer>(0, Integer::parseInt).parse(Arrays.asList(),option("p")));
    }
}
