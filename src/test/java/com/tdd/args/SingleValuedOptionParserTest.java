package com.tdd.args;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static com.tdd.args.BooleanOptionParserTest.option;
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
        assertThrows(TooManyArgumentsException.class,() -> new SingleValuedParser<>(Integer::parseInt).parse(Arrays.asList("-p","8080","8081"),option("p")));
    }
}
