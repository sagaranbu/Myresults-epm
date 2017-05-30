package com.kpisoft.kpi.helper;

import org.apache.log4j.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.utility.*;
import java.util.*;

public class KpiAchievementHelper
{
    private KpiScaleBean kpiScale;
    private Double annualTarget;
    private Double applicableTarget;
    private Double applicableActuals;
    private boolean isMinHigher;
    private static final Logger log;
    
    public KpiAchievementHelper(final KpiScaleBean scale, final Double annualTarget, final Double applicableTarget, final Double applicableActuals, final boolean isMinHigher) {
        this.kpiScale = scale;
        this.annualTarget = annualTarget;
        this.applicableTarget = applicableTarget;
        this.applicableActuals = applicableActuals;
        this.isMinHigher = isMinHigher;
    }
    
    public Integer getRatingLevel() {
        final ScaleValueBean scaleBean = this.getScaleLevel();
        int level = 0;
        if (scaleBean != null) {
            level = scaleBean.getScaleIndex();
        }
        return new Integer(level);
    }
    
    public Double getAchievement() {
        final ScaleValueBean scaleBean = this.getScaleLevel();
        if (scaleBean == null) {
            return new Double(0.0);
        }
        final double minPoints = this.nullSafeGet(scaleBean.getFromPoint(), 0.0);
        final double maxPoints = this.nullSafeGet(scaleBean.getToPoint(), 0.0);
        double points = minPoints;
        final double actual = this.applicableActuals;
        final double min = this.getPeriodScaleValue(scaleBean.getFromValue());
        final double max = this.getPeriodScaleValue(scaleBean.getToValue());
        final double scaleDiff = max - min;
        final double actualDiff = actual - min;
        final double pointsRange = maxPoints - minPoints;
        if (scaleDiff != 0.0) {
            points = minPoints + actualDiff / scaleDiff * pointsRange;
        }
        if (this.isMinHigher && points > minPoints) {
            points = 0.0;
        }
        if (points < 0.0) {
            points = 0.0;
        }
        return new Double(points);
    }
    
    private Double getPeriodScaleValue(final Double value) {
        if (value == null || this.annualTarget == null || this.applicableTarget == null) {
            return null;
        }
        if (this.annualTarget == 0.0) {
            return new Double(0.0);
        }
        double val = value;
        final double annual = this.annualTarget;
        final double appl = this.applicableTarget;
        val = val / annual * appl;
        return new Double(val);
    }
    
    private ScaleValueBean getScaleLevel() {
        if (this.kpiScale.getAlkpiMasterScaleValue() == null) {
            return null;
        }
        final List<ScaleValueBean> scales = new ArrayList<ScaleValueBean>();
        scales.addAll(this.kpiScale.getAlkpiMasterScaleValue());
        ScaleValueBean level = null;
        for (final ScaleValueBean scale : scales) {
            final Double fromValue = this.getPeriodScaleValue(scale.getFromValue());
            final Double toValue = this.getPeriodScaleValue(scale.getToValue());
            final ScaleRange range = new ScaleRange((Comparable)fromValue, (Comparable)toValue);
            final boolean isRange = range.contains((Comparable)this.applicableActuals, this.isMinHigher);
            level = scale;
            KpiAchievementHelper.log.debug((Object)("SCORELOG " + level.getScaleIndex() + ": " + fromValue + "-" + toValue + ". Score = " + this.applicableActuals + ", Min = " + this.isMinHigher + ", is range = " + isRange));
            if (isRange) {
                return level;
            }
        }
        return level;
    }
    
    private double nullSafeGet(final Double val, final double def) {
        if (val == null) {
            return def;
        }
        return val;
    }
    
    static {
        log = Logger.getLogger((Class)KpiAchievementHelper.class);
    }
}
