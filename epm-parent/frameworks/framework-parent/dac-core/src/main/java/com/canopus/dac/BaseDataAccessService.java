package com.canopus.dac;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public abstract class BaseDataAccessService extends BaseMiddlewareService
{
    public Response OK(final String responseIdentifier, final BaseValueObject vo) {
        final Response response = new Response();
        response.put(responseIdentifier, vo);
        return response;
    }
    
    public Response OK() {
        return new Response();
    }
    
    public Response ERROR(final Exception e) {
        final Response response = new Response();
        response.addError(e);
        return response;
    }
}
