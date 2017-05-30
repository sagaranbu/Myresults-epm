package com.kpisoft.user.impl;

import com.canopus.mw.*;
import com.kpisoft.user.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.vo.param.*;
import java.util.*;
import java.text.*;
import com.canopus.mw.dto.*;
import com.kpisoft.user.impl.domain.session.stats.*;
import com.kpisoft.user.vo.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ SessionStatisticsManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class SessionStatisticsManagerServiceImpl extends BaseMiddlewareBean implements SessionStatisticsManagerService
{
    @Autowired
    private SessionStatsManager statsManager;
    @Autowired
    private String aggrPeriodType;
    
    public SessionStatisticsManagerServiceImpl() {
        this.statsManager = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response getSessionSummary(final Request request) {
        final Identifier id = (Identifier)request.get(UMSParams.SESSION_STATS_AGGR_PERIOD.name());
        Integer aggrPeriod = null;
        if (id == null || id.getId() == null) {
            final Date now = new Date();
            final String format = null;
            if (this.getAggrPeriodType().equals(UMSParams.SESSION_STATS_AGGR_PERIOD_DIALY.name())) {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                aggrPeriod = new Integer(dateFormat.format(now));
            }
            else {
                final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
                aggrPeriod = new Integer(dateFormat.format(now));
            }
            final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
            aggrPeriod = new Integer(dateFormat.format(now));
        }
        else {
            aggrPeriod = id.getId();
        }
        final SessionStatsSummary summary = this.getStatsManager().getSessioStatsSummary(aggrPeriod);
        final SessionStatsSummaryData summaryData = summary.getSessionStatsSummaryDetails();
        return this.OK(UMSParams.SESSION_STATS_SUMMARY_DATA.name(), (BaseValueObject)summaryData);
    }
    
    public Response getUserLastSessionDetails(final Request request) {
        return null;
    }
    
    public SessionStatsManager getStatsManager() {
        return this.statsManager;
    }
    
    public void setStatsManager(final SessionStatsManager statsManager) {
        this.statsManager = statsManager;
    }
    
    public String getAggrPeriodType() {
        return this.aggrPeriodType;
    }
    
    public void setAggrPeriodType(final String aggrPeriodType) {
        this.aggrPeriodType = aggrPeriodType;
    }
}
