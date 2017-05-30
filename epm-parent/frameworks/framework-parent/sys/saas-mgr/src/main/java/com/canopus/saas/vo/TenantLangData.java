package com.canopus.saas.vo;

import com.canopus.mw.dto.*;

public class TenantLangData extends BaseValueObject
{
    private TenantBaseData tenantBaseData;
    private String maslang;
    
    public TenantBaseData getTenantBaseData() {
        return this.tenantBaseData;
    }
    
    public void setTenantBaseData(final TenantBaseData tenantBaseData) {
        this.tenantBaseData = tenantBaseData;
    }
    
    public String getMaslang() {
        return this.maslang;
    }
    
    public void setMaslang(final String maslang) {
        this.maslang = maslang;
    }
}
