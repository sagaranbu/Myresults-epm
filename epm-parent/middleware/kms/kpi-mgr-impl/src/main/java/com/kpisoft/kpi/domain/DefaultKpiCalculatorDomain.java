package com.kpisoft.kpi.domain;

import com.canopus.mw.*;
import org.apache.log4j.*;
import com.kpisoft.kpi.vo.param.*;
import com.kpisoft.kpi.vo.*;
import java.util.*;
import com.kpisoft.kpi.utility.*;
import java.text.*;

public class DefaultKpiCalculatorDomain extends BaseDomainObject
{
    private static final Logger log;
    
    public Integer calcKpiRatingLevel(final String achievementType, final String kpiType, final double scoreActual, final KpiScaleBean kpiScaleBean, final Date dateScoreActual) {
        Integer ratingLevel = 0;
        boolean isMesurement = false;
        boolean isMinHigher = false;
        TreeMap<Integer, ScaleValueBean> scaleValueMap = new TreeMap<Integer, ScaleValueBean>();
        for (final ScaleValueBean scaleValueBean : kpiScaleBean.getAlkpiMasterScaleValue()) {
            scaleValueMap.put(scaleValueBean.getScaleIndex(), scaleValueBean);
        }
        if (achievementType.equalsIgnoreCase(AchivementTypeEnum.MIN_IS_HIGHER.name())) {
            scaleValueMap = new TreeMap<Integer, ScaleValueBean>(scaleValueMap.descendingMap());
            isMinHigher = true;
        }
        if (kpiType.equalsIgnoreCase(KpiValidationParams.KpiType.MESUREMENT.name())) {
            isMesurement = true;
        }
        ratingLevel = this.calcKpiRating(scaleValueMap, isMesurement, scoreActual, dateScoreActual, isMinHigher);
        return ratingLevel;
    }
    
    public Double calcKpiAggregatedTarget(final String aggregationType, final List<KpiTargetDataBean> kpiTargetDataBeanList) {
        Double target = 0.0;
        int count = 1;
        if (aggregationType.equalsIgnoreCase(KpiValidationParams.KpiAggregationType.SUMMATION.name())) {
            for (final KpiTargetDataBean kpiTargetDataBean : kpiTargetDataBeanList) {
                target += kpiTargetDataBean.getTargetDataNum();
            }
        }
        else if (aggregationType.equalsIgnoreCase(KpiValidationParams.KpiAggregationType.AVERAGE.name()) || aggregationType.equalsIgnoreCase(KpiValidationParams.KpiAggregationType.CONSTANT.name())) {
            for (final KpiTargetDataBean kpiTargetDataBean : kpiTargetDataBeanList) {
                target += kpiTargetDataBean.getTargetDataNum();
                ++count;
            }
            target /= count;
        }
        return target;
    }
    
    public Double calcKpiAcievement(final KpiScoreBean kpiScoreBean, final String acievementType) {
        Double kpiAchievement = 0.0;
        if (acievementType.equalsIgnoreCase(AchivementTypeEnum.MAX_IS_HIGHER.name()) && kpiScoreBean.getNumTarget() != null && kpiScoreBean.getNumTarget() != 0.0) {
            kpiAchievement = kpiScoreBean.getNumScore() / kpiScoreBean.getNumTarget() * 100.0;
        }
        return kpiAchievement;
    }
    
    private Integer calcKpiRating(final TreeMap<Integer, ScaleValueBean> scaleValueMap, final boolean isMesurement, final double scoreActual, final Date dateScoreActual, final boolean isMinHigher) {
        Integer ratingLevel = 0;
        boolean isRange = false;
        if (isMesurement) {
            for (final Map.Entry<Integer, ScaleValueBean> entry : scaleValueMap.entrySet()) {
                Double fromValue = null;
                Double toValue = null;
                if (entry.getValue().getFromValue() != null) {
                    fromValue = entry.getValue().getFromValue();
                }
                if (entry.getValue().getToValue() != null) {
                    toValue = entry.getValue().getToValue();
                }
                isRange = new ScaleRange((Comparable)fromValue, (Comparable)toValue).contains((Comparable)scoreActual, isMinHigher);
                ratingLevel = entry.getValue().getScaleIndex();
                DefaultKpiCalculatorDomain.log.debug((Object)("SCORELOG " + ratingLevel + ": " + fromValue + "-" + toValue + ". Score = " + scoreActual + ", Min = " + isMinHigher + ", is range = " + isRange));
                if (isRange) {
                    break;
                }
            }
        }
        else {
            for (final Map.Entry<Integer, ScaleValueBean> entry : scaleValueMap.entrySet()) {
                Date fromValue2 = null;
                Date toValue2 = null;
                if (entry.getValue().getFromDate() != null) {
                    fromValue2 = entry.getValue().getFromDate();
                }
                if (entry.getValue().getToDate() != null) {
                    toValue2 = entry.getValue().getToDate();
                }
                isRange = new ScaleRange((Comparable)fromValue2, (Comparable)toValue2).contains((Comparable)dateScoreActual, isMinHigher);
                ratingLevel = entry.getValue().getScaleIndex();
                if (isRange) {
                    break;
                }
            }
        }
        return ratingLevel;
    }
    
    public KpiCalcUtility calculateKpiPerformancePoints(final String achievementType, final String kpiType, final double scoreActual, final KpiScaleBean kpiScaleBean, final double target, final double targetCumulative, final Date dateScoreActual, final Date dateTarget) throws Exception {
        boolean isMesurement = false;
        boolean isMinHigher = false;
        final KpiCalcUtility utility = new KpiCalcUtility();
        TreeMap<Integer, ScaleValueBean> scaleValueMap = new TreeMap<Integer, ScaleValueBean>();
        if (kpiType.equalsIgnoreCase(KpiValidationParams.KpiType.MESUREMENT.name())) {
            isMesurement = true;
            double targetValue = target;
            if (targetValue == 0.0) {
                targetValue = 1.0;
            }
            final DecimalFormat format = new DecimalFormat("#.##");
            for (final ScaleValueBean iterator : kpiScaleBean.getAlkpiMasterScaleValue()) {
                if (iterator.getFromValue() != null) {
                    final double limit = targetCumulative / targetValue * iterator.getFromValue();
                    iterator.setFromValue(Double.parseDouble(format.format(limit)));
                }
                if (iterator.getToValue() != null) {
                    final double limit = targetCumulative / targetValue * iterator.getToValue();
                    iterator.setToValue(Double.parseDouble(format.format(limit)));
                }
            }
        }
        for (final ScaleValueBean scaleValueBean : kpiScaleBean.getAlkpiMasterScaleValue()) {
            scaleValueMap.put(scaleValueBean.getScaleIndex(), scaleValueBean);
        }
        if (achievementType.equalsIgnoreCase(AchivementTypeEnum.MIN_IS_HIGHER.name())) {
            scaleValueMap = new TreeMap<Integer, ScaleValueBean>(scaleValueMap.descendingMap());
            isMinHigher = true;
        }
        final Integer ratingLevel = this.calcKpiRating(scaleValueMap, isMesurement, scoreActual, dateScoreActual, isMinHigher);
        final ScaleValueBean scaleBean = scaleValueMap.get(ratingLevel);
        final double performanceMinPoints = scaleBean.getFromPoint();
        final double performanceMaxPoints = scaleBean.getToPoint();
        final double actual = 0.0;
        double min = 0.0;
        double max = 0.0;
        double performancePoints = 0.0;
        double middleCalc = 0.0;
        long minL = 0L;
        long maxL = 0L;
        long actualL = 0L;
        if (isMesurement) {
            if (scaleBean.getFromValue() != null) {
                min = scaleBean.getFromValue();
            }
            if (scaleBean.getToValue() != null) {
                max = scaleBean.getToValue();
            }
            if (max - min != 0.0) {
                middleCalc = (scoreActual - min) / (max - min);
            }
        }
        else {
            actualL = dateScoreActual.getTime();
            if (scaleBean.getFromDate() != null) {
                minL = scaleBean.getFromDate().getTime();
            }
            if (scaleBean.getToDate() != null) {
                maxL = scaleBean.getToDate().getTime();
            }
            if (maxL - minL != 0.0) {
                middleCalc = (actualL - minL) / (maxL - minL);
            }
        }
        performancePoints = performanceMinPoints;
        performancePoints += middleCalc * (performanceMaxPoints - performanceMinPoints);
        if (achievementType.equalsIgnoreCase(AchivementTypeEnum.MIN_IS_HIGHER.name()) && performancePoints > performanceMinPoints) {
            performancePoints = 0.0;
        }
        if (performancePoints < 0.0) {
            performancePoints = 0.0;
        }
        utility.setRatingLevel(ratingLevel);
        utility.setAchievement(performancePoints);
        return utility;
    }
    
    static {
        log = Logger.getLogger((Class)DefaultKpiCalculatorDomain.class);
    }
}
