package com.kpisoft.emp.dac.impl;

import com.canopus.dac.hibernate.*;

public class ManagerLevelDao extends BaseHibernateDao<ManagerLevel, Integer>
{
    public ManagerLevel merge(final ManagerLevel managerLevel) {
        if (managerLevel.getId() != null && managerLevel.getId() > 0) {
            return (ManagerLevel)super._merge((Object)managerLevel);
        }
        super._save((Object)managerLevel);
        return managerLevel;
    }
}
