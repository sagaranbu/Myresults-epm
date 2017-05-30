package com.kpisoft.user.impl.domain.session.stats;

import com.canopus.mw.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import com.kpisoft.user.utility.*;
import com.kpisoft.user.dac.*;
import javax.validation.*;

public class SessionStatsSummary extends BaseDomainObject
{
    private SessionStatsManager manager;
    private SessionStatsSummaryData sessionStatsSummaryDetails;
    
    public SessionStatsSummary(final SessionStatsManager manager) {
        this.manager = manager;
    }
    
    public int save() {
        this.validate();
        final Request request = new Request();
        request.put(UMSParams.SESSION_STATS_SUMMARY_DATA.name(), (BaseValueObject)this.sessionStatsSummaryDetails);
        final Response response = this.getDataService().saveSessionSummary(request);
        final Identifier id = (Identifier)response.get(UMSParams.SESSION_STATS_SUMMARY_ID.name());
        this.sessionStatsSummaryDetails.setId(id.getId());
        return id.getId();
    }
    
    public void validate() {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), (Object)this.sessionStatsSummaryDetails, UserErrorCodesEnum.ERR_SESSION_INVALID_INPUT_002.name(), "Invalid Session Statistics details");
    }
    
    public SessionStatsSummaryData getSessionStatsSummaryDetails() {
        return this.sessionStatsSummaryDetails;
    }
    
    public void setsessionStatsSummaryDetails(final SessionStatsSummaryData sessionStatsSummaryDetails) {
        this.sessionStatsSummaryDetails = sessionStatsSummaryDetails;
    }
    
    private SessionStatsSummaryDataService getDataService() {
        return this.manager.getDataService();
    }
    
    private Validator getValidator() {
        return this.manager.getValidator();
    }
}
