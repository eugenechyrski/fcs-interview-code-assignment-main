package com.fulfilment.application.monolith.adapters.restapi.exceptions;

import com.fulfilment.application.monolith.warehouses.exception.BusinessLogicException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownLocationException;
import com.fulfilment.application.monolith.warehouses.exception.UnknownWarehouseException;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BusinessLogicExceptionMapper extends AbstractExceptionMapper<BusinessLogicException> {

    @Override
    protected int getCode(BusinessLogicException e) {
        if (e instanceof UnknownLocationException ||
                e instanceof UnknownWarehouseException) {
            return 404;
        }
        return 422;
    }

    @Override
    protected String getMessage(BusinessLogicException e) {
        return e.getMessage();
    }
}
