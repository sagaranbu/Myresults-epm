package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;

public class OrgStructureMappingData extends BaseValueObject
{
    private static final long serialVersionUID = -2361361884347237602L;
    private Integer id;
    private Integer status;
    private OrganizationUnitData orgUnit;
    private OrganizationDimensionStructureData structure;
    
    public OrgStructureMappingData() {
        this.status = 1;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getStatus() {
        return this.status;
    }
    
    public void setStatus(final Integer status) {
        this.status = status;
    }
    
    public OrganizationUnitData getOrgUnit() {
        return this.orgUnit;
    }
    
    public void setOrgUnit(final OrganizationUnitData orgUnit) {
        this.orgUnit = orgUnit;
    }
    
    public OrganizationDimensionStructureData getStructure() {
        return this.structure;
    }
    
    public void setStructure(final OrganizationDimensionStructureData structure) {
        this.structure = structure;
    }
}
