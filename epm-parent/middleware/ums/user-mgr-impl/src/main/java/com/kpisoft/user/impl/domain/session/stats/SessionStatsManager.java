package com.kpisoft.user.impl.domain.session.stats;

import com.kpisoft.user.dac.*;
import javax.validation.*;
import com.canopus.mw.cache.*;
import org.springframework.beans.factory.annotation.*;
import java.util.*;
import com.kpisoft.user.vo.param.*;
import java.text.*;
import com.kpisoft.user.vo.*;
import com.canopus.mw.dto.*;

public class SessionStatsManager implements CacheLoader<Integer, SessionStatsSummary>
{
    @Autowired
    private SessionStatsSummaryDataService dataService;
    @Autowired
    private Validator validator;
    @Autowired
    @Qualifier("sessionStatsCache")
    private Cache<Integer, SessionStatsSummary> multiTenantCache;
    private String aggrPeriodType;
    
    public SessionStatsManager() {
        this.dataService = null;
        this.multiTenantCache = null;
    }
    
    public SessionStatsSummary getSessioStatsSummary(final Integer Identifier) {
        final SessionStatsSummary sessioStatsSummary = (SessionStatsSummary)this.multiTenantCache.get(Identifier, (CacheLoader)this);
        return sessioStatsSummary;
    }
    
    public SessionStatsSummary getCurrentSessioStatsSummary() {
        final Date now = new Date();
        Integer aggrPeriod;
        if (this.getAggrPeriodType().equals(UMSParams.SESSION_STATS_AGGR_PERIOD_DIALY.name())) {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            aggrPeriod = new Integer(dateFormat.format(now));
        }
        else {
            final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMM");
            aggrPeriod = new Integer(dateFormat.format(now));
        }
        final SessionStatsSummary sessioStatsSummary = (SessionStatsSummary)this.multiTenantCache.get(aggrPeriod, (CacheLoader)this);
        return sessioStatsSummary;
    }
    
    public SessionStatsSummary load(final Integer key) {
        final SessionStatsSummaryDataService svc = this.getDataService();
        final Request request = new Request();
        request.put(UMSParams.SESSION_STATS_AGGR_PERIOD.name(), (BaseValueObject)new Identifier(key));
        request.put(UMSParams.USER_TYPE.name(), (BaseValueObject)new Identifier(1));
        final Response response = svc.getSessionSummary(request);
        final SessionStatsSummaryData sessionData = (SessionStatsSummaryData)response.get(UMSParams.SESSION_STATS_SUMMARY_DATA.name());
        final SessionStatsSummary summary = new SessionStatsSummary(this);
        summary.setsessionStatsSummaryDetails(sessionData);
        this.multiTenantCache.put(key, summary);
        return summary;
    }
    
    public void updateStatsSummaryInCache(final SessionStatsSummary summary) {
        this.multiTenantCache.put(summary.getSessionStatsSummaryDetails().getAgrPeriod(), summary);
    }
    
    public SessionStatsSummaryDataService getDataService() {
        return this.dataService;
    }
    
    public Validator getValidator() {
        return this.validator;
    }
    
    public String getAggrPeriodType() {
        return this.aggrPeriodType;
    }
    
    public void setAggrPeriodType(final String aggrPeriodType) {
        this.aggrPeriodType = aggrPeriodType;
    }
    
    public Cache<Integer, SessionStatsSummary> getMultiTenantCache() {
        return this.multiTenantCache;
    }
}
