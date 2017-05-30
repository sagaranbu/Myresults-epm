package com.kpisoft.kpi.helper;

import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.vo.*;
import java.util.*;

public class KpiScoreHelper
{
    private KpiData kpi;
    private boolean isAggregate;
    private boolean isMinHigher;
    private boolean isMeasure;
    private List<PeriodMasterBean> periods;
    
    public KpiScoreHelper(final KpiData kpi, final List<PeriodMasterBean> periods) {
        this.isAggregate = false;
        this.isMinHigher = false;
        this.isMeasure = true;
        if (kpi == null) {
            throw new IllegalArgumentException("KPI can not be null");
        }
        if (periods == null) {
            throw new IllegalArgumentException("Periods can not be null");
        }
        this.kpi = kpi;
        this.periods = periods;
        if (kpi.getKpiTarget() != null && kpi.getKpiTarget().getAchievementType() != null) {
            this.isMinHigher = kpi.getKpiTarget().getAchievementType().equals((Object)AchivementTypeEnum.MIN_IS_HIGHER);
        }
        if (kpi.getAggregationType() != null && kpi.getAggregationType().getType() != null) {
            this.isAggregate = kpi.getAggregationType().getType().equalsIgnoreCase(KpiValidationParams.ScoreAggregationType.SUMMATION.name());
        }
        if (kpi.getKpiType() != null && kpi.getKpiType().getType() != null) {
            this.isMeasure = kpi.getKpiType().getType().equalsIgnoreCase(KpiValidationParams.KpiType.MESUREMENT.name());
        }
    }
    
    public KpiScoreBean getScore() {
        return this.getScore(new Date());
    }
    
    public KpiScoreBean getScore(final Date date) {
        if (this.isMeasure) {
            return this.getNumScore(date);
        }
        return this.getDateScore(date);
    }
    
    protected KpiScoreBean getDateScore(final Date date) {
        final Integer period = this.getCurrentPeriod(date);
        final Date target = this.kpi.getDateTarget();
        final Date actual = this.getDateActual(date);
        double appAch = 0.0;
        int level = 0;
        if (actual != null) {
            final KpiDateAchievementHelper achievement = new KpiDateAchievementHelper(this.kpi.getKpiTarget().getScale(), target, actual);
            level = achievement.getRatingLevel();
            appAch = achievement.getAchievement();
        }
        final KpiScoreBean score = new KpiScoreBean();
        score.setPeriodMasterId(period);
        score.setAchievement(appAch);
        score.setDateTarget(target);
        score.setDateScore(actual);
        score.setRatingLevel(level);
        return score;
    }
    
    protected KpiScoreBean getNumScore(final Date date) {
        final KpiTargetHelper target = new KpiTargetHelper(this.kpi.getKpiTarget(), this.periods, this.isAggregate);
        final KpiActualsHelper actuals = new KpiActualsHelper(this.kpi.getKpiScore(), this.periods, this.isAggregate);
        final Integer period = this.getCurrentPeriod(date);
        final double annualTarget = this.kpi.getNumTarget();
        final double applicableTarget = target.getCurrentTarget(date);
        final double applicableActual = actuals.getCurrentActuals(date);
        final double appAct = Double.isNaN(applicableActual) ? 0.0 : applicableActual;
        final double appTgt = Double.isNaN(applicableTarget) ? 0.0 : applicableTarget;
        double appAch = 0.0;
        int level = 0;
        if (!Double.isNaN(applicableActual)) {
            final KpiAchievementHelper achievement = new KpiAchievementHelper(this.kpi.getKpiTarget().getScale(), annualTarget, applicableTarget, applicableActual, this.isMinHigher);
            level = achievement.getRatingLevel();
            appAch = achievement.getAchievement();
        }
        final KpiScoreBean score = new KpiScoreBean();
        score.setPeriodMasterId(period);
        score.setAchievement(appAch);
        score.setNumScore(appAct);
        score.setNumTarget(appTgt);
        score.setRatingLevel(level);
        return score;
    }
    
    public Integer getCurrentPeriod(final Date date) {
        Integer currentPeriod = null;
        if (this.periods != null && !this.periods.isEmpty()) {
            currentPeriod = this.periods.get(0).getId();
        }
        for (final PeriodMasterBean bean : this.periods) {
            if (bean.getEndDate() != null) {
                if (bean.getEndDate().compareTo(date) > 0) {
                    break;
                }
                currentPeriod = bean.getId();
            }
        }
        return currentPeriod;
    }
    
    protected Date getDateActual(final Date queryDate) {
        Date actual = null;
        final List<KpiScoreBean> scores = (List<KpiScoreBean>)this.kpi.getKpiScore();
        if (scores != null && !scores.isEmpty()) {
            for (final KpiScoreBean score : scores) {
                actual = score.getDateScore();
                if (actual != null) {
                    if (actual.after(queryDate)) {
                        return null;
                    }
                    return actual;
                }
            }
        }
        return actual;
    }
}
