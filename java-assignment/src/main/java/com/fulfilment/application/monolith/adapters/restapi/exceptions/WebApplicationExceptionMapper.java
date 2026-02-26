package com.fulfilment.application.monolith.adapters.restapi.exceptions;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper extends AbstractExceptionMapper<WebApplicationException> {

    @Override
    protected int getCode(WebApplicationException e) {
        return e.getResponse().getStatus();
    }

    @Override
    protected String getMessage(WebApplicationException e) {
        return "";
    }
}

