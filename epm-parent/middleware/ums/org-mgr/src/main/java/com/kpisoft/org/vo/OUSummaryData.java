package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import com.canopus.mw.aggregation.*;

public class OUSummaryData extends BaseValueObject implements ISummaryData, Comparable<OUSummaryData>
{
    private int id;
    private int localEmpCount;
    private Integer tenantId;
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public int getLocalEmpCount() {
        return this.localEmpCount;
    }
    
    public void setLocalEmpCount(final int localEmpCount) {
        this.localEmpCount = localEmpCount;
    }
    
    public OUSummaryData(final int id, final int localEmpCount) {
        this.id = id;
        this.localEmpCount = localEmpCount;
    }
    
    public OUSummaryData() {
    }
    
    public int hashCode() {
        return this.id;
    }
    
    public boolean equals(final Object obj) {
        return this.getId() == ((OUSummaryData)obj).getId();
    }
    
    public String toString() {
        return " id=" + this.id + "\n empcount" + this.localEmpCount;
    }
    
    public int compareTo(final OUSummaryData o) {
        return this.getId() - o.getId();
    }
}
