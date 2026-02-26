package com.fulfilment.application.monolith.adapters.restapi.exceptions;

import jakarta.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper extends AbstractExceptionMapper<Exception> {

    @Override
    protected int getCode(Exception e) {
        return 500;
    }

    @Override
    protected String getMessage(Exception e) {
        return "";
    }
}

