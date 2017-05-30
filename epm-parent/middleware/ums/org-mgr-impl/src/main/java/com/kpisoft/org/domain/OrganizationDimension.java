package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.dac.*;
import com.kpisoft.org.params.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import javax.validation.*;

public class OrganizationDimension extends BaseDomainObject
{
    private OrganizationDimensionManager objOrganizationDimensionManager;
    private OrganizationDimensionData objOrganizationDimensionData;
    private OrganizationDimensionDataService objDimensionDataService;
    
    public OrganizationDimension(final OrganizationDimensionManager objOrganizationDimensionManager, final OrganizationDimensionDataService objDimensionDataService) {
        this.objDimensionDataService = null;
        this.objOrganizationDimensionManager = objOrganizationDimensionManager;
        this.objDimensionDataService = objDimensionDataService;
    }
    
    public OrganizationDimensionData save(OrganizationDimensionData objOrganizationDimensionData) {
        this.validate(objOrganizationDimensionData);
        final Request request = new Request();
        request.put(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)objOrganizationDimensionData);
        final Response response = this.objDimensionDataService.saveOrganizationDimension(request);
        objOrganizationDimensionData = (OrganizationDimensionData)response.get(OrgDimensionParams.ORG_DIM_DATA.name());
        return objOrganizationDimensionData;
    }
    
    public boolean removeOrganizationDimension(final int id) {
        final Request request = new Request();
        request.put(OrgDimensionParams.ORG_DIM_DATA_ID.name(), (BaseValueObject)new Identifier(id));
        final Response response = this.objDimensionDataService.removeOrganizationDimension(request);
        final BooleanResponse bResponse = (BooleanResponse)response.get(OrgDimensionParams.ORG_DIM_STATUS_RESPONSE.name());
        return bResponse.isResponse();
    }
    
    public OrganizationDimensionData updateOrganizationDimension(OrganizationDimensionData objOrganizationDimensionData) {
        final Request request = new Request();
        request.put(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)objOrganizationDimensionData);
        final Response response = this.objDimensionDataService.saveOrganizationDimension(request);
        objOrganizationDimensionData = (OrganizationDimensionData)response.get(OrgDimensionParams.ORG_DIM_DATA.name());
        return objOrganizationDimensionData;
    }
    
    public void validate(final Object object) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), object, "ERR_DIMENSION_INVALID_INPUT", "Invalid  details");
    }
    
    public OrganizationDimensionData getObjOrganizationDimensionData() {
        return this.objOrganizationDimensionData;
    }
    
    public void setObjOrganizationDimensionData(final OrganizationDimensionData objOrganizationDimensionData) {
        this.objOrganizationDimensionData = objOrganizationDimensionData;
    }
    
    private Validator getValidator() {
        return this.objOrganizationDimensionManager.getValidator();
    }
}
