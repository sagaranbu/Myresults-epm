package com.kpisoft.strategy.program.vo;

import com.canopus.mw.dto.*;
import com.canopus.entity.vo.*;

public class KpiRuleBean extends BaseValueObject
{
    private static final long serialVersionUID = -5185309807613313285L;
    private Integer id;
    private String value;
    private SystemMasterBaseBean systemMasterBaseData;
    private Integer programPolicyId;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public SystemMasterBaseBean getSystemMasterBaseData() {
        return this.systemMasterBaseData;
    }
    
    public void setSystemMasterBaseData(final SystemMasterBaseBean systemMasterBaseData) {
        this.systemMasterBaseData = systemMasterBaseData;
    }
    
    public Integer getProgramPolicyId() {
        return this.programPolicyId;
    }
    
    public void setProgramPolicyId(final Integer programPolicyId) {
        this.programPolicyId = programPolicyId;
    }
}
