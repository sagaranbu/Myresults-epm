package com.kpisoft.user.dac.impl.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.user.dac.impl.entity.*;
import org.springframework.jdbc.core.*;
import org.springframework.beans.factory.annotation.*;
import java.util.*;

public class OrgUserRoleRelDao extends BaseHibernateDao<OrgUserRoleRelation, Integer>
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public OrgUserRoleRelDao() {
        this.jdbcTemplate = null;
    }
    
    public List<Integer> removeRelations(final List<Integer> idList, final int type) {
        String query = "";
        String input = "";
        for (final Integer id : idList) {
            input = input + id + ",";
        }
        input = input.substring(0, input.length() - 1);
        switch (type) {
            case 1: {
                query = "select USR_UOR_PK_ID from USR_MAP_USER_ORG_ROLE where UDU_FK_ID in (" + input + ")";
                break;
            }
            case 2: {
                query = "select USR_UOR_PK_ID from USR_MAP_USER_ORG_ROLE where SMR_FK_ID in (" + input + ")";
                break;
            }
            case 3: {
                query = "select USR_UOR_PK_ID from USR_MAP_USER_ORG_ROLE where ODO_FK_ID in (" + input + ")";
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
            final String queryString = "update USR_MAP_USER_ORG_ROLE set IS_DELETED = 1 where USR_UOR_PK_ID in (" + input + ")";
            this.jdbcTemplate.update(queryString);
        }
        return ids;
    }
}
