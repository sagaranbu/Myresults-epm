package com.kpisoft.kpi.impl;

import com.kpisoft.kpi.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.kpisoft.kpi.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.kpisoft.kpi.vo.param.*;
import com.canopus.mw.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.utility.*;
import com.canopus.mw.dto.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ ScorecardCalculatorManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class DefaultScorecardCalculatorManagerServiceImpl extends BaseMiddlewareBean implements ScorecardCalculatorManagerService
{
    @Autowired
    private DefaultScorecardCalculatorManager defaultScorecardCalculatorManager;
    private static final Logger log;
    
    public Response calcScorecardRatingLevel(final Request request) {
        Double ratingLevel = null;
        final BaseValueObjectList list = (BaseValueObjectList)request.get(KpiParams.KPI_DATA_LIST.name());
        if (list == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_CALC_INVALID_INPUT_002.name(), "No data object in the request");
        }
        final List<KpiData> kpiDataList = (List<KpiData>)list.getValueObjectList();
        try {
            ratingLevel = this.defaultScorecardCalculatorManager.calcScorecardRatingLevel(kpiDataList);
            final DoubleIdentifier doubleId = new DoubleIdentifier(ratingLevel);
            return this.OK(KpiParams.SCORECARD_RATING_LEVEL.name(), (BaseValueObject)doubleId);
        }
        catch (Exception ex) {
            DefaultScorecardCalculatorManagerServiceImpl.log.error((Object)"Exception in DefaultScorecardManagerServiceImpl - calcScorecardRatingLevel() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_CALC_RATING_LEVEL_003.name(), "Failed to calculate kpi aggregate target", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response calcScorecardAcievement(final Request request) {
        Double achievement = null;
        final BaseValueObjectList list = (BaseValueObjectList)request.get(KpiParams.SCORE_UTILITY_LIST.name());
        if (list == null || list.getValueObjectList() == null) {
            throw new MiddlewareException("INVALID_SCORECARD_CALCULATOR_DATA", "No data object in the request");
        }
        final List<ScorecardCalculatorUtility> kpiDataList = (List<ScorecardCalculatorUtility>)list.getValueObjectList();
        try {
            achievement = this.defaultScorecardCalculatorManager.calcScorecardAcievement(kpiDataList);
            final DoubleIdentifier doubleId = new DoubleIdentifier(achievement);
            return this.OK(KpiParams.SCORECARD_ACHIEVEMENT.name(), (BaseValueObject)doubleId);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new MiddlewareException("Failed to calculate Achievement of group.", ex.getMessage()));
        }
    }
    
    public Response calcGroupAcievement(final Request request) {
        Double achievement = null;
        final BaseValueObjectList list = (BaseValueObjectList)request.get(KpiParams.SCORE_UTILITY_LIST.name());
        if (list == null) {
            throw new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_CALC_INVALID_INPUT_002.name(), "No data object in the request");
        }
        final List<ScorecardCalculatorUtility> kpiDataList = (List<ScorecardCalculatorUtility>)list.getValueObjectList();
        try {
            achievement = this.defaultScorecardCalculatorManager.calcGroupAcievement(kpiDataList);
            final DoubleIdentifier doubleId = new DoubleIdentifier(achievement);
            return this.OK(KpiParams.GROUP_ACHIEVEMENT.name(), (BaseValueObject)doubleId);
        }
        catch (Exception ex) {
            DefaultScorecardCalculatorManagerServiceImpl.log.error((Object)"Exception in DefaultScorecardManagerServiceImpl - calcGroupAcievement() : ", (Throwable)ex);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_SCORECARD_CALC_GROUP_ACHIEVEMENT_005.name(), "Failed to calculate Achievement of group.", new Object[] { ex.getMessage() }));
        }
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    static {
        log = Logger.getLogger((Class)DefaultScorecardCalculatorManagerServiceImpl.class);
    }
}
