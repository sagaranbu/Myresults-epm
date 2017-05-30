package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;

public class ProgramZoneBean extends BaseValueObject
{
    private static final long serialVersionUID = 2093087146163791583L;
    private Integer id;
    private Integer orgUnitId;
    private String value;
    private Integer programId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(final Integer programId) {
        this.programId = programId;
    }
}
