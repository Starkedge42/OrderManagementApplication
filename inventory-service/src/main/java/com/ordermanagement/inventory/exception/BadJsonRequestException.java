package com.ordermanagement.inventory.exception;

public class BadJsonRequestException extends RuntimeException{

    public BadJsonRequestException(String message){
        super(message);
    }
}
