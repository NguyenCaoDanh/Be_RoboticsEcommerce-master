package com.wisdom.roboticsecommerce.ExceptionHandler;

public class LogOnlyException extends RuntimeException{
    public LogOnlyException(String message) {
        super(message);
    }
}
