package com.tdd.args;

import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    public void should_example_1(){
        Options options = Args.parse(Options.class, "-l", "-p", "8080", "-d", "/usr/logs");
        assertTrue(options.logging());
        assertEquals(8080,options.port());
        assertEquals("/usr/logs",options.directory());
    }

    static record Options(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {

    }

}
