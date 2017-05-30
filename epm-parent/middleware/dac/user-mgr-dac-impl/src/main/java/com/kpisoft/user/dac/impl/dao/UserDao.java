package com.kpisoft.user.dac.impl.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.user.dac.impl.entity.*;
import java.util.*;
import org.hibernate.*;
import com.canopus.mw.dto.*;
import org.hibernate.criterion.*;

public class UserDao extends BaseHibernateDao<User, Integer>
{
    public List<Object[]> getEmployeeIdsByRoles(final List<Integer> roleIds) {
        final Session session = this.getSession();
        final Query query = session.createQuery("select urr.roleId, user.employeeId, urr.userId from UserRoleRelationship as urr, User as user  where  urr.userId = user.id and urr.roleId IN (:roleIds)");
        query.setParameterList("roleIds", (Collection)roleIds);
        final List<Object[]> results = (List<Object[]>)query.list();
        return results;
    }
    
    public List<Object[]> getUsersIdEmailFromRoleList(final List<Integer> roleIds) {
        final Session session = this.getSession();
        final Query query = session.createQuery("SELECT urr.roleId,urr.userId,user.employeeId,user.email  from UserRoleRelationship as urr, User as user  where  urr.userId = user.id and urr.roleId IN (:roleIds)");
        query.setParameterList("roleIds", (Collection)roleIds);
        final List<Object[]> results = (List<Object[]>)query.list();
        return results;
    }
    
    public List<User> searchByUserName(final String userName, final Integer tenantId, final boolean isExact) {
        final Session session = this.getSession();
        final Criteria criteria = session.createCriteria((Class)User.class);
        if (isExact) {
            criteria.add((Criterion)Restrictions.eq("userName", (Object)userName));
        }
        else {
            criteria.add(Restrictions.ilike("userName", (Object)("%" + userName + "%")));
        }
        criteria.add((Criterion)Restrictions.eq("deleted", (Object)false));
        if (tenantId != null && tenantId != -1) {
            criteria.add((Criterion)Restrictions.eq("tenantId", (Object)tenantId));
        }
        final List<User> userList = (List<User>)criteria.list();
        return userList;
    }
    
    public Long getActiveUserCount() {
        final Session session = this.getSession();
        final Criteria criteria = session.createCriteria((Class)User.class);
        criteria.add((Criterion)Restrictions.eq("tenantId", (Object)ExecutionContext.getTenantId()));
        criteria.add((Criterion)Restrictions.eq("deleted", (Object)false));
        criteria.setProjection(Projections.rowCount());
        final Long count = (Long)criteria.uniqueResult();
        return count;
    }
}
