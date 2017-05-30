package com.kpisoft.kpi.impl;

import com.kpisoft.kpi.*;
import com.kpisoft.kpi.vo.*;
import com.canopus.mw.dto.*;
import java.util.*;

public class CustomValidatorExtImpl extends BaseValueObject implements CustomValidator
{
    private static final long serialVersionUID = 5826073182073821042L;
    
    public List<StatusResponse> validateKpi(final KpiData kpiData) {
        final List<StatusResponse> errorMsgs = new ArrayList<StatusResponse>();
        return errorMsgs;
    }
    
    public List<StatusResponse> validateScorecard(final KpiData kpiData) {
        final List<StatusResponse> errorMsgs = new ArrayList<StatusResponse>();
        return errorMsgs;
    }
}
