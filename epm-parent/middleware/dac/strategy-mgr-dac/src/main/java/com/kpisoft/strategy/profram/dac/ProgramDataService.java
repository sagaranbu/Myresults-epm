package com.kpisoft.strategy.profram.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface ProgramDataService extends DataAccessService
{
    Response savePerformanceProgram(final Request p0);
    
    Response getPerformanceProgram(final Request p0);
    
    Response getProgramRules(final Request p0);
    
    Response removePerformanceProgram(final Request p0);
    
    Response addOrgUnitToProgramZone(final Request p0);
    
    Response getProgramZoneByProgramId(final Request p0);
    
    Response searchProgramPolicyRule(final Request p0);
    
    Response getAllProgram(final Request p0);
    
    Response searchProgram(final Request p0);
    
    Response getProgramsByPeriodType(final Request p0);
    
    Response getProgramsForEmployee(final Request p0);
    
    Response getProgramsByIds(final Request p0);
    
    Response getProgramZoneById(final Request p0);
    
    Response searchExclusionMetaData(final Request p0);
    
    Response getProgramsByZoneIds(final Request p0);
}
