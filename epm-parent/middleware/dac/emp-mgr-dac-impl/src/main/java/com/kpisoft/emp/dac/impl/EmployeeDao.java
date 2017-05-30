package com.kpisoft.emp.dac.impl;

import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.jdbc.core.*;
import com.kpisoft.emp.vo.*;
import org.hibernate.*;
import com.canopus.mw.dto.*;
import java.util.*;
import java.sql.*;

public class EmployeeDao extends BaseHibernateDao<Employee, Integer>
{
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public EmployeeDao() {
        this.jdbcTemplate = null;
    }
    
    public Employee merge(final Employee emp) {
        if (emp.getId() != null && emp.getId() > 0) {
            return (Employee)super._merge((Object)emp);
        }
        super._save((Object)emp);
        return emp;
    }
    
    public List<EmployeeSupervisorRelationshipData> getSupervisorRelationships() {
        final Calendar date = Calendar.getInstance();
        date.set(10, 11);
        date.set(12, 59);
        date.set(13, 59);
        date.set(14, 999);
        date.set(9, 1);
        final Timestamp timestamp = new Timestamp(date.getTimeInMillis());
        final String sql = "SELECT EDE_PK_EMP_REL_ID, EDE_PK_EMPLOYEE_ID, EDE_AT_SUPID, EDE_IS_PRIMARY, IS_DELETED, START_DATE, END_DATE FROM EMP_REL_EMP_SUPERVISOR WHERE (IS_DELETED = 0 OR IS_DELETED IS NULL) AND START_DATE <= ? AND (END_DATE >= ? OR END_DATE IS NULL)";
        final List<EmployeeSupervisorRelationshipData> supRelData = (List<EmployeeSupervisorRelationshipData>)this.jdbcTemplate.query(sql, new Object[] { timestamp, timestamp }, (RowMapper)new EmpSupervisorRowMapper());
        return supRelData;
    }
    
    public List<EmployeeSupervisorRelationshipData> getAllSupervisorRelationships() {
        final String sql = "SELECT EDE_PK_EMP_REL_ID, EDE_PK_EMPLOYEE_ID, EDE_AT_SUPID, EDE_IS_PRIMARY, IS_DELETED, START_DATE, END_DATE FROM EMP_REL_EMP_SUPERVISOR WHERE (IS_DELETED = 0 OR IS_DELETED IS NULL)";
        final List<EmployeeSupervisorRelationshipData> supRelData = (List<EmployeeSupervisorRelationshipData>)this.jdbcTemplate.query(sql, (RowMapper)new EmpSupervisorRowMapper());
        return supRelData;
    }
    
    public List<EmployeeOrgRelationshipData> getEmployeeOrganizationRelationships() {
        final Calendar date = Calendar.getInstance();
        date.set(10, 11);
        date.set(12, 59);
        date.set(13, 59);
        date.set(14, 999);
        date.set(9, 1);
        final Timestamp timestamp = new Timestamp(date.getTimeInMillis());
        final String sql = "SELECT ORE_PK_ID, ODO_FK_ID, EDE_PK_EMPLOYEE_ID, TYPE, IS_HOD, IS_DELETED, START_DATE, END_DATE FROM ORG_REL_EMP_ORG_REL WHERE IS_DELETED = 0 OR IS_DELETED IS NULL AND START_DATE <= ? AND (END_DATE >= ? OR END_DATE IS NULL)";
        final List<EmployeeOrgRelationshipData> result = (List<EmployeeOrgRelationshipData>)this.jdbcTemplate.query(sql, new Object[] { timestamp, timestamp }, (RowMapper)new EmpOrgRelRowMapper());
        return result;
    }
    
    public List<EmployeeOrgRelationshipData> getAllEmployeeOrganizationRelationships() {
        final String sql = "SELECT ORE_PK_ID, ODO_FK_ID, EDE_PK_EMPLOYEE_ID, TYPE, IS_HOD, IS_DELETED, START_DATE, END_DATE FROM ORG_REL_EMP_ORG_REL WHERE IS_DELETED = 0 OR IS_DELETED IS NULL";
        final List<EmployeeOrgRelationshipData> result = (List<EmployeeOrgRelationshipData>)this.jdbcTemplate.query(sql, (RowMapper)new EmpOrgRelRowMapper());
        return result;
    }
    
    public List<Object[]> getUsersIdEmailFromEmployeeIdList(final List<Integer> employeIds) {
        final Session session = this.getSession();
        final Query query = session.createQuery("SELECT userBase.id, userBase.employeeId, userBase.email FROM EmployeeBase as empBase, UserBase as userBase WHERE empBase.id = userBase.employeeId and empBase.id IN (:employeIds)");
        query.setParameterList("employeIds", (Collection)employeIds);
        final List<Object[]> results = (List<Object[]>)query.list();
        return results;
    }
    
    public List<EmployeeBase> getEmployeeBaseByIds(final List<Integer> employeIds) {
        final Session session = this.getSession();
        final Query query = session.createQuery("FROM EmployeeBase empBase WHERE empBase.id IN (:employeIds)");
        query.setParameterList("employeIds", (Collection)employeIds);
        final List<EmployeeBase> results = (List<EmployeeBase>)query.list();
        return results;
    }
    
    public List<EmployeeBase> getEmployeesForOrgId(final List<Integer> orgIds) {
        final Calendar date = Calendar.getInstance();
        date.set(10, 11);
        date.set(12, 59);
        date.set(13, 59);
        date.set(14, 999);
        date.set(9, 1);
        final Session session = this.getSession();
        final String sql = "SELECT base FROM EmployeeBase base, EmployeeOrganizationRelationship rel WHERE rel.organizationId in (:orgId) and rel.employeeId = base.id and base.tenantId = :tenantId and base.startDate <= :date and (base.endDate = null or base.endDate >= :date) and rel.startDate <= :date and (rel.endDate = null or base.endDate >= :date)";
        final Query query = session.createQuery(sql);
        query.setParameterList("orgId", (Collection)orgIds);
        query.setInteger("tenantId", (int)ExecutionContext.getTenantId());
        query.setTimestamp("date", new Timestamp(date.getTimeInMillis()));
        final List<EmployeeBase> result = (List<EmployeeBase>)query.list();
        return result;
    }
    
    private class EmpSupervisorRowMapper implements RowMapper<EmployeeSupervisorRelationshipData>
    {
        public EmployeeSupervisorRelationshipData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final EmployeeSupervisorRelationshipData data = new EmployeeSupervisorRelationshipData();
            data.setId(rs.getInt("EDE_PK_EMP_REL_ID"));
            data.setEmployeeId(rs.getInt("EDE_PK_EMPLOYEE_ID"));
            data.setSupervisorId(rs.getInt("EDE_AT_SUPID"));
            data.setPrimary(rs.getBoolean("EDE_IS_PRIMARY"));
            data.setStartDate(rs.getTimestamp("START_DATE"));
            data.setEndDate(rs.getTimestamp("END_DATE"));
            return data;
        }
    }
    
    private class EmpOrgRelRowMapper implements RowMapper<EmployeeOrgRelationshipData>
    {
        public EmployeeOrgRelationshipData mapRow(final ResultSet rs, final int rowNum) throws SQLException {
            final EmployeeOrgRelationshipData data = new EmployeeOrgRelationshipData();
            data.setId(rs.getInt("ORE_PK_ID"));
            data.setOrganizationId(rs.getInt("ODO_FK_ID"));
            data.setEmployeeId(rs.getInt("EDE_PK_EMPLOYEE_ID"));
            data.setType(rs.getInt("TYPE"));
            data.setHod(rs.getBoolean("IS_HOD"));
            data.setStartDate(rs.getTimestamp("START_DATE"));
            data.setEndDate(rs.getTimestamp("END_DATE"));
            return data;
        }
    }
}
