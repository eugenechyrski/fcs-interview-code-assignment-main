package com.fulfilment.application.monolith.adapters.restapi.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fulfilment.application.monolith.products.ProductResource;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import org.jboss.logging.Logger;

public abstract class AbstractExceptionMapper<T extends Throwable> implements ExceptionMapper<T> {
    private static final Logger LOGGER = Logger.getLogger(ProductResource.class.getName());

    @Inject
    ObjectMapper objectMapper;

    @Override
    public Response toResponse(T exception) {
        LOGGER.error("Failed to handle request", exception);
        int code = getCode(exception);
        String message = getMessage(exception);
        ObjectNode exceptionJson = objectMapper.createObjectNode();
        exceptionJson.put("exceptionType", exception.getClass().getName());
        exceptionJson.put("code", code);

        if (message != null) {
            exceptionJson.put("error", exception.getMessage());
        }

        return Response.status(code).entity(exceptionJson).build();
    }

    protected abstract int getCode(T e);

    protected abstract String getMessage(T e);
}
