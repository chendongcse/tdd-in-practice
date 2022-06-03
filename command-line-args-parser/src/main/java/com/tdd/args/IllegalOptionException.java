package com.tdd.args;

public class IllegalOptionException extends RuntimeException{
    private String paramter;

    public IllegalOptionException(String option) {
        this.paramter = option;
    }

    public String getParamter() {
        return paramter;
    }

    public void setParamter(String paramter) {
        this.paramter = paramter;
    }
}
