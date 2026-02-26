package com.fulfilment.application.monolith.warehouses.adapters.restapi;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.ext.Provider;

import java.util.Objects;

@Provider
public class WarehouseResourceCreateResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext rq, ContainerResponseContext rs) {
        if (Objects.equals("POST", rq.getMethod())
                && Objects.equals(rs.getStatus(), 200)
                && Objects.equals("/warehouse", rq.getUriInfo().getPath())

        ) {
            rs.setStatus(201);
        }
    }
}

