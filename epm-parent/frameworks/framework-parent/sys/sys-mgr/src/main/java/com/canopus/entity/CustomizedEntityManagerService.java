package com.canopus.entity;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface CustomizedEntityManagerService extends MiddlewareService
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
    
    Response createEntityInstanceFieldData(final Request p0);
    
    Response updateEntityInstanceFieldData(final Request p0);
    
    Response getAllEntityInstanceFieldData(final Request p0);
    
    Response createEntityFieldData(final Request p0);
    
    Response updateEntityFieldData(final Request p0);
    
    Response removeEntityFieldData(final Request p0);
    
    Response createEntityInstance(final Request p0);
}
