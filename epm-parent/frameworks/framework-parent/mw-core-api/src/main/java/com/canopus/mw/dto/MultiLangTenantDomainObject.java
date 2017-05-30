package com.canopus.mw.dto;

public class MultiLangTenantDomainObject<T extends MultiLangDataExtension> extends MultiLangDomainObject<T>
{
    private Integer tenantId;
    
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
}
