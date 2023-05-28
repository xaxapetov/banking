package ru.iql.banking.exeptions;

public class NoArgumentsSpecified extends RuntimeException {
    public NoArgumentsSpecified(String message){
        super(message);
    }
}
