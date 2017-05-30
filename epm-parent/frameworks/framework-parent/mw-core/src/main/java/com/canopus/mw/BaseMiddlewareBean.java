package com.canopus.mw;

import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.dto.*;

public class BaseMiddlewareBean
{
    private MainInterceptor mainInterceptor;
    
    @Autowired
    public void setMainInterceptor(final MainInterceptor mainInterceptor) {
        this.mainInterceptor = mainInterceptor;
    }
    
    public MainInterceptor getMainInterceptor() {
        return this.mainInterceptor;
    }
    
    public Response OK(final String responseIdentifier, final BaseValueObject vo) {
        final Response response = ExecutionContext.getCurrent().getResponse();
        response.put(responseIdentifier, vo);
        return response;
    }
    
    public Response OK() {
        final Response response = ExecutionContext.getCurrent().getResponse();
        return response;
    }
    
    public Response ERROR(final Exception e) {
        final Response response = ExecutionContext.getCurrent().getResponse();
        response.addError(e);
        return response;
    }
}
