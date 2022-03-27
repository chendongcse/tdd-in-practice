package com.tdd.args;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class argsTest {
    // -l -p 8080 -d /usr/logs
    // TODO
    //Single Option:
    //    -Bool
    //    -Int -p 8080
    //    -String -d /usr/logs
    //Multi Options: -l -p 8080 -d /usr/logs
    //sad path:
    // -Bool -l t / -l t f
    // -Int -p / -p 8080 8081
    // -String -d /  -d /usr/logs /usr/vars
    // default value
    // -Bool : false
    // -Int :0
    // -String: ""

    @Test
    public void should_set_bool_true_if_flag_present() {
        BooleanOption option = Args.parse(BooleanOption.class, "-l");
        assertTrue(option.logging());
    }

    @Test
    public void should_set_bool_false_if_flag_not_present() {
        BooleanOption option = Args.parse(BooleanOption.class);
        assertFalse(option.logging());
    }

    static record BooleanOption(@Option("l")boolean logging){

    }



    @Test
    @Disabled
    public void should_example_1(){
        Options options = Args.parse(Options.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging());
        assertEquals(8080,options.port());
        assertEquals("/usr/logs",options.directory());
    }

    static record Options(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {

    }

}
