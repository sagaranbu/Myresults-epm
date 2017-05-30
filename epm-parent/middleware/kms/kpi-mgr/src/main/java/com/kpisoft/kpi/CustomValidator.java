package com.kpisoft.kpi;

import com.kpisoft.kpi.vo.*;
import java.util.*;
import com.canopus.mw.dto.*;

public interface CustomValidator
{
    List<StatusResponse> validateKpi(final KpiData p0);
    
    List<StatusResponse> validateScorecard(final KpiData p0);
}
