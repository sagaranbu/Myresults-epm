package com.kpisoft.kpi.helper;

import com.kpisoft.kpi.vo.*;
import java.util.*;

public class KpiTargetHelper extends AbstractCumulativeHelper
{
    private KpiTargetBean kpiTarget;
    
    public KpiTargetHelper(final KpiTargetBean target, final List<PeriodMasterBean> periods, final boolean isAggregate) {
        super(periods, isAggregate);
        this.kpiTarget = null;
        this.kpiTarget = target;
        final Map<Integer, Double> qtyMap = new HashMap<Integer, Double>();
        if (this.kpiTarget.getTargetData() != null) {
            for (final KpiTargetDataBean breakup : this.kpiTarget.getTargetData()) {
                if (breakup != null && breakup.getTargetDataNum() != null) {
                    qtyMap.put(breakup.getPeriodMasterId(), breakup.getTargetDataNum());
                }
            }
        }
        this.setQuantityMap(qtyMap);
    }
    
    public Double getCurrentTarget(final Date date) {
        if (this.kpiTarget.getTargetData() != null && this.kpiTarget.getTargetData().size() > 0) {
            return this.getCumulative(date);
        }
        return this.kpiTarget.getNumTarget();
    }
    
    public Double getCurrentTarget() {
        return this.getCurrentTarget(new Date());
    }
}
