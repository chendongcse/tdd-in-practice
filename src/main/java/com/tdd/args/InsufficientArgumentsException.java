package com.tdd.args;

public class InsufficientArgumentsException extends RuntimeException{
    private String option;

    public InsufficientArgumentsException(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
