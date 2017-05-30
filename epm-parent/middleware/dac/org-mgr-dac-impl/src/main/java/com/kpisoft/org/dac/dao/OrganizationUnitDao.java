package com.kpisoft.org.dac.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.org.dac.impl.domain.OrganizationUnit;

public class OrganizationUnitDao extends BaseHibernateDao<OrganizationUnit, Integer>
{
    public OrganizationUnit merge(final OrganizationUnit orgUnit) {
        if (orgUnit.getId() != null && orgUnit.getId() > 0) {
            return (OrganizationUnit)super._merge((Object)orgUnit);
        }
        super.save(orgUnit);
        return orgUnit;
    }
}
