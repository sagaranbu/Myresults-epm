package com.kpisoft.kpi.impl;

import com.kpisoft.kpi.*;
import com.kpisoft.kpi.vo.*;
import com.canopus.mw.dto.*;
import java.util.*;

public class CustomValidatorImpl extends BaseValueObject implements CustomValidator
{
    private static final long serialVersionUID = -3741461439467172955L;
    
    public List<StatusResponse> validateKpi(final KpiData kpiData) {
        final List<StatusResponse> errorMsgs = new ArrayList<StatusResponse>();
        return errorMsgs;
    }
    
    public List<StatusResponse> validateScorecard(final KpiData kpiData) {
        final List<StatusResponse> errorMsgs = new ArrayList<StatusResponse>();
        return errorMsgs;
    }
}
