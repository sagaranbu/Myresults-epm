package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface EntityTypeDataService extends DataAccessService
{
    Response createEntityField(final Request p0);
    
    Response getEntityField(final Request p0);
    
    Response getAllEntityFields(final Request p0);
    
    Response getEntityFieldsForEntityType(final Request p0);
    
    Response removeEntityField(final Request p0);
    
    Response getEntityFieldsForEntityTypeByLocale(final Request p0);
    
    Response searchOnEntityRelationship(final Request p0);
    
    Response searchOnEntity(final Request p0);
    
    Response getBaseEntity(final Request p0);
    
    Response createBaseEntity(final Request p0);
    
    Response removeBaseEntity(final Request p0);
}
