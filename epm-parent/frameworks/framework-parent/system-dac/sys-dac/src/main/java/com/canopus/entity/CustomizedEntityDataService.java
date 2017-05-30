package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface CustomizedEntityDataService extends DataAccessService
{
    Response getCustomEntity(final Request p0);
    
    Response getAllCustomEntity(final Request p0);
    
    Response getCustomEntityFields(final Request p0);
    
    Response getCustomEntityFieldsByGroupId(final Request p0);
    
    Response getCustomEntityFieldLangValues(final Request p0);
    
    Response getDataType(final Request p0);
    
    Response getAllDataTypes(final Request p0);
    
    Response getFieldType(final Request p0);
    
    Response getAllFieldType(final Request p0);
    
    Response getEntityInstance(final Request p0);
    
    Response getAllEntityInstanceByEntityID(final Request p0);
    
    Response saveEntityInstanceFieldData(final Request p0);
    
    Response getAllEntityInstanceFieldData(final Request p0);
    
    Response createCustomEntity(final Request p0);
    
    Response removeCustomEntityField(final Request p0);
    
    Response createCustomEntityInstance(final Request p0);
}
