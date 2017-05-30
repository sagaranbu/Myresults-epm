package com.kpisoft.kpi.helper;

import org.apache.log4j.*;
import com.kpisoft.kpi.vo.*;
import com.kpisoft.kpi.utility.*;
import java.util.*;

public class KpiDateAchievementHelper
{
    private KpiScaleBean kpiScale;
    private Date target;
    private Date actual;
    private static final Logger log;
    
    public KpiDateAchievementHelper(final KpiScaleBean scale, final Date target, final Date actual) {
        this.kpiScale = scale;
        this.target = target;
        this.actual = actual;
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
        final Date min = scaleBean.getFromDate();
        final Date max = scaleBean.getToDate();
        final double scaleDiff = this.diff(max, min);
        final double actualDiff = this.diff(this.actual, min);
        final double pointsRange = maxPoints - minPoints;
        if (scaleDiff != 0.0) {
            points = minPoints + actualDiff / scaleDiff * pointsRange;
        }
        if (points < 0.0) {
            points = 0.0;
        }
        return new Double(points);
    }
    
    private double diff(final Date after, final Date before) {
        if (after == null || before == null) {
            return 0.0;
        }
        final long afterMs = after.getTime();
        final long beforeMs = before.getTime();
        final long diffMs = afterMs - beforeMs;
        final double diffDays = Math.round(diffMs / 8.64E7);
        return diffDays;
    }
    
    private ScaleValueBean getScaleLevel() {
        if (this.kpiScale.getAlkpiMasterScaleValue() == null) {
            return null;
        }
        final List<ScaleValueBean> scales = new ArrayList<ScaleValueBean>();
        scales.addAll(this.kpiScale.getAlkpiMasterScaleValue());
        ScaleValueBean level = null;
        for (final ScaleValueBean scale : scales) {
            final Date fromValue = scale.getFromDate();
            final Date toValue = scale.getToDate();
            final ScaleRange range = new ScaleRange((Comparable)fromValue, (Comparable)toValue);
            final boolean isRange = range.contains((Comparable)this.actual, true);
            level = scale;
            KpiDateAchievementHelper.log.debug((Object)("SCORELOG " + level.getScaleIndex() + ": " + fromValue + "-" + toValue + ". Score = " + this.actual + ", is range = " + isRange));
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
