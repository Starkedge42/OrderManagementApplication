package com.ordermanagement.apigateway.exception;

public class HeaderDoesNotContainJwtTokenException extends RuntimeException{

    public HeaderDoesNotContainJwtTokenException(String message){
        super(message);
    }
}
