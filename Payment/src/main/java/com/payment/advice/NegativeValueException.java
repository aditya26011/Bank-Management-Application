package com.payment.advice;

public class NegativeValueException extends RuntimeException{
    public NegativeValueException(String message){
        super(message);

    }
}
