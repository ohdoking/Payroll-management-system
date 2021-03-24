package com.ohdoking.payment.exception;

public class IncorrectPaymentTypeEmployeeException extends RuntimeException {
    public IncorrectPaymentTypeEmployeeException(String errMsg) {
        super(errMsg);
    }
}
