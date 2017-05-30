package com.kpisoft.strategy.program.dac.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.strategy.program.dac.impl.domain.*;
import java.util.*;
import org.hibernate.*;

public class ProgramZoneDao extends BaseHibernateDao<ProgramZoneMetaData, Integer>
{
    public List<Integer> getProgsByProgZoneIdList(final List<Integer> progZoneIds) {
        final Session session = this.getSession();
        final Query query = session.createQuery("select progZone.programId from ProgramZoneMetaData as progZone WHERE progZone.id IN (:progZoneIds)");
        query.setParameterList("progZoneIds", (Collection)progZoneIds);
        final List<Integer> results = (List<Integer>)query.list();
        return results;
    }
}
