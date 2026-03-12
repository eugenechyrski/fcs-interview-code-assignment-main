package com.fulfilment.application.monolith.fulfillment.adapters.restapi;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.util.Objects;

@Provider
public class WarehouseFulfilmentResourceResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext rq, ContainerResponseContext rs) {
        if (Objects.equals("POST", rq.getMethod())
                && rs.getStatus() < 300
                && Objects.equals("/fulfilment", rq.getUriInfo().getPath())

        ) {
            rs.setStatus(201);
        }
    }
}

