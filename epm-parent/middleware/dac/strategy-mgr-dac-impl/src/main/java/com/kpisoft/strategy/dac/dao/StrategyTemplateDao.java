package com.kpisoft.strategy.dac.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.strategy.dac.impl.domain.*;

public class StrategyTemplateDao extends BaseHibernateDao<StrategyTemplate, Integer>
{
    public StrategyTemplate merge(final StrategyTemplate str) {
        if (str.getId() != null && str.getId() > 0) {
            return (StrategyTemplate)super._merge((Object)str);
        }
        super._save((Object)str);
        return str;
    }
}
