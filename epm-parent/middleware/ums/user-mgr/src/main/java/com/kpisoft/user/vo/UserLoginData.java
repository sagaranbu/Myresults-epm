package com.kpisoft.user.vo;

import com.canopus.mw.dto.*;
import javax.validation.constraints.*;

public class UserLoginData extends BaseValueObject
{
    @NotNull
    private String userName;
    @NotNull
    private String password;
    private Integer tenantId;
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
}
