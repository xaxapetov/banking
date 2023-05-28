package ru.iql.banking.exeptions;

public class BankingUserNotFoundException extends RuntimeException{
    public BankingUserNotFoundException(String message){
        super(message);
    }
}
