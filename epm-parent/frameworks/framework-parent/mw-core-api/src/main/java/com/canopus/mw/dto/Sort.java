package com.canopus.mw.dto;

import javax.validation.constraints.*;

public class Sort extends BaseValueObject
{
    private static final long serialVersionUID = -3842307109143616013L;
    @NotNull
    private String field;
    private boolean isDesc;
    private boolean ignoreCase;
    
    public String getField() {
        return this.field;
    }
    
    public void setField(final String field) {
        this.field = field;
    }
    
    public boolean isDesc() {
        return this.isDesc;
    }
    
    public void setDesc(final boolean isDesc) {
        this.isDesc = isDesc;
    }
    
    public boolean isIgnoreCase() {
        return this.ignoreCase;
    }
    
    public void setIgnoreCase(final boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }
}
