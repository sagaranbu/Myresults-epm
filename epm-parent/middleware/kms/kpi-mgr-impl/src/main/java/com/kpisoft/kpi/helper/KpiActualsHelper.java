package com.kpisoft.kpi.helper;

import com.kpisoft.kpi.vo.*;
import java.util.*;

public class KpiActualsHelper extends AbstractCumulativeHelper
{
    private List<KpiScoreBean> actuals;
    
    public KpiActualsHelper(final List<KpiScoreBean> actuals, final List<PeriodMasterBean> periods, final boolean isAggregate) {
        super(periods, isAggregate);
        this.actuals = null;
        this.actuals = actuals;
        final Map<Integer, Double> qtyMap = new HashMap<Integer, Double>();
        if (actuals != null) {
            for (final KpiScoreBean actual : actuals) {
                if (actual != null) {
                    Double act;
                    if (isAggregate) {
                        act = actual.getNumScore_mtd();
                    }
                    else {
                        act = actual.getNumScore();
                    }
                    if (act == null) {
                        continue;
                    }
                    qtyMap.put(actual.getPeriodMasterId(), act);
                }
            }
        }
        this.setQuantityMap(qtyMap);
    }
    
    protected Double getCurrentActuals(final Date date) {
        return super.getCurrent(date);
    }
    
    protected Double getCurrentActuals() {
        return super.getCurrent(new Date());
    }
}
