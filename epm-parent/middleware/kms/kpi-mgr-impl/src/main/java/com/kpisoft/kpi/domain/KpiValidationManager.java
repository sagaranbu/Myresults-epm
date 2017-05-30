package com.kpisoft.kpi.domain;

import org.springframework.stereotype.*;
import com.canopus.mw.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.kpi.vo.*;
import java.util.*;
import com.canopus.mw.dto.*;

@Component
public class KpiValidationManager extends BaseDomainManager
{
    @Autowired
    private IServiceLocator serviceLocator;
    @Autowired
    private KpiManager kpiManager;
    
    public List<StatusResponse> validateKpi(final KpiData kpi, final BaseValueObjectMap map) {
        final KpiValidationDomain kpiValidation = new KpiValidationDomain(this.kpiManager, this.serviceLocator);
        final List<StatusResponse> errorMsgs = kpiValidation.validateKpi(kpi, map);
        return errorMsgs;
    }
    
    public List<StatusResponse> validateScorecard(final KpiData kpi, final BaseValueObjectMap programRules, final Integer scorecardId) {
        final KpiValidationDomain kpiValidation = new KpiValidationDomain(this.kpiManager, this.serviceLocator);
        final List<StatusResponse> errorMsgs = kpiValidation.validateScorecard(kpi, programRules, scorecardId);
        return errorMsgs;
    }
}
