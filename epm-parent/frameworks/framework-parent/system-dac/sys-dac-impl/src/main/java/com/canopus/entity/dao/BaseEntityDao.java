package com.canopus.entity.dao;

import com.canopus.dac.hibernate.*;
import com.canopus.entity.domain.*;

public class BaseEntityDao extends BaseHibernateDao<BaseEntity, Integer>
{
    public BaseEntity merge(final BaseEntity baseEntity) {
        if (baseEntity.getId() != null && baseEntity.getId() > 0) {
            return (BaseEntity)super.merge((Object)baseEntity);
        }
        super.save(baseEntity);
        return baseEntity;
    }
}
