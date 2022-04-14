package com.tdd.args;

public class TooManyArgumentsException extends Throwable{


    private String option;

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
