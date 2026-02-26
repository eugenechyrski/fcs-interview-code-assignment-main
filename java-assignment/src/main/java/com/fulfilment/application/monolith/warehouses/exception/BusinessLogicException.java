package com.fulfilment.application.monolith.warehouses.exception;

public class BusinessLogicException extends RuntimeException {

    public BusinessLogicException() {

    }

    public BusinessLogicException(String message) {
        super(message);
    }

}
