package ru.iql.banking.exeptions;

public class PhoneNotFoundException extends RuntimeException{
    public PhoneNotFoundException(String message) {
        super(message);
    }
}
