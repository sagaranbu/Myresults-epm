package com.kpisoft.org.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface OrganizationSummaryDataService extends DataAccessService
{
    public static final String ERR_ORGSUMMARY_UNKNOWN_EXCEPTION = "ORGSUMMARY-DAC-000";
    public static final String ERR_ORGSUMMAR_DOES_NOT_EXIST = "ORGSUMMARY-DAC-101";
    public static final String ERR_ORGSUMMAR_INVALID_REQUEST = "ORGSUMMARY-DAC-102";
    
    Response getOUSummary(final Request p0);
    
    Response getOUSummaries(final Request p0);
    
    Response saveOUSummary(final Request p0);
    
    Response saveOUSummaries(final Request p0);
}
