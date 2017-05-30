package com.kpisoft.strategy.exception;

import com.canopus.mw.dto.*;
import com.canopus.dac.*;
import com.canopus.mw.*;

public class APIUtility
{
    public static Response handleException(final Exception exception) {
        final Response response = new Response();
        if (exception instanceof MiddlewareException) {
            final MiddlewareException api = (MiddlewareException)exception;
            response.addError((Exception)api);
        }
        else if (exception instanceof DataAccessException) {
            final DataAccessException api2 = (DataAccessException)exception;
            response.addError((Exception)api2);
        }
        if (exception instanceof ValidationException) {
            final ValidationException api3 = (ValidationException)exception;
            response.addError((Exception)api3);
        }
        else {
            exception.printStackTrace();
            response.addError(exception);
        }
        return response;
    }
}
