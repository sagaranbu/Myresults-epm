package com.canopus.event.mgr;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface INotificationService extends MiddlewareService
{
    Response getDashboardMessages(final Request p0);
    
    Response cancelUserDashboardMessage(final Request p0);
}
