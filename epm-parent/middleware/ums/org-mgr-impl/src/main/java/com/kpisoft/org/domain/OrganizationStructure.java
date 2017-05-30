package com.kpisoft.org.domain;

import com.canopus.mw.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.dac.*;
import com.kpisoft.org.params.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.utils.*;
import javax.validation.*;

public class OrganizationStructure extends BaseDomainObject
{
    private OrganizationStructureManager structureManager;
    private OrganizationDimensionStructureData structureData;
    private OrganizationStructureDataService dataService;
    
    public OrganizationStructure(final OrganizationStructureManager structureManager, final OrganizationStructureDataService dataService) {
        this.dataService = null;
        this.structureManager = structureManager;
        this.dataService = dataService;
    }
    
    public OrganizationDimensionStructureData save(final OrganizationDimensionStructureData orgTypeData) {
        this.validate(orgTypeData);
        final Request request = new Request();
        request.put(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name(), (BaseValueObject)orgTypeData);
        final Response response = this.dataService.saveOrganizationStructureDimension(request);
        final OrganizationDimensionStructureData result = (OrganizationDimensionStructureData)response.get(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name());
        return result;
    }
    
    public void validate(final Object object) {
        final ValidationHelper vh = new ValidationHelper();
        vh.validate(this.getValidator(), object, "ERR_STRUCTURE_INVALID_INPUT", "Invalid structure details");
    }
    
    private Validator getValidator() {
        return this.structureManager.getValidator();
    }
    
    public OrganizationDimensionStructureData getStructureData() {
        return this.structureData;
    }
    
    public void setStructureData(final OrganizationDimensionStructureData structureData) {
        this.structureData = structureData;
    }
}
