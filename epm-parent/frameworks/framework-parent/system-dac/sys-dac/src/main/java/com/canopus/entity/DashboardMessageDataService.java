package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface DashboardMessageDataService extends DataAccessService
{
    public static final String ERR_DASHBOARDMSG_INVALID_DATA = "err-invalid-data";
    public static final String ERR_DASHBOARD_MESSAGE_UNKNOWN_EXCEPTION = "err-unknown-exp";
    
    Response saveDashboardMessage(final Request p0);
    
    Response getDashboardMessages(final Request p0);
    
    Response cancelUserDashboardMessage(final Request p0);
    
    Response getAllDashboardMessages(final Request p0);
    
    Response getAllDashboardUserMessages(final Request p0);
    
    Response getDashboardMessage(final Request p0);
    
    Response searchDUMByDId(final Request p0);
    
    Response getDashboardMessageByIds(final Request p0);
}
