package com.canopus.dac;

import javax.persistence.*;

@MappedSuperclass
public interface IBaseTenantEntity
{
    Integer getTenantId();
    
    void setTenantId(final Integer p0);
}
