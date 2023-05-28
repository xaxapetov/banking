package ru.iql.banking.exeptions;

public class OnlyOneInstanceOfData extends RuntimeException{
    public OnlyOneInstanceOfData(String message){
        super(message);
    }
}
