package com.kpisoft.kpi.impl;

import com.kpisoft.kpi.*;
import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.kpisoft.kpi.domain.*;
import org.springframework.beans.factory.annotation.*;
import org.apache.log4j.*;
import com.canopus.mw.*;
import com.canopus.mw.dto.*;
import java.util.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.utility.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ KpiCalculatorManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class DefaultKpiCalculatorManagerServiceImpl extends BaseMiddlewareBean implements KpiCalculatorManagerService
{
    @Autowired
    private DefaultKpiCalculatorManager defaultKpiCalculatorManager;
    private static final Logger log;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier si = new StringIdentifier();
        si.setId(this.getClass().getSimpleName());
        return si;
    }
    
    public Response calcKpiRatingLevel(final Request request) {
        Integer ratingLevel = null;
        final StringIdentifier kpiType = (StringIdentifier)request.get(KpiParams.KPI_TYPE_NAME.name());
        final StringIdentifier achievementType = (StringIdentifier)request.get(KpiParams.KPI_ACIEVEMENT_TYPE.name());
        final KpiScaleBean kpiScaleBean = (KpiScaleBean)request.get(KpiParams.KPI_TARGET_SCALE.name());
        final DateResponse dateScoreActual = (DateResponse)request.get(KpiParams.KPI_DATE_SCORE_ACTUAL.name());
        final DoubleIdentifier scoreActual = (DoubleIdentifier)request.get(KpiParams.KPI_SCORE_ACTUAL.name());
        if ((scoreActual == null && dateScoreActual == null) || kpiScaleBean == null || kpiType == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            double scoreActualD = 0.0;
            if (scoreActual != null && scoreActual.getId() != null) {
                scoreActualD = scoreActual.getId();
            }
            Date dateScoreActualD = null;
            if (dateScoreActual != null) {
                dateScoreActualD = dateScoreActual.getDate();
            }
            ratingLevel = this.defaultKpiCalculatorManager.calcKpiRatingLevel(achievementType.getId(), kpiType.getId(), scoreActualD, kpiScaleBean, dateScoreActualD);
            final Identifier ratingLevelId = new Identifier(ratingLevel);
            return this.OK(KpiParams.KPI_RATING_LEVEL.name(), (BaseValueObject)ratingLevelId);
        }
        catch (Exception ex) {
            DefaultKpiCalculatorManagerServiceImpl.log.error((Object)("Exception in DefaultKpiCalculatorManagerServiceImpl - calcKpiRatingLevel() : " + ex.getMessage()));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_RATING_LEVEL_003.name(), "Failed to calculate kpi rating level", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response calcKpiAcievement(final Request request) {
        Double achievement = null;
        final StringIdentifier id = (StringIdentifier)request.get(KpiParams.KPI_ACIEVEMENT_TYPE.name());
        final KpiScoreBean kpiScoreBean = (KpiScoreBean)request.get(KpiParams.KPI_SCORE_DATA.name());
        if (kpiScoreBean == null || id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            achievement = this.defaultKpiCalculatorManager.calcKpiAcievement(kpiScoreBean, id.getId());
            final DoubleIdentifier doubleId = new DoubleIdentifier(achievement);
            return this.OK(KpiParams.KPI_ACHIEVEMENT.name(), (BaseValueObject)doubleId);
        }
        catch (Exception ex) {
            DefaultKpiCalculatorManagerServiceImpl.log.error((Object)("Exception in DefaultKpiCalculatorManagerServiceImpl - calcKpiAcievement() : " + ex.getMessage()));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_ACHIEVEMENT_004.name(), "Failed to calculate kpi achievement", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response calcKpiAggregatedTarget(final Request request) {
        Double target = null;
        final StringIdentifier id = (StringIdentifier)request.get(KpiParams.KPI_AGGREGATION_TYPE.name());
        final BaseValueObjectList list = (BaseValueObjectList)request.get(KpiParams.KPI_TARGET_DATA_LIST.name());
        if (list == null || id == null) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final List<KpiTargetDataBean> kpiTargetDataBeanList = (List<KpiTargetDataBean>)list.getValueObjectList();
        try {
            target = this.defaultKpiCalculatorManager.calcKpiAggregatedTarget(id.getId(), kpiTargetDataBeanList);
            final DoubleIdentifier doubleId = new DoubleIdentifier(target);
            return this.OK(KpiParams.KPI_TARGET.name(), (BaseValueObject)doubleId);
        }
        catch (Exception ex) {
            DefaultKpiCalculatorManagerServiceImpl.log.error((Object)("Exception in DefaultKpiCalculatorManagerServiceImpl - calcKpiAggregatedTarget() : " + ex.getMessage()));
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_AGGR_TARGET_005.name(), "Failed to calculate kpi aggregate target", new Object[] { ex.getMessage() }));
        }
    }
    
    public Response calculateKpiPerformancePoints(final Request request) {
        StringIdentifier achievementType = (StringIdentifier)request.get(KpiParams.KPI_ACIEVEMENT_TYPE.name());
        StringIdentifier kpiType = (StringIdentifier)request.get(KpiParams.KPI_TYPE_NAME.name());
        final DoubleIdentifier scoreActual = (DoubleIdentifier)request.get(KpiParams.KPI_SCORE_ACTUAL.name());
        final DoubleIdentifier target = (DoubleIdentifier)request.get(KpiParams.KPI_TARGET.name());
        final DoubleIdentifier targetCumulative = (DoubleIdentifier)request.get(KpiParams.KPI_TARGET_CUMULATIVE.name());
        final KpiScaleBean kpiScaleBean = (KpiScaleBean)request.get(KpiParams.KPI_TARGET_SCALE.name());
        final DateResponse dateTarget = (DateResponse)request.get(KpiParams.KPI_DATE_TARGET.name());
        final DateResponse dateScoreActual = (DateResponse)request.get(KpiParams.KPI_DATE_SCORE_ACTUAL.name());
        double targetD = 0.0;
        double targetCumulativeD = 0.0;
        double scoreActualD = 0.0;
        Date dateTargetD = null;
        Date dateScoreActualD = null;
        if (kpiScaleBean == null || kpiScaleBean.getAlkpiMasterScaleValue() == null || kpiScaleBean.getAlkpiMasterScaleValue().isEmpty()) {
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        if (achievementType == null || achievementType.getId() == null || achievementType.getId().trim().isEmpty()) {
            achievementType = new StringIdentifier(AchivementTypeEnum.MAX_IS_HIGHER.name());
        }
        if (kpiType == null || kpiType.getId() == null || kpiType.getId().trim().isEmpty()) {
            kpiType = new StringIdentifier(KpiValidationParams.KpiType.MESUREMENT.name());
        }
        if (kpiType.getId().equalsIgnoreCase(KpiValidationParams.KpiType.MESUREMENT.name())) {
            if (scoreActual == null || scoreActual.getId() == null || target == null || target.getId() == null || targetCumulative == null || targetCumulative.getId() == null) {
                return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_INVALID_INPUT_002.name(), "Invalid data in request"));
            }
            targetD = target.getId();
            targetCumulativeD = targetCumulative.getId();
            scoreActualD = scoreActual.getId();
        }
        else {
            if (dateTarget == null || dateScoreActual == null) {
                return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_INVALID_INPUT_002.name(), "Invalid data in request"));
            }
            if (dateTarget != null) {
                dateTargetD = dateTarget.getDate();
            }
            if (dateScoreActual != null) {
                dateScoreActualD = dateScoreActual.getDate();
            }
        }
        try {
            final KpiCalcUtility utility = this.defaultKpiCalculatorManager.calculateKpiPerformancePoints(achievementType.getId(), kpiType.getId(), scoreActualD, kpiScaleBean, targetD, targetCumulativeD, dateScoreActualD, dateTargetD);
            return this.OK(KpiParams.KPI_CALC_UTILITY.name(), (BaseValueObject)utility);
        }
        catch (Exception e) {
            DefaultKpiCalculatorManagerServiceImpl.log.error((Object)"Exception in DefaultKpiCalculatorManagerServiceImpl - calculateKpiPerformancePoints()", (Throwable)e);
            return this.ERROR((Exception)new MiddlewareException(KpiErrorCodesEnum.ERR_KPI_CALC_PERFORMANCE_006.name(), "Failed to calculate kpi performance points", (Throwable)e));
        }
    }
    
    static {
        log = Logger.getLogger((Class)DefaultKpiCalculatorManagerServiceImpl.class);
    }
}
