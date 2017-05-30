package com.canopus.mw;

import com.canopus.mw.dto.*;

public interface IRecipientResolver extends MiddlewareService
{
    public static final String INVALID_RECIPIENT_RESOLVER_INPUT_TYPE = "RES-MW-001";
    
    Response getRecipients(final Request p0);
}
