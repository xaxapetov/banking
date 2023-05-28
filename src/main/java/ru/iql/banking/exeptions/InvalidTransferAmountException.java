package ru.iql.banking.exeptions;

public class InvalidTransferAmountException extends RuntimeException {
    public InvalidTransferAmountException(String message){
        super(message);
    }
}
