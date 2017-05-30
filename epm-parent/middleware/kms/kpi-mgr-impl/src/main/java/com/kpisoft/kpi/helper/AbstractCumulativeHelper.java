package com.kpisoft.kpi.helper;

import com.kpisoft.kpi.vo.*;
import java.util.*;

public abstract class AbstractCumulativeHelper
{
    protected Map<Integer, Double> quantityMap;
    protected boolean isAggregate;
    protected List<PeriodMasterBean> periods;
    
    public AbstractCumulativeHelper(final List<PeriodMasterBean> periods, final boolean isAggregate) {
        this.quantityMap = null;
        this.isAggregate = false;
        this.isAggregate = isAggregate;
        this.periods = periods;
    }
    
    protected void setQuantityMap(final Map<Integer, Double> quantityMap) {
        this.quantityMap = quantityMap;
    }
    
    protected Double getCumulative(final Date date) {
        double cumulativeQty = 0.0;
        boolean found = false;
        for (final PeriodMasterBean period : this.periods) {
            if (period != null) {
                final Double quantity = this.quantityMap.get(period.getId());
                if (period.getEndDate() == null || period.getEndDate().compareTo(date) > 0) {
                    break;
                }
                if (this.isAggregate) {
                    if (quantity == null) {
                        continue;
                    }
                    cumulativeQty += quantity;
                    found = true;
                }
                else if (quantity != null) {
                    cumulativeQty = quantity;
                    found = true;
                }
                else {
                    cumulativeQty = 0.0;
                    found = false;
                }
            }
        }
        if (found) {
            return cumulativeQty;
        }
        return Double.NaN;
    }
    
    protected Double getCurrent(final Date date) {
        if (this.quantityMap != null && !this.quantityMap.isEmpty()) {
            return this.getCumulative(date);
        }
        return Double.NaN;
    }
}
