package com.canopus.entity;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface AttachmentManagerService extends MiddlewareService
{
    Response getSystemAttachmentDetails(final Request p0);
    
    Response saveSystemAttachmentDetails(final Request p0);
}
