package com.tdd.args;

import org.junit.jupiter.api.Test;

public class argsTest {
    // -l -p 8080 -d /usr/logs
    @Test
    public void should() {
        Arguments args = Args.parse("l:b,p:d,d:s", "-l", "-p", "8080", "-d", "/usr/logs");
        args.getBool("l");
        args.getInt("p");

        Options options = Args.parse(Options.class, "-l", "-p", "8080", "-d", "/usr/logs");
        options.logging();
        options.port();
        options.directory();

    }

    static record Options(@Option("l") boolean logging, @Option("p") int port, @Option("d") String directory) {

    }

}
