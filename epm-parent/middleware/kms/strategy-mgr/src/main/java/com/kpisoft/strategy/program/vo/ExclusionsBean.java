package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;

public class ExclusionsBean extends BaseValueObject
{
    private static final long serialVersionUID = -6525100853646312387L;
    private Integer id;
    private Integer employeeGradeId;
    private Integer programId;
    private Integer orgPositionId;
    private Integer orgUnitId;
    private Integer employeeId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getEmployeeGradeId() {
        return this.employeeGradeId;
    }
    
    public void setEmployeeGradeId(final Integer employeeGradeId) {
        this.employeeGradeId = employeeGradeId;
    }
    
    public Integer getProgramId() {
        return this.programId;
    }
    
    public void setProgramId(final Integer programId) {
        this.programId = programId;
    }
    
    public Integer getOrgPositionId() {
        return this.orgPositionId;
    }
    
    public void setOrgPositionId(final Integer orgPositionId) {
        this.orgPositionId = orgPositionId;
    }
    
    public Integer getOrgUnitId() {
        return this.orgUnitId;
    }
    
    public void setOrgUnitId(final Integer orgUnitId) {
        this.orgUnitId = orgUnitId;
    }
    
    public Integer getEmployeeId() {
        return this.employeeId;
    }
    
    public void setEmployeeId(final Integer employeeId) {
        this.employeeId = employeeId;
    }
}
