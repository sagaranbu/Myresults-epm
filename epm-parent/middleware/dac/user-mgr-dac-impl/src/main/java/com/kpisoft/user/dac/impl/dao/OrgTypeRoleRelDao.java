package com.kpisoft.user.dac.impl.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.user.dac.impl.entity.*;
import org.springframework.jdbc.core.*;
import org.springframework.beans.factory.annotation.*;
import java.util.*;

public class OrgTypeRoleRelDao extends BaseHibernateDao<OrgTypeRoleRelation, Integer>
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public OrgTypeRoleRelDao() {
        this.jdbcTemplate = null;
    }
    
    public List<Integer> removeRelationships(final List<Integer> idList, final int type) {
        String input = "";
        String query = "";
        for (final Integer id : idList) {
            input = input + id + ",";
        }
        input = input.substring(0, input.length() - 1);
        switch (type) {
            case 1: {
                query = "select USR_OTR_PK_ID from USR_MAP_ORGTYPE_ROLE where SMR_FK_ID in (" + input + ")";
                break;
            }
            case 2: {
                query = "select USR_OTR_PK_ID from USR_MAP_ORGTYPE_ROLE where OMS_FK_ID in (" + input + ")";
                break;
            }
        }
        final List<Integer> ids = (List<Integer>)this.jdbcTemplate.queryForList(query, (Class)Integer.class);
        if (ids != null && !ids.isEmpty()) {
            input = "";
            for (final Integer id2 : ids) {
                input = input + id2 + ",";
            }
            input = input.substring(0, input.length() - 1);
            final String queryString = "update USR_MAP_ORGTYPE_ROLE set IS_DELETED = 1 where USR_OTR_PK_ID in (" + input + ")";
            this.jdbcTemplate.update(queryString);
        }
        return ids;
    }
}
