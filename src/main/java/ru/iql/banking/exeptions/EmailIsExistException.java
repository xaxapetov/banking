package ru.iql.banking.exeptions;

public class EmailIsExistException extends RuntimeException {
    public EmailIsExistException(String message){
        super(message);
    }
}
