package com.canopus.dac;

import org.hibernate.envers.*;
import org.hibernate.annotations.*;
import javax.persistence.*;

@Audited
@MappedSuperclass
@FilterDef(name = "tenant", parameters = { @ParamDef(name = "tenantId", type = "int") })
@Filter(name = "tenant", condition = ":tenantId = TENANT_ID")
public abstract class BaseMultiLangTenantEntity<T extends MultiLangExtension> extends BaseMultiLangEntity<T> implements IBaseTenantEntity
{
    @Column(name = "TENANT_ID", length = 11)
    private Integer tenantId;
    
    @Override
    public Integer getTenantId() {
        return this.tenantId;
    }
    
    @Override
    public void setTenantId(final Integer tenantId) {
        this.tenantId = tenantId;
    }
}
