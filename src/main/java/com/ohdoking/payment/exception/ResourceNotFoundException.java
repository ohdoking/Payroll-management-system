package com.ohdoking.payment.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String errMessage) {
        super(errMessage);
    }
}
