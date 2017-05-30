package com.kpisoft.kpi.dac.dao;

import com.canopus.dac.hibernate.*;
import com.kpisoft.kpi.dac.impl.domain.*;
import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import org.hibernate.*;
import com.kpisoft.kpi.utility.*;
import java.util.*;
import java.math.*;
import com.canopus.mw.dto.*;
import org.perf4j.*;
import org.springframework.jdbc.core.*;
import com.kpisoft.kpi.vo.*;
import java.sql.*;

public class KpiDao extends BaseHibernateDao<Kpi, Integer>
{
    private static Logger logger;
    @Value("${kpigraph.fetchsize:10000}")
    private Integer fetchSize;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public KpiDao() {
        this.fetchSize = 10000;
        this.jdbcTemplate = null;
    }
    
    public Kpi merge(final Kpi kpi) {
        if (kpi.getId() != null && kpi.getId() > 0) {
            return (Kpi)super._merge((Object)kpi);
        }
        super.save(kpi);
        return kpi;
    }
    
    public int updateKpiState(final List<Integer> kpiIdList, final Integer state) {
        final Session session = this.getSession();
        final Query query = session.createQuery("update Kpi as kpi set kpi.state = :state where kpi.id IN (:ids)");
        query.setInteger("state", (int)state);
        query.setParameterList("ids", (Collection)kpiIdList);
        final int result = query.executeUpdate();
        return result;
    }
    
    public int updateKpiStatus(final List<Integer> kpiIdList, final Integer status) {
        final Session session = this.getSession();
        final Query query = session.createQuery("update Kpi as kpi set kpi.status = :status where kpi.id IN (:ids)");
        query.setInteger("status", (int)status);
        query.setParameterList("ids", (Collection)kpiIdList);
        final int result = query.executeUpdate();
        return result;
    }
    
    public int updateKpiDetailsForMultiple(final List<KpiUtility> kpiIdList) {
        int result = 0;
        final Session session = this.getSession();
        for (final KpiUtility util : kpiIdList) {
            final Query query = session.createQuery("update Kpi as kpi set kpi.weightage = :weightage where kpi.id = :id )");
            query.setFloat("weightage", util.getWeightage());
            query.setInteger("id", (int)util.getId());
            result += query.executeUpdate();
        }
        return result;
    }
    
    public int saveScoreDetailsInKpiForMultiple(final List<KpiScoreBean> kpiUtilityList) {
        int result = 0;
        final Session session = this.getSession();
        for (final KpiScoreBean util : kpiUtilityList) {
            final Query query = session.createQuery("update Kpi as kpi set kpi.numTarget = :numTarget, kpi.dateTarget = :dateTarget,  kpi.currRatingLevel = :currRatingLevel, kpi.currAchievement = :currAchievement, kpi.currDateScore = :currDateScore, kpi.currScore = :currScore, kpi.currPeriodId = :currPeriodId where kpi.id = :id ");
            if (util.getNumTarget() != null) {
                query.setDouble("numTarget", (double)util.getNumTarget());
            }
            else {
                query.setBigInteger("numTarget", (BigInteger)null);
            }
            if (util.getAchievement() != null) {
                query.setDouble("currAchievement", (double)util.getAchievement());
            }
            else {
                query.setBigInteger("currAchievement", (BigInteger)null);
            }
            if (util.getNumScore() != null) {
                query.setDouble("currScore", (double)util.getNumScore());
            }
            else {
                query.setBigInteger("currScore", (BigInteger)null);
            }
            if (util.getRatingLevel() != null) {
                query.setInteger("currRatingLevel", (int)util.getRatingLevel());
            }
            else {
                query.setBigInteger("currRatingLevel", (BigInteger)null);
            }
            if (util.getPeriodMasterId() != null) {
                query.setInteger("currPeriodId", (int)util.getPeriodMasterId());
            }
            else {
                query.setBigInteger("currPeriodId", (BigInteger)null);
            }
            query.setDate("dateTarget", util.getDateTarget());
            query.setDate("currDateScore", util.getDateScore());
            query.setInteger("id", (int)util.getKpiId());
            result += query.executeUpdate();
        }
        return result;
    }
    
    public List<Object[]> getKpiDataToCalcScore(final Integer scorecardId, final Identifier groupId, final Identifier periodMasterId, final DateResponse startDate, final DateResponse endDate) {
        final Session session = this.getSession();
        String append = "";
        if (groupId != null && groupId.getId() != null && groupId.getId() > 0) {
            append = " and kpi.groupId = " + groupId.getId();
        }
        if (periodMasterId != null && periodMasterId.getId() != null && periodMasterId.getId() > 0) {
            append = append + " and ks.periodMasterId = " + periodMasterId.getId();
        }
        if (startDate != null && startDate.getDate() != null) {
            append += " and ks.startDate >= :startDate";
        }
        if (endDate != null && endDate.getDate() != null) {
            append += " AND ks.endDate <= :endDate";
        }
        final Query query = session.createQuery("select kpi.id , ks.id , kpi.weightage, ks.ratingLevel , kpi.groupId , kpi.groupKpi , ks.achievement from Kpi as kpi, KpiScore ks, KpiScorecardRelationship as kpiscrel where kpi.id = kpiscrel.kpiId and kpi.id = ks.kpi.id and kpiscrel.empScorecardId =  " + scorecardId + append);
        if (startDate != null && startDate.getDate() != null) {
            query.setDate("startDate", startDate.getDate());
        }
        if (endDate != null && endDate.getDate() != null) {
            query.setDate("endDate", endDate.getDate());
        }
        final List<Object[]> results = (List<Object[]>)query.list();
        return results;
    }
    
    public List<KpiKpiGraphRelationshipBean> getAllKpiRelationships() {
        if (this.fetchSize == null) {
            this.jdbcTemplate.setFetchSize(10000);
        }
        final StopWatch sw = new StopWatch("get-kpigraph");
        sw.start();
        final String sql = "select kgr.KKG_PK_ID as KKG_PK_ID, kgr.LAST_MODIFIED_BY as LAST_MODIFIED_BY, kgr.LAST_MODIFIED_ON as LAST_MODIFIED_ON,  kgr.TENANT_ID as TENANT_ID, kgr.CHILD_ID as CHILD_ID, kgr.IS_DELETED as IS_DELETED, kgr.PARENT_ID as PARENT_ID, kgr.TYPE as TYPE,  kgr.WEIGHTAGE as WEIGHTAGE,  ki1.KDB_PK_ID as C_CHILD_ID, ki1.CODE as CHILD_CODE, ki1.NAME as CHILD_NAME, ki1.WEIGHTAGE as CHILD_WEIGHTAGE,  ki2.KDB_PK_ID as P_PARENT_ID, ki2.CODE as PARENT_CODE, ki2.NAME as PARENT_NAME, ki2.WEIGHTAGE as PARENT_WEIGHTAGE from KPI_REL_KPI_GRAPH kgr left outer join KPI_DET_BASE ki1 on kgr.CHILD_ID=ki1.KDB_PK_ID inner join KPI_DET_BASE ki2 on  kgr.PARENT_ID=ki2.KDB_PK_ID where ( kgr.IS_DELETED = 0 OR kgr.IS_DELETED IS NULL)";
        final List<KpiKpiGraphRelationshipBean> kpiKpiRelData = (List<KpiKpiGraphRelationshipBean>)this.jdbcTemplate.query(sql, (RowMapper)new kpiKpiGraphRowMapper());
        sw.stop();
        KpiDao.logger.info((Object)("DAC - Kpi Kpi Graph Load time:" + sw.getElapsedTime()));
        return kpiKpiRelData;
    }
    
    static {
        KpiDao.logger = Logger.getLogger((Class)KpiDao.class);
    }
    
    private class kpiKpiGraphRowMapper implements RowMapper<KpiKpiGraphRelationshipBean>
    {
        public KpiKpiGraphRelationshipBean mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final KpiKpiGraphRelationshipBean data = new KpiKpiGraphRelationshipBean();
            data.setId(rs.getInt("KKG_PK_ID"));
            data.setType(rs.getString("TYPE"));
            data.setWeightage(rs.getFloat("WEIGHTAGE"));
            data.setParent(new KpiIdentityBean());
            data.getParent().setId(rs.getInt("PARENT_ID"));
            data.getParent().setCode(rs.getString("PARENT_CODE"));
            data.getParent().setName(rs.getString("PARENT_NAME"));
            data.getParent().setWeightage(rs.getFloat("PARENT_WEIGHTAGE"));
            data.setChild(new KpiIdentityBean());
            data.getChild().setId(rs.getInt("CHILD_ID"));
            data.getChild().setCode(rs.getString("CHILD_CODE"));
            data.getChild().setName(rs.getString("CHILD_NAME"));
            data.getChild().setWeightage(rs.getFloat("CHILD_WEIGHTAGE"));
            return data;
        }
    }
}
