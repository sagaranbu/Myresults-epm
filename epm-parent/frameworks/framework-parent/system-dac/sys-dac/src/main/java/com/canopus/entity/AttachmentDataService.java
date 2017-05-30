package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface AttachmentDataService extends DataAccessService
{
    Response getSystemAttachmentDetails(final Request p0);
    
    Response saveSystemAttachmentDetails(final Request p0);
}
