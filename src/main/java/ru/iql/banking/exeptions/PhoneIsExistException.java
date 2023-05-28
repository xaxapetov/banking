package ru.iql.banking.exeptions;

public class PhoneIsExistException extends RuntimeException{
    public PhoneIsExistException(String message){
        super(message);
    }
}
