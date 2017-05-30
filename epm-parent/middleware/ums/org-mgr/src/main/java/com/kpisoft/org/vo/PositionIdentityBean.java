package com.kpisoft.org.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

@Deprecated
public class PositionIdentityBean extends BaseValueObject
{
    private static final long serialVersionUID = -1147769882890665323L;
    private Integer id;
    @NotNull
    private String name;
    @NotNull
    private String positionCode;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getPositionCode() {
        return this.positionCode;
    }
    
    public void setPositionCode(final String positionCode) {
        this.positionCode = positionCode;
    }
}
