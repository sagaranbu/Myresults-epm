package com.kpisoft.strategy.program.service;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface ProgramManagerService extends MiddlewareService
{
    Response createPerformanceProgram(final Request p0);
    
    Response updatePerformanceProgram(final Request p0);
    
    Response getPerformanceProgram(final Request p0);
    
    Response getProgramRules(final Request p0);
    
    Response removePerformanceProgram(final Request p0);
    
    Response getProgramZoneByProgramId(final Request p0);
    
    Response addOrgUnitToProgramZone(final Request p0);
    
    Response getAllProgram(final Request p0);
    
    Response searchProgram(final Request p0);
    
    Response getProgramsByPeriodType(final Request p0);
    
    Response getValidationRules(final Request p0);
    
    Response getWorkflowLevels(final Request p0);
    
    Response getProgramsForEmployee(final Request p0);
    
    Response getProgramsByIds(final Request p0);
    
    Response getProgramZoneById(final Request p0);
    
    Response getProgramsByZoneIds(final Request p0);
    
    Response getProgramDetails(final Request p0);
}
