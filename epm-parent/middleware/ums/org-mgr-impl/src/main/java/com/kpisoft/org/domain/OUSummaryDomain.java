package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.kpisoft.org.vo.*;

public class OUSummaryDomain extends BaseDomainObject
{
    private OUSummaryData ouSummaryData;
    private OrganizationManager organizationManager;
    
    public OUSummaryDomain(final OUSummaryData ouSummaryData, final OrganizationManager organizationManager) {
        this.ouSummaryData = null;
        this.organizationManager = null;
        this.ouSummaryData = ouSummaryData;
        this.organizationManager = organizationManager;
    }
    
    public OUSummaryDomain() {
        this.ouSummaryData = null;
        this.organizationManager = null;
    }
    
    public int save() {
        return 0;
    }
    
    public OUSummaryData get() {
        return null;
    }
}
