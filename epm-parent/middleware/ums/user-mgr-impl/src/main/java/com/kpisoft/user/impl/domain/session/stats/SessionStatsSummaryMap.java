package com.kpisoft.user.impl.domain.session.stats;

import java.util.*;
import com.kpisoft.user.vo.*;

public class SessionStatsSummaryMap
{
    private Map<Integer, SessionStatsSummaryData> sessionStatsSummaryDetails;
    
    public Map<Integer, SessionStatsSummaryData> getSessionStatsSummaryDetails() {
        return this.sessionStatsSummaryDetails;
    }
    
    public void setSessionStatsSummaryDetails(final Map<Integer, SessionStatsSummaryData> sessionStatsSummaryDetails) {
        this.sessionStatsSummaryDetails = sessionStatsSummaryDetails;
    }
}
