package com.kpisoft.kpi.dac.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.kpi.dac.impl.domain.*;
import java.util.*;
import org.hibernate.*;

public class ScorecardDao extends BaseHibernateDao<ScorecardMetaData, Integer>
{
    public int updateScorecardStatus(final List<Integer> ids, final Integer state) {
        final Session session = this.getSession();
        final Query query = session.createQuery("update ScorecardMetaData as sc set sc.state = :state where sc.id IN (:ids)");
        query.setInteger("state", (int)state);
        query.setParameterList("ids", (Collection)ids);
        final int result = query.executeUpdate();
        return result;
    }
    
    public int updateScorecardScoreStatus(final List<Integer> scorecardIdList, final Integer state) {
        final Session session = this.getSession();
        final Query query = session.createQuery("update ScorecardMetaData as sc set sc.state = :state where sc.id IN (:ids)");
        query.setInteger("state", (int)state);
        query.setParameterList("ids", (Collection)scorecardIdList);
        final int result = query.executeUpdate();
        return result;
    }
}
