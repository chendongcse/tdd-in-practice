package com.tdd.args;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ArgsTest {

    @Test
    public void should_parse_multi_options() {
        MultiOptions options = Args.parse(MultiOptions.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging());
        assertEquals(8080, options.port());
        assertEquals("/usr/logs", options.directory());
    }

    static record MultiOptions(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {

    }

    @Test
    public void should_throw_illegal_exception_if_annotation_not_present() {
        IllegalOptionException e = assertThrows(IllegalOptionException.class, () -> Args.parse(OptionWithoutAnnotation.class, "-l", "-p", "8080", "-d", "/usr/logs"));
        assertEquals("port", e.getParamter());
    }

    static record OptionWithoutAnnotation(@Option("l") boolean logging, int port, @Option("d") String directory) {

    }

    @Test
    public void should_parse_list_value() {
        ListOption options = Args.parse(ListOption.class, "-g", "this", "is", "a", "list", "-d", "1", "2", "-3", "4");
        assertArrayEquals(new String[]{"this", "is", "a", "list"}, options.group);
        assertArrayEquals(new Integer[]{1, 2, -3, 4}, options.decimals);
    }

    static record ListOption(@Option("g") String[] group, @Option("d") Integer[] decimals) {
    }

}
