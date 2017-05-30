package com.canopus.entity.dao;

import com.canopus.dac.hibernate.*;
import com.canopus.entity.domain.*;

public class EntityTypeDao extends BaseHibernateDao<EntityField, Integer>
{
    public EntityField merge(final EntityField field) {
        if (field.getId() != null && field.getId() > 0) {
            return (EntityField)super.merge((Object)field);
        }
        super.save(field);
        return field;
    }
}
