package com.kpisoft.emp.dac.impl;

import com.kpisoft.emp.dac.*;
import org.springframework.stereotype.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.user.dac.*;
import com.canopus.dac.hibernate.*;
import com.kpisoft.emp.vo.param.*;
import com.kpisoft.emp.utility.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import java.sql.*;
import com.googlecode.genericdao.search.*;
import com.canopus.dac.utils.*;
import org.modelmapper.*;
import java.lang.reflect.*;
import com.canopus.mw.*;
import com.kpisoft.emp.vo.*;
import com.canopus.mw.dto.*;
import java.util.*;
import org.slf4j.*;

@Component
public class EmployeeDataServiceImpl extends BaseDataAccessService implements EmployeeDataService
{
    @Autowired
    private EmployeeDao employeeDao;
    @Autowired
    private UserDataService userDataService;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public EmployeeDataServiceImpl() {
        this.employeeDao = null;
        this.userDataService = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)EmployeeData.class, (Class)Employee.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)EmployeeFieldData.class, (Class)com.kpisoft.emp.dac.impl.EmployeeFieldData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)EmployeeOrgRelationshipData.class, (Class)EmployeeOrganizationRelationship.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)EmployeePositionData.class, (Class)EmployeePositionRelationship.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)EmployeeSupervisorRelationshipData.class, (Class)EmployeeSupervisorRelationship.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional(readOnly = true)
    public Response getEmployee(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        Employee emp = null;
        try {
            emp = (Employee)this.employeeDao.find((Serializable)identifier.getId());
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
        if (emp == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_DOES_NOT_EXIST_001.name(), "Employee id {0} does not exist.", new Object[] { identifier.getId() }));
        }
        final EmployeeData empData = (EmployeeData)this.modelMapper.map((Object)emp, (Class)EmployeeData.class);
        return this.OK(EMPParams.EMP_DATA.name(), (BaseValueObject)empData);
    }
    
    @Transactional
    public Response saveEmployee(final Request request) {
        final EmployeeData empData = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        if (empData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        Employee emp = null;
        if (empData.getId() != null && empData.getId() > 0) {
            emp = (Employee)this.employeeDao.find((Serializable)empData.getId());
        }
        else {
            emp = new Employee();
        }
        String orgIds = "";
        String posIds = "";
        String supIds = "";
        try {
            this.modelMapper.map((Object)empData, (Object)emp);
            this.employeeDao.merge(emp);
            if (emp.getFieldData() != null && !emp.getFieldData().isEmpty()) {
                for (final com.kpisoft.emp.dac.impl.EmployeeFieldData obj : emp.getFieldData()) {
                    obj.setEmployeeId(emp.getId());
                }
            }
            if (emp.getEmpOrgRelData() != null && !emp.getEmpOrgRelData().isEmpty()) {
                for (final EmployeeOrganizationRelationship obj2 : emp.getEmpOrgRelData()) {
                    obj2.setEmployeeId(emp.getId());
                    orgIds = orgIds + obj2.getOrganizationId() + ",";
                }
                orgIds = orgIds.substring(0, orgIds.length() - 1);
                emp.setOrgIds(orgIds);
            }
            if (emp.getEmpSupData() != null && !emp.getEmpSupData().isEmpty()) {
                for (final EmployeeSupervisorRelationship obj3 : emp.getEmpSupData()) {
                    obj3.setEmployeeId(emp.getId());
                    supIds = supIds + obj3.getSupervisorId() + ",";
                }
                supIds = supIds.substring(0, supIds.length() - 1);
                emp.setSupervisorIds(supIds);
            }
            if (emp.getPosData() != null && !emp.getPosData().isEmpty()) {
                for (final EmployeePositionRelationship obj4 : emp.getPosData()) {
                    obj4.setEmployeeId(emp.getId());
                    posIds = posIds + obj4.getPositionId() + ",";
                }
                posIds = posIds.substring(0, posIds.length() - 1);
                emp.setPositionIds(posIds);
            }
            return this.OK(EMPParams.EMP_ID.name(), (BaseValueObject)new Identifier(emp.getId()));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - saveEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response deleteEmployee(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.EMP_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            this.employeeDao.removeById((Serializable)identifier.getId());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - deleteEmployee() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response deleteEmployeesByIds(final Request request) {
        final IdentifierList empIds = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        if (empIds == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            for (final Integer iterator : empIds.getIdsList()) {
                this.employeeDao.removeById((Serializable)iterator);
            }
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - deleteEmployeesByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendEmployeesByIds(final Request request) {
        final IdentifierList empIds = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        if (empIds == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final Search search = new Search();
            search.addFilterIn("id", (Collection)empIds.getIdsList());
            final List<Employee> employees = (List<Employee>)this.employeeDao.search((ISearch)search);
            for (final Employee iterator : employees) {
                iterator.setEndDate(endDate.getDate());
            }
            final Calendar endDateCalendar = Calendar.getInstance();
            endDateCalendar.setTime(endDate.getDate());
            endDateCalendar.set(10, 0);
            endDateCalendar.set(12, 0);
            endDateCalendar.set(13, 0);
            endDateCalendar.set(14, 0);
            final Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.set(10, 0);
            currentCalendar.set(12, 0);
            currentCalendar.set(13, 0);
            currentCalendar.set(14, 0);
            if (currentCalendar.getTime().equals(endDateCalendar.getTime())) {
                for (final Employee iterator2 : employees) {
                    iterator2.setStatus(0);
                }
            }
            this.genericDao.save(employees.toArray());
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - suspendEmployeesByIds() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_SUSPEND_BY_IDS_009.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final EmployeeData employeeData = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        if (employeeData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "firstName";
        try {
            final Employee employee = (Employee)this.modelMapper.map((Object)employeeData, (Class)Employee.class);
            employee.setDeleted(false);
            employee.setStatus(1);
            employee.setStartDate(null);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.employeeDao.getFilterFromExample(employee, options);
            final Calendar date = Calendar.getInstance();
            date.set(10, 11);
            date.set(12, 59);
            date.set(13, 59);
            date.set(14, 999);
            date.set(9, 1);
            final Search search = new Search((Class)EmployeeBase.class);
            search.addFilter(filter);
            search.addFilterEqual("deleted", (Object)false);
            search.addFilterLessOrEqual("startDate", (Object)new Timestamp(date.getTimeInMillis()));
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(date.getTimeInMillis())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<EmployeeBase> result = (List<EmployeeBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<EmployeeData>>() {}.getType();
            final List<EmployeeData> employeesList = (List<EmployeeData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)employeesList);
            final Response response = this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - search() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchAll(final Request request) {
        final EmployeeData employeeData = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        if (employeeData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "firstName";
        try {
            final Employee employee = (Employee)this.modelMapper.map((Object)employeeData, (Class)Employee.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.employeeDao.getFilterFromExample(employee, options);
            final Search search = new Search((Class)EmployeeBase.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<EmployeeBase> result = (List<EmployeeBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<EmployeeData>>() {}.getType();
            final List<EmployeeData> employeesList = (List<EmployeeData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)employeesList);
            final Response response = this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - searchAll() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_SEARCH_008.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response addSupervisor(final Request request) {
        final EmployeeSupervisorRelationshipData data = (EmployeeSupervisorRelationshipData)request.get(EMPParams.EMP_SUP_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final Calendar date = Calendar.getInstance();
        date.set(10, 11);
        date.set(12, 59);
        date.set(13, 59);
        date.set(14, 999);
        date.set(9, 1);
        try {
            final Search search = new Search((Class)EmployeeSupervisorRelationship.class);
            search.addFilterEqual("employeeId", (Object)data.getEmployeeId());
            search.addFilterEqual("primary", (Object)true);
            search.addFilterLessOrEqual("startDate", (Object)new Timestamp(date.getTimeInMillis()));
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(date.getTimeInMillis())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            final EmployeeSupervisorRelationship relData = (EmployeeSupervisorRelationship)this.genericDao.searchUnique((ISearch)search);
            if (relData == null) {
                data.setPrimary(true);
            }
            else {
                data.setPrimary(false);
            }
            final EmployeeSupervisorRelationship rel = (EmployeeSupervisorRelationship)this.modelMapper.map((Object)data, (Class)EmployeeSupervisorRelationship.class);
            this.genericDao.save((Object)rel);
            final Employee employee = (Employee)this.employeeDao.find((Serializable)data.getEmployeeId());
            String supIds = employee.getSupervisorIds();
            if (supIds == null || supIds.trim().isEmpty()) {
                supIds = data.getSupervisorId() + "";
            }
            else {
                supIds = supIds + "," + data.getSupervisorId();
            }
            employee.setSupervisorIds(supIds);
            this.employeeDao.merge(employee);
            return this.OK(EMPParams.EMP_SUP_REL_ID.name(), (BaseValueObject)new Identifier(rel.getId()));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - addSupervisor() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response setPrimarySupervisor(final Request request) {
        final Identifier subEmpId = (Identifier)request.get(EMPParams.SUB_EMP_ID.name());
        final Identifier superEmpId = (Identifier)request.get(EMPParams.SUP_EMP_ID.name());
        if (subEmpId == null || superEmpId == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final Calendar date = Calendar.getInstance();
        date.set(10, 11);
        date.set(12, 59);
        date.set(13, 59);
        date.set(14, 999);
        date.set(9, 1);
        try {
            Search search = new Search((Class)EmployeeSupervisorRelationship.class);
            search.addFilterEqual("employeeId", (Object)subEmpId.getId());
            search.addFilterEqual("supervisorId", (Object)superEmpId.getId());
            final EmployeeSupervisorRelationship data = (EmployeeSupervisorRelationship)this.genericDao.searchUnique((ISearch)search);
            if (data == null) {
                return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
            }
            search = new Search((Class)EmployeeSupervisorRelationship.class);
            search.addFilterEqual("employeeId", (Object)subEmpId.getId());
            search.addFilterEqual("primary", (Object)true);
            search.addFilterLessOrEqual("startDate", (Object)new Timestamp(date.getTimeInMillis()));
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(date.getTimeInMillis())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            final List<EmployeeSupervisorRelationship> relData = (List<EmployeeSupervisorRelationship>)this.genericDao.search((ISearch)search);
            for (final EmployeeSupervisorRelationship iterator : relData) {
                iterator.setPrimary(false);
                this.genericDao.save((Object)iterator);
            }
            data.setPrimary(true);
            this.genericDao.save((Object)data);
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - setPrimarySupervisor() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeSupervisor(final Request request) {
        final EmployeeSupervisorRelationshipData data = (EmployeeSupervisorRelationshipData)request.get(EMPParams.EMP_SUP_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final EmployeeSupervisorRelationship rel = (EmployeeSupervisorRelationship)this.genericDao.find((Class)EmployeeSupervisorRelationship.class, (Serializable)data.getId());
            final boolean status = this.genericDao.removeById((Class)EmployeeSupervisorRelationship.class, (Serializable)data.getId());
            if (status) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)rel.getEmployeeId());
                if (employee.getSupervisorIds() != null && !employee.getSupervisorIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] supIds = split = employee.getSupervisorIds().split(",");
                    for (final String iterator : split) {
                        if (!iterator.equals(rel.getSupervisorId() + "")) {
                            ids = ids + iterator + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setSupervisorIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - removeSupervisor() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendSupervisorRelationships(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(EMPParams.EMP_SUP_REL_DATA_LIST.name());
        if (list == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final List<EmployeeSupervisorRelationshipData> relList = (List<EmployeeSupervisorRelationshipData>)list.getValueObjectList();
        try {
            final List<EmployeeSupervisorRelationship> relations = new ArrayList<EmployeeSupervisorRelationship>();
            for (final EmployeeSupervisorRelationshipData iterator : relList) {
                relations.add((EmployeeSupervisorRelationship)this.modelMapper.map((Object)iterator, (Class)EmployeeSupervisorRelationship.class));
            }
            this.genericDao.save(relations.toArray());
            for (final EmployeeSupervisorRelationship iterator2 : relations) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)iterator2.getEmployeeId());
                if (employee.getSupervisorIds() != null && !employee.getSupervisorIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] supIds = split = employee.getSupervisorIds().split(",");
                    for (final String id : split) {
                        if (!id.equals(iterator2.getSupervisorId() + "")) {
                            ids = ids + id + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setSupervisorIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - suspendSupervisorRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_SUSPEND_SUP_RELS_013.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendSupervisorRelationshipsByEmployeeIds(final Request request) {
        final IdentifierList list = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        BooleanResponse delete = (BooleanResponse)request.get(EMPParams.BOOLEAN_RESPONSE.name());
        if (list == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        if (delete == null) {
            delete = new BooleanResponse(false);
        }
        final List<EmployeeSupervisorRelationship> result = new ArrayList<EmployeeSupervisorRelationship>();
        final List<EmployeeSupervisorRelationshipData> data = new ArrayList<EmployeeSupervisorRelationshipData>();
        try {
            Search search = new Search((Class)EmployeeSupervisorRelationship.class);
            search.addFilterIn("employeeId", (Collection)list.getIdsList());
            List<EmployeeSupervisorRelationship> relations = (List<EmployeeSupervisorRelationship>)this.genericDao.search((ISearch)search);
            for (final EmployeeSupervisorRelationship iterator : relations) {
                iterator.setEndDate(endDate.getDate());
                iterator.setDeleted(delete.isResponse());
            }
            this.genericDao.save(relations.toArray());
            result.addAll(relations);
            search = new Search((Class)EmployeeSupervisorRelationship.class);
            search.addFilterIn("supervisorId", (Collection)list.getIdsList());
            relations = (List<EmployeeSupervisorRelationship>)this.genericDao.search((ISearch)search);
            for (final EmployeeSupervisorRelationship iterator : relations) {
                iterator.setEndDate(endDate.getDate());
                iterator.setDeleted(delete.isResponse());
            }
            this.genericDao.save(relations.toArray());
            result.addAll(relations);
            for (final EmployeeSupervisorRelationship iterator : result) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)iterator.getEmployeeId());
                if (employee.getSupervisorIds() != null && !employee.getSupervisorIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] supIds = split = employee.getSupervisorIds().split(",");
                    for (final String id : split) {
                        if (!id.equals(iterator.getSupervisorId() + "")) {
                            ids = ids + id + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setSupervisorIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            for (final EmployeeSupervisorRelationship iterator : result) {
                data.add((EmployeeSupervisorRelationshipData)this.modelMapper.map((Object)iterator, (Class)EmployeeSupervisorRelationshipData.class));
            }
            final BaseValueObjectList baseValueObjectlist = new BaseValueObjectList();
            baseValueObjectlist.setValueObjectList((List)data);
            final Response response = this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(EMPParams.EMP_SUP_REL_DATA_LIST.name(), (BaseValueObject)baseValueObjectlist);
            return response;
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - suspendSupervisorRelationshipsByEmployeeIds() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_SUSPEND_SUP_RELS_BY_EMP_IDS_012.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getSupervisorRelationships(final Request request) {
        try {
            final List<EmployeeSupervisorRelationshipData> result = this.employeeDao.getSupervisorRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_SUP_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getSupervisorRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_GET_SUP_RELS_011.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllSupervisorRelationships(final Request request) {
        try {
            final List<EmployeeSupervisorRelationshipData> result = this.employeeDao.getAllSupervisorRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_SUP_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getAllSupervisorRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_GET_ALL_SUPS_010.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response addEmployeePosition(final Request request) {
        final EmployeePositionData data = (EmployeePositionData)request.get(EMPParams.EMP_POSITION_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final EmployeePositionRelationship rel = (EmployeePositionRelationship)this.modelMapper.map((Object)data, (Class)EmployeePositionRelationship.class);
            this.genericDao.save((Object)rel);
            final Employee employee = (Employee)this.employeeDao.find((Serializable)data.getEmployeeId());
            String posIds = employee.getSupervisorIds();
            if (posIds == null || posIds.trim().isEmpty()) {
                posIds = data.getPositionId() + "";
            }
            else {
                posIds = posIds + "," + data.getPositionId();
            }
            employee.setPositionIds(posIds);
            this.employeeDao.merge(employee);
            return this.OK(EMPParams.EMP_POSITION_ID.name(), (BaseValueObject)new Identifier(rel.getId()));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - addEmployeePosition() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeEmployeePosition(final Request request) {
        final EmployeePositionData data = (EmployeePositionData)request.get(EMPParams.EMP_POSITION_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final EmployeePositionRelationship rel = (EmployeePositionRelationship)this.genericDao.find((Class)EmployeePositionRelationship.class, (Serializable)data.getId());
            final boolean status = this.genericDao.remove((Object)rel);
            if (status) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)rel.getEmployeeId());
                if (employee.getPositionIds() != null && !employee.getPositionIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] posIds = split = employee.getPositionIds().split(",");
                    for (final String iterator : split) {
                        if (!iterator.equals(rel.getPositionId() + "")) {
                            ids = ids + iterator + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setPositionIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - removeEmployeePosition() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEmployeePositionRelationships(final Request request) {
        final Calendar date = Calendar.getInstance();
        date.set(10, 11);
        date.set(12, 59);
        date.set(13, 59);
        date.set(14, 999);
        date.set(9, 1);
        try {
            final Search search = new Search((Class)EmployeePositionRelationship.class);
            search.addFilterLessOrEqual("startDate", (Object)new Timestamp(date.getTimeInMillis()));
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(date.getTimeInMillis())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            final List<EmployeePositionRelationship> data = (List<EmployeePositionRelationship>)this.genericDao.search((ISearch)search);
            final List<EmployeePositionData> result = new ArrayList<EmployeePositionData>();
            for (final EmployeePositionRelationship iterator : data) {
                result.add((EmployeePositionData)this.modelMapper.map((Object)iterator, (Class)EmployeePositionData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_POSITION_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getEmployeePositionRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllEmployeePositionRelationships(final Request request) {
        try {
            final List<EmployeePositionRelationship> data = (List<EmployeePositionRelationship>)this.genericDao.findAll((Class)EmployeePositionRelationship.class);
            final List<EmployeePositionData> result = new ArrayList<EmployeePositionData>();
            for (final EmployeePositionRelationship iterator : data) {
                result.add((EmployeePositionData)this.modelMapper.map((Object)iterator, (Class)EmployeePositionData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_POSITION_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getAllEmployeePositionRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendEmpPositionRelationships(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(EMPParams.EMP_POSITION_DATA_LIST.name());
        if (list == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<EmployeePositionData> data = (List<EmployeePositionData>)list.getValueObjectList();
            final List<EmployeePositionRelationship> relData = new ArrayList<EmployeePositionRelationship>();
            for (final EmployeePositionData iterator : data) {
                relData.add((EmployeePositionRelationship)this.modelMapper.map((Object)iterator, (Class)EmployeePositionRelationship.class));
            }
            this.genericDao.save(relData.toArray());
            for (final EmployeePositionRelationship iterator2 : relData) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)iterator2.getEmployeeId());
                if (employee.getPositionIds() != null && !employee.getPositionIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] posIds = split = employee.getPositionIds().split(",");
                    for (final String id : split) {
                        if (!id.equals(iterator2.getPositionId() + "")) {
                            ids = ids + id + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setPositionIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getAllEmployeePositionRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendEmpPositionRelationshipsByEmployeeIds(final Request request) {
        final IdentifierList list = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        BooleanResponse delete = (BooleanResponse)request.get(EMPParams.BOOLEAN_RESPONSE.name());
        if (list == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        if (delete == null) {
            delete = new BooleanResponse(false);
        }
        final List<EmployeePositionData> data = new ArrayList<EmployeePositionData>();
        try {
            final Search search = new Search((Class)EmployeePositionRelationship.class);
            search.addFilterIn("employeeId", (Collection)list.getIdsList());
            final List<EmployeePositionRelationship> relations = (List<EmployeePositionRelationship>)this.genericDao.search((ISearch)search);
            for (final EmployeePositionRelationship iterator : relations) {
                iterator.setEndDate(endDate.getDate());
                iterator.setDeleted(delete.isResponse());
            }
            this.genericDao.save(relations.toArray());
            for (final EmployeePositionRelationship iterator : relations) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)iterator.getEmployeeId());
                if (employee.getPositionIds() != null && !employee.getPositionIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] posIds = split = employee.getPositionIds().split(",");
                    for (final String id : split) {
                        if (!id.equals(iterator.getPositionId() + "")) {
                            ids = ids + id + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setPositionIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            for (final EmployeePositionRelationship iterator : relations) {
                data.add((EmployeePositionData)this.modelMapper.map((Object)iterator, (Class)EmployeePositionData.class));
            }
            final BaseValueObjectList objectList = new BaseValueObjectList();
            objectList.setValueObjectList((List)data);
            final Response response = this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(EMPParams.EMP_POSITION_DATA_LIST.name(), (BaseValueObject)objectList);
            return response;
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - suspendEmpPositionRelationshipsByEmployeeIds() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response addEmployeeOrganization(final Request request) {
        final EmployeeOrgRelationshipData data = (EmployeeOrgRelationshipData)request.get(EMPParams.EMP_ORG_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final EmployeeOrganizationRelationship rel = (EmployeeOrganizationRelationship)this.modelMapper.map((Object)data, (Class)EmployeeOrganizationRelationship.class);
            this.genericDao.save((Object)rel);
            final Employee employee = (Employee)this.employeeDao.find((Serializable)data.getEmployeeId());
            String orgIds = employee.getSupervisorIds();
            if (orgIds == null || orgIds.trim().isEmpty()) {
                orgIds = data.getOrganizationId() + "";
            }
            else {
                orgIds = orgIds + "," + data.getOrganizationId();
            }
            employee.setOrgIds(orgIds);
            this.employeeDao.merge(employee);
            return this.OK(EMPParams.EMP_ORG_REL_ID.name(), (BaseValueObject)new Identifier(rel.getId()));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - addEmployeeOrganization() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_ADD_EMP_TO_ORG_014.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeEmployeeOrganization(final Request request) {
        final EmployeeOrgRelationshipData data = (EmployeeOrgRelationshipData)request.get(EMPParams.EMP_ORG_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final EmployeeOrganizationRelationship rel = (EmployeeOrganizationRelationship)this.genericDao.find((Class)EmployeeOrganizationRelationship.class, (Serializable)data.getId());
            final boolean status = this.genericDao.remove((Object)rel);
            if (status) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)rel.getEmployeeId());
                if (employee.getOrgIds() != null && !employee.getOrgIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] orgIds = split = employee.getOrgIds().split(",");
                    for (final String iterator : split) {
                        if (!iterator.equals(rel.getOrganizationId() + "")) {
                            ids = ids + iterator + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setOrgIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - removeEmployeeOrganization() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_REM_EMP_FROM_ORG_015.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEmployeeOrganizationRelationships(final Request request) {
        try {
            final List<EmployeeOrgRelationshipData> result = this.employeeDao.getEmployeeOrganizationRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getEmployeeOrganizationRelationships() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_GET_EMP_ORG_RELS_019.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllEmployeeOrganizationRelationships(final Request request) {
        try {
            final List<EmployeeOrgRelationshipData> result = this.employeeDao.getAllEmployeeOrganizationRelationships();
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(EMPParams.EMP_ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getAllEmployeeOrganizationRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_GET_ALL_EMP_ORG_RELS_016.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendEmpOrgRelationships(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(EMPParams.EMP_ORG_REL_DATA_LIST.name());
        if (list == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final List<EmployeeOrgRelationshipData> data = (List<EmployeeOrgRelationshipData>)list.getValueObjectList();
            final List<EmployeeOrganizationRelationship> result = new ArrayList<EmployeeOrganizationRelationship>();
            for (final EmployeeOrgRelationshipData iterator : data) {
                result.add((EmployeeOrganizationRelationship)this.modelMapper.map((Object)iterator, (Class)EmployeeOrganizationRelationship.class));
            }
            this.genericDao.save(result.toArray());
            for (final EmployeeOrganizationRelationship iterator2 : result) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)iterator2.getEmployeeId());
                if (employee.getOrgIds() != null && !employee.getOrgIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] orgIds = split = employee.getOrgIds().split(",");
                    for (final String id : split) {
                        if (!id.equals(iterator2.getOrganizationId() + "")) {
                            ids = ids + id + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setOrgIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - suspendEmpOrgRelationships() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_SUSPEND_EMP_ORG_RELS_017.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendEmpOrgRelationshipsByEmployeeIds(final Request request) {
        final IdentifierList list = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(EMPParams.END_DATE.name());
        BooleanResponse delete = (BooleanResponse)request.get(EMPParams.BOOLEAN_RESPONSE.name());
        if (list == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        if (delete == null) {
            delete = new BooleanResponse(false);
        }
        final List<EmployeeOrgRelationshipData> data = new ArrayList<EmployeeOrgRelationshipData>();
        try {
            final Search search = new Search((Class)EmployeeOrganizationRelationship.class);
            search.addFilterIn("employeeId", (Collection)list.getIdsList());
            final List<EmployeeOrganizationRelationship> relations = (List<EmployeeOrganizationRelationship>)this.genericDao.search((ISearch)search);
            for (final EmployeeOrganizationRelationship iterator : relations) {
                iterator.setEndDate(endDate.getDate());
                iterator.setDeleted(delete.isResponse());
            }
            this.genericDao.save(relations.toArray());
            for (final EmployeeOrganizationRelationship iterator : relations) {
                String ids = "";
                final Employee employee = (Employee)this.employeeDao.find((Serializable)iterator.getEmployeeId());
                if (employee.getOrgIds() != null && !employee.getOrgIds().trim().isEmpty()) {
                    final String[] split;
                    final String[] orgIds = split = employee.getOrgIds().split(",");
                    for (final String id : split) {
                        if (!id.equals(iterator.getOrganizationId() + "")) {
                            ids = ids + id + ",";
                        }
                    }
                    if (ids.endsWith(",")) {
                        ids = ids.substring(0, ids.length() - 1);
                    }
                    employee.setOrgIds(ids);
                    this.employeeDao.merge(employee);
                }
            }
            for (final EmployeeOrganizationRelationship iterator : relations) {
                data.add((EmployeeOrgRelationshipData)this.modelMapper.map((Object)iterator, (Class)EmployeeOrgRelationshipData.class));
            }
            final BaseValueObjectList objectList = new BaseValueObjectList();
            objectList.setValueObjectList((List)data);
            final Response response = this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(EMPParams.EMP_ORG_REL_DATA_LIST.name(), (BaseValueObject)objectList);
            return response;
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - suspendEmpOrgRelationshipsByEmployeeIds() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_SUSPEND_EMP_ORG_RELS_BY_EMP_IDS_018.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response employeeResignation(final Request request) {
        final Identifier empPositionId = (Identifier)request.get(EMPParams.EMP_POSITION_ID.name());
        final DateResponse date = (DateResponse)request.get(EMPParams.POSITION_END_DATE.name());
        if (empPositionId == null || date == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        try {
            final EmployeePositionRelationship data = (EmployeePositionRelationship)this.genericDao.find((Class)EmployeePositionRelationship.class, (Serializable)empPositionId);
            if (data == null) {
                return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), "Unknown error in employee resignation"));
            }
            data.setEndDate(date.getDate());
            final boolean status = this.genericDao.save((Object)data);
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - employeeResignation() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_RESGIN_020.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response transferEmployee(final Request request) {
        final EmployeePositionData oldPositionData = (EmployeePositionData)request.get(EMPParams.EMP_OLD_POSITION_DATA.name());
        final EmployeePositionData newPositionData = (EmployeePositionData)request.get(EMPParams.EMP_POSITION_DATA.name());
        if (oldPositionData == null || newPositionData == null) {
            return this.ERROR((Exception)new MiddlewareException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in the request"));
        }
        EmployeePositionRelationship oldPositionRel = null;
        EmployeePositionRelationship newPositionRel = null;
        try {
            oldPositionRel = (EmployeePositionRelationship)this.genericDao.find((Class)EmployeePositionRelationship.class, (Serializable)oldPositionData.getId());
            this.modelMapper.map((Object)oldPositionData, (Object)oldPositionRel);
            newPositionRel = (EmployeePositionRelationship)this.modelMapper.map((Object)newPositionData, (Class)EmployeePositionRelationship.class);
            this.genericDao.save((Object)oldPositionRel);
            this.genericDao.save((Object)newPositionRel);
            return this.OK(EMPParams.BOOLEAN_RESPONSE.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - transferEmployee() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_TRANSFER_021.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response createCategory(final Request request) {
        final CategoryData categoryData = (CategoryData)request.get(EMPParams.CATEGORY_DATA.name());
        if (categoryData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            Category category = null;
            if (categoryData.getId() != null) {
                category = (Category)this.genericDao.find((Class)Category.class, (Serializable)categoryData.getId());
            }
            else {
                category = new Category();
            }
            this.modelMapper.map((Object)categoryData, (Object)category);
            this.genericDao.save((Object)category);
            return this.OK(EMPParams.CATEGORY_ID.name(), (BaseValueObject)new Identifier(category.getId()));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - createCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_CREATE_003.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getCategory(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.CATEGORY_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        Category category = null;
        try {
            category = (Category)this.genericDao.find((Class)Category.class, (Serializable)identifier.getId());
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_GET_004.name(), e.getMessage(), (Throwable)e));
        }
        if (category == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_DOES_NOT_EXIST_001.name(), "category id {0} does not exist.", new Object[] { identifier }));
        }
        final CategoryData categoryData = (CategoryData)this.modelMapper.map((Object)category, (Class)CategoryData.class);
        return this.OK(EMPParams.CATEGORY_DATA.name(), (BaseValueObject)categoryData);
    }
    
    @Transactional(readOnly = true)
    public Response getAllCategories(final Request request) {
        final SortList sortList = request.getSortList();
        final String defaultField = "type";
        try {
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.genericDao.getFilterFromExample((Object)new Category(), options);
            final Search search = new Search((Class)Category.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortToSearch(search, sortList, defaultField);
            final List<Category> result = (List<Category>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<CategoryData>>() {}.getType();
            final List<CategoryData> categoryList = (List<CategoryData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)categoryList);
            final Response response = this.OK(EMPParams.CATEGORY_TYPE_DATA_LIST.name(), (BaseValueObject)list);
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getAllCategories() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_UNABLE_TO_GET_ALL_007.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response deleteCategory(final Request request) {
        final Identifier identifier = (Identifier)request.get(EMPParams.CATEGORY_ID.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        try {
            final boolean status = this.genericDao.removeById((Class)Category.class, (Serializable)identifier.getId());
            return this.OK(EMPParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - deleteCategory() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_DELETE_006.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response updateEmployeeCategory(final Request request) {
        final CategoryData objCategoryData = (CategoryData)request.get(EMPParams.CATEGORY_DATA.name());
        if (objCategoryData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        Category objCategory = null;
        try {
            if (objCategoryData.getId() != null && objCategoryData.getId() > 0) {
                objCategory = (Category)this.genericDao.find((Class)Category.class, (Serializable)objCategoryData.getId());
                if (objCategory == null) {
                    return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_CATEGORY_DOES_NOT_EXIST_001.name(), "category id {0} does not exist.", new Object[] { objCategoryData.getId() }));
                }
                this.modelMapper.map((Object)objCategoryData, (Object)objCategory);
            }
            final boolean isUpdate = this.genericDao.save((Object)objCategory);
            return this.OK(EMPParams.CATEGORY_DATA.name(), (BaseValueObject)new BooleanResponse(isUpdate));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - updateEmployeeCategory() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNABLE_TO_UPDATE_005.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEmployeesCount(final Request request) {
        try {
            final Search search = new Search((Class)Employee.class);
            final Integer totalEmployees = this.employeeDao.count((ISearch)search);
            return this.OK(EMPParams.EMP_COUNT.name(), (BaseValueObject)new DoubleIdentifier((double)totalEmployees));
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getEmployeesCount() : " + e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response loadFrequentEmployeesToCache(final Request request) {
        final EmployeeData employeeData = (EmployeeData)request.get(EMPParams.EMP_DATA.name());
        if (employeeData == null) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid data in request"));
        }
        final Calendar date = Calendar.getInstance();
        date.set(10, 11);
        date.set(12, 59);
        date.set(13, 59);
        date.set(14, 999);
        date.set(9, 1);
        try {
            final Employee employee = (Employee)this.modelMapper.map((Object)employeeData, (Class)Employee.class);
            final Filter filter = this.employeeDao.getFilterFromExample(employee);
            final Search search = new Search((Class)Employee.class);
            search.addFilter(filter);
            search.addFilterLessOrEqual("startDate", (Object)new Timestamp(date.getTimeInMillis()));
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(date.getTimeInMillis())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            search.addFetch("fieldData");
            final List<Employee> result = (List<Employee>)this.employeeDao.search((ISearch)search);
            final Type listType = new TypeToken<List<EmployeeData>>() {}.getType();
            final List<EmployeeData> employeesList = (List<EmployeeData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)employeesList);
            return this.OK(EMPParams.BASEVALUE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - loadFrequentEmployeesToCache() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_LOAD_FREQ_USERS_TO_CACHE_022.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getUsersIdEmailFromEmployeeIdList(final Request request) {
        final IdentifierList roleIdentifierList = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        if (roleIdentifierList == null) {
            throw new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No roles in the request");
        }
        try {
            final List<Integer> employeeIds = (List<Integer>)roleIdentifierList.getIdsList();
            final List<Object[]> results = this.employeeDao.getUsersIdEmailFromEmployeeIdList(employeeIds);
            final Map<IdentifierList, BaseValueObjectList> userIdEmailMap = new HashMap<IdentifierList, BaseValueObjectList>();
            final List<Integer> userIdList = new ArrayList<Integer>();
            final List<StringIdentifier> userEmailList = new ArrayList<StringIdentifier>();
            for (final Object[] objects : results) {
                if (objects[0] != null && objects[1] != null) {
                    userIdList.add(Integer.parseInt(objects[1].toString()));
                    userEmailList.add(new StringIdentifier((objects[2] == null) ? "" : objects[2].toString()));
                }
            }
            final BaseValueObjectList userEmailBaseObjList = new BaseValueObjectList();
            userEmailBaseObjList.setValueObjectList((List)userEmailList);
            userIdEmailMap.put(new IdentifierList((List)userIdList), userEmailBaseObjList);
            final BaseValueObjectMap baseValueObjectMap = new BaseValueObjectMap();
            baseValueObjectMap.setBaseValueMap((Map)userIdEmailMap);
            return this.OK(EMPParams.USER_DATA_ID_EMAIL.name(), (BaseValueObject)baseValueObjectMap);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in UserDataServiceImpl - getUserIdEmailFromRoleList() : ", (Throwable)e);
            throw new DataAccessException(EmpErrorCodeEnum.ERR_EMP_GET_USERS_ROLE_LIST_023.name(), e.getMessage(), (Throwable)e);
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEmployeesByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(EMPParams.EMP_ID_LIST.name());
        if (idList == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "No data object in the request"));
        }
        final List<EmployeeData> list = new ArrayList<EmployeeData>();
        try {
            final List<EmployeeBase> empArr = this.employeeDao.getEmployeeBaseByIds(idList.getIdsList());
            for (final EmployeeBase objArr : empArr) {
                final EmployeeData data = (EmployeeData)this.modelMapper.map((Object)objArr, (Class)EmployeeData.class);
                list.add(data);
            }
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)list);
            final Response response = this.OK(EMPParams.EMP_DATA_LIST.name(), (BaseValueObject)bvol);
            return response;
        }
        catch (Exception ex) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeDataServiceImpl - getEmployeesByIds() : ", (Throwable)ex);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), ex.getMessage(), (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getEmployeesForOrgId(final Request request) {
        final IdentifierList orgIds = (IdentifierList)request.get(EMPParams.ORG_ID_LIST.name());
        if (orgIds == null || orgIds.getIdsList() == null || orgIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_INVALID_INPUT_002.name(), "Invalid input in request"));
        }
        try {
            final List<EmployeeBase> result = this.employeeDao.getEmployeesForOrgId(orgIds.getIdsList());
            final List<EmployeeData> empData = new ArrayList<EmployeeData>();
            for (final EmployeeBase iterator : result) {
                empData.add((EmployeeData)this.modelMapper.map((Object)iterator, (Class)EmployeeData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)empData);
            return this.OK(EMPParams.EMP_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            EmployeeDataServiceImpl.log.error("Exception in EmployeeManagerServiceImpl - getEmployeesByIds() : ", (Throwable)e);
            return this.ERROR((Exception)new DataAccessException(EmpErrorCodeEnum.ERR_EMP_UNKNOWN_EXCEPTION_000.name(), e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)EmployeeDataServiceImpl.class);
    }
}
