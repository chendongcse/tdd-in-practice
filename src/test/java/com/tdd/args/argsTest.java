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
    //Multi MultiOptions: -l -p 8080 -d /usr/logs
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
    public void should_parse_int_to_option_value(){
        IntOption option = Args.parse(IntOption.class, "-p", "8080");
        assertEquals(8080, option.port());
    }

    static record IntOption(@Option("p")int port){

    }

    @Test
    public void should_parse_string_to_option_value(){
        StringOption option = Args.parse(StringOption.class, "-d", "/usr/logs");
        assertEquals("/usr/logs", option.directory());
    }

    static record StringOption(@Option("d")String directory){

    }


    @Test
    public void should_parse_multi_options(){
        MultiOptions options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging());
        assertEquals(8080,options.port());
        assertEquals("/usr/logs",options.directory());
    }

    static record MultiOptions(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {

    }

}
