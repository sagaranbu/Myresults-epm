package com.kpisoft.org.dac.impl;

import com.kpisoft.org.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.org.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import com.canopus.mw.*;
import com.kpisoft.org.vo.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.kpisoft.org.dac.impl.domain.*;
import org.modelmapper.*;
import java.lang.reflect.*;
import java.util.*;
import java.util.Date;
import java.sql.*;
import com.canopus.dac.utils.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;
import org.slf4j.*;

@Component
public class OrganizationUnitDataServiceImpl extends BaseDataAccessService implements OrganizationUnitDataService
{
    @Autowired
    private OrganizationUnitDao organizationUnitDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public OrganizationUnitDataServiceImpl() {
        this.organizationUnitDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)OrganizationUnitData.class, (Class)OrganizationUnit.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)OrganizationFiledData.class, (Class)OrganizationFieldData.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)PasswordPolicyData.class, (Class)PasswordPolicy.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)OrgParentRelationshipBean.class, (Class)OrgParentRelationship.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)OrganizationDimensionStructureData.class, (Class)OrganizationUnitType.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response saveOrganizationUnit(final Request request) {
        final OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        final Identifier dimId = (Identifier)request.get(OrganizationParams.DIMENSION_ID.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        Integer dimensionId = null;
        if (data.getParentId() != null && dimId != null && dimId.getId() != null) {
            dimensionId = dimId.getId();
        }
        if (dimensionId == null) {
            dimensionId = 0;
        }
        OrganizationUnit orgUnit = null;
        int oldParentId = 0;
        if (data.getId() != null && data.getId() > 0) {
            orgUnit = (OrganizationUnit)this.organizationUnitDao.find((Serializable)data.getId());
            if (orgUnit.getParentId() != null && orgUnit.getParentId() > 0) {
                oldParentId = orgUnit.getParentId();
            }
        }
        else {
            orgUnit = new OrganizationUnit();
        }
        try {
            this.modelMapper.map((Object)data, (Object)orgUnit);
            this.organizationUnitDao.merge(orgUnit);
            if (orgUnit.getFiledData() != null && !orgUnit.getFiledData().isEmpty()) {
                for (final OrganizationFieldData iterator : orgUnit.getFiledData()) {
                    iterator.setOrgUnitId(orgUnit.getId());
                }
            }
            if (orgUnit.getParentId() != null && orgUnit.getParentId() > 0 && dimensionId != null) {
                final OrganizationUnit parentOrgUnit = (OrganizationUnit)this.organizationUnitDao.find((Serializable)orgUnit.getParentId());
                if (data.getId() == null || data.getId() <= 0) {
                    final OrgIdentity sourceIdentity = new OrgIdentity();
                    sourceIdentity.setId(orgUnit.getId());
                    sourceIdentity.setOrgUnitCode(orgUnit.getOrgUnitCode());
                    sourceIdentity.setOrgName(orgUnit.getOrgName());
                    final OrgIdentity destinationIdentity = new OrgIdentity();
                    destinationIdentity.setId(parentOrgUnit.getId());
                    destinationIdentity.setOrgUnitCode(parentOrgUnit.getOrgUnitCode());
                    destinationIdentity.setOrgName(parentOrgUnit.getOrgName());
                    final OrgParentRelationship relIdentity = new OrgParentRelationship();
                    relIdentity.setDimensionId(dimensionId);
                    relIdentity.setSourceIdentity(sourceIdentity);
                    relIdentity.setDestinationIdentity(destinationIdentity);
                    this.genericDao.save((Object)relIdentity);
                }
                else if (oldParentId != 0) {
                    final Search search = new Search((Class)OrgParentRelationship.class);
                    search.addFilterEqual("sourceIdentity.id", (Object)orgUnit.getId());
                    final List<OrgParentRelationship> result = (List<OrgParentRelationship>)this.genericDao.search((ISearch)search);
                    OrgParentRelationship relData = null;
                    if (result != null && !result.isEmpty()) {
                        for (final OrgParentRelationship iterator2 : result) {
                            if (iterator2.getDestinationIdentity().getId() == oldParentId) {
                                relData = iterator2;
                                break;
                            }
                        }
                        if (relData != null) {
                            relData.setEndDate(new Date());
                            relData.setDeleted(true);
                            this.genericDao.save((Object)relData);
                        }
                    }
                    final OrgIdentity sourceIdentity2 = new OrgIdentity();
                    sourceIdentity2.setId(orgUnit.getId());
                    sourceIdentity2.setOrgUnitCode(orgUnit.getOrgUnitCode());
                    sourceIdentity2.setOrgName(orgUnit.getOrgName());
                    final OrgIdentity destinationIdentity2 = new OrgIdentity();
                    destinationIdentity2.setId(parentOrgUnit.getId());
                    destinationIdentity2.setOrgUnitCode(parentOrgUnit.getOrgUnitCode());
                    destinationIdentity2.setOrgName(parentOrgUnit.getOrgName());
                    final OrgParentRelationship relIdentity2 = new OrgParentRelationship();
                    relIdentity2.setDimensionId(dimensionId);
                    relIdentity2.setSourceIdentity(sourceIdentity2);
                    relIdentity2.setDestinationIdentity(destinationIdentity2);
                    this.genericDao.save((Object)relIdentity2);
                }
            }
            if (data.getId() == null || data.getId() == 0) {
                final Search search2 = new Search((Class)PasswordPolicy.class);
                search2.addFilterEqual("isDefault", (Object)1);
                final PasswordPolicy passwordPolicy = (PasswordPolicy)this.genericDao.searchUnique((ISearch)search2);
                final PasswordPolicyData passwordPolicyData = (PasswordPolicyData)this.modelMapper.map((Object)passwordPolicy, (Class)PasswordPolicyData.class);
                passwordPolicyData.setId((Integer)null);
                passwordPolicyData.setIsDefault(0);
                passwordPolicyData.setOrgId(orgUnit.getId());
                final PasswordPolicy defaultPasswordPolicy = (PasswordPolicy)this.modelMapper.map((Object)passwordPolicyData, (Class)PasswordPolicy.class);
                this.genericDao.save((Object)defaultPasswordPolicy);
            }
            return this.OK(OrganizationParams.ORG_IDENTIFIER.name(), (BaseValueObject)new Identifier(orgUnit.getId()));
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - saveOrganizationUnit() : " + e);
            throw new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e);
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOrganizationUnitData(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        OrganizationUnit orgUnit = null;
        try {
            orgUnit = (OrganizationUnit)this.organizationUnitDao.find((Serializable)identifier.getId());
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getOrganizationUnitData() : " + e);
            throw new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e);
        }
        if (orgUnit == null) {
            throw new DataAccessException("ORG-DAC-101", "Organization unit id {0} does not exist.", new Object[] { identifier.getId() });
        }
        final OrganizationUnitData orgData = (OrganizationUnitData)this.modelMapper.map((Object)orgUnit, (Class)OrganizationUnitData.class);
        return this.OK(OrganizationParams.ORG_DATA.name(), (BaseValueObject)orgData);
    }
    
    @Transactional(readOnly = true)
    public Response getOrganizationUnits(final Request request) {
        List<OrganizationUnitBase> allOrgUnits = null;
        try {
            final Search search = new Search((Class)OrganizationUnitBase.class);
            search.addFilterEqual("deleted", (Object)false);
            search.addFilterEqual("status", (Object)1);
            search.addFetch("filedData");
            allOrgUnits = (List<OrganizationUnitBase>)this.genericDao.search((ISearch)search);
            if (allOrgUnits == null || allOrgUnits.isEmpty()) {
                throw new DataAccessException("ORG-DAC-102", "Organization units does not exist.");
            }
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgUnitsDataList = (List<OrganizationUnitData>)this.modelMapper.map((Object)allOrgUnits, listType);
            final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
            baseValueObjectList.setValueObjectList((List)orgUnitsDataList);
            return this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)baseValueObjectList);
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getOrganizationUnits() : " + e);
            throw new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e);
        }
    }
    
    @Transactional
    public Response deleteOrganizationUnit(final Request request) {
        final Identifier orgId = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.name());
        if (orgId == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        try {
            this.organizationUnitDao.removeByIds(new Serializable[] { orgId.getId() });
            return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - deleteOrganizationUnit() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendOrganizationUnit(final Request request) {
        final Identifier orgId = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.name());
        final DateResponse endDate = (DateResponse)request.get(OrganizationParams.END_DATE.name());
        if (orgId == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        try {
            final OrganizationUnit orgUnit = (OrganizationUnit)this.organizationUnitDao.find((Serializable)orgId.getId());
            orgUnit.setEndDate(endDate.getDate());
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
                orgUnit.setStatus(0);
            }
            final boolean status = this.organizationUnitDao.save(orgUnit);
            return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - suspendOrganizationUnit() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response deleteOrganizationsByIds(final Request request) {
        final IdentifierList orgIds = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        if (orgIds == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        try {
            this.organizationUnitDao.removeByIds(new Serializable[] { orgIds.getIdsList().toArray() });
            return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - deleteOrganizationsByIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response suspendOrganizationsByIds(final Request request) {
        final IdentifierList orgIds = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(OrganizationParams.END_DATE.name());
        if (orgIds == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        try {
            final Search search = new Search((Class)OrganizationUnit.class);
            search.addFilterIn("id", (Collection)orgIds.getIdsList());
            final List<OrganizationUnit> orgUnits = (List<OrganizationUnit>)this.organizationUnitDao.search((ISearch)search);
            for (final OrganizationUnit iterator : orgUnits) {
                iterator.setEndDate(endDate.getDate());
            }
            this.organizationUnitDao.save((OrganizationUnit[])orgUnits.toArray());
            return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - suspendOrganizationsByIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchOrganizationUnitByName(final Request request) {
        final Name orgUnitName = (Name)request.get(OrganizationParams.ORG_UNIT_NAME.getParamName());
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "orgName";
        if (orgUnitName == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        List<OrganizationUnitBase> orgUnits = null;
        final List<OrganizationUnitData> result = new ArrayList<OrganizationUnitData>();
        final Search search = new Search((Class)OrganizationUnitBase.class);
        search.addFilterILike("orgName", "%" + orgUnitName.getName() + "%");
        search.addFilterEqual("status", (Object)1);
        search.addFilterEqual("deleted", (Object)false);
        search.addFilterLessOrEqual("startDate", (Object)new Timestamp(new Date().getTime()));
        search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(new Date().getTime())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
        PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
        orgUnits = (List<OrganizationUnitBase>)this.genericDao.search((ISearch)search);
        if (orgUnits == null) {
            throw new DataAccessException("ORG-DAC-101", "Organization unit with name {0} does not exist.", new Object[] { orgUnitName.getName() });
        }
        for (final OrganizationUnitBase iterator : orgUnits) {
            result.add((OrganizationUnitData)this.modelMapper.map((Object)iterator, (Class)OrganizationUnitData.class));
        }
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)result);
        final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)list);
        response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
        response.setSortList(sortList);
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "orgName";
        try {
            final OrganizationUnit orgUnit = (OrganizationUnit)this.modelMapper.map((Object)data, (Class)OrganizationUnit.class);
            orgUnit.setStatus(1);
            orgUnit.setStartDate(null);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.organizationUnitDao.getFilterFromExample(orgUnit, options);
            final Search search = new Search((Class)OrganizationUnitBase.class);
            search.addFilter(filter);
            search.addFilterLessOrEqual("startDate", (Object)new Timestamp(new Date().getTime()));
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(new Date().getTime())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<OrganizationUnitBase> result = (List<OrganizationUnitBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgsList = (List<OrganizationUnitData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)orgsList);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - search() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchAll(final Request request) {
        final OrganizationUnitData data = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "orgName";
        try {
            final OrganizationUnit orgUnit = (OrganizationUnit)this.modelMapper.map((Object)data, (Class)OrganizationUnit.class);
            orgUnit.setStartDate(null);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            options.setIgnoreCase(true);
            final Filter filter = this.organizationUnitDao.getFilterFromExample(orgUnit, options);
            final Search search = new Search((Class)OrganizationUnitBase.class);
            search.addFilter(filter);
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.genericDao.count((ISearch)search), defaultField);
            final List<OrganizationUnitBase> result = (List<OrganizationUnitBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgsList = (List<OrganizationUnitData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)orgsList);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - searchAll() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response addParent(final Request request) {
        final OrgParentRelationshipBean data = (OrgParentRelationshipBean)request.get(OrganizationParams.ORG_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        final OrgParentRelationship rel = (OrgParentRelationship)this.modelMapper.map((Object)data, (Class)OrgParentRelationship.class);
        this.genericDao.save((Object)rel);
        final OrgParentRelationship relData = (OrgParentRelationship)this.genericDao.find((Class)OrgParentRelationship.class, (Serializable)rel.getId());
        final OrgParentRelationshipBean result = (OrgParentRelationshipBean)this.modelMapper.map((Object)relData, (Class)OrgParentRelationshipBean.class);
        final Response response = this.OK(OrganizationParams.ORG_REL_ID.name(), (BaseValueObject)new Identifier(rel.getId()));
        response.put(OrganizationParams.ORG_REL_DATA.name(), (BaseValueObject)result);
        return response;
    }
    
    @Transactional(readOnly = true)
    public Response getParentRelById(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_REL_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        try {
            final OrgParentRelationship relData = (OrgParentRelationship)this.genericDao.find((Class)OrgParentRelationship.class, (Serializable)identifier.getId());
            final OrgParentRelationshipBean result = (OrgParentRelationshipBean)this.modelMapper.map((Object)relData, (Class)OrgParentRelationshipBean.class);
            return this.OK(OrganizationParams.ORG_REL_DATA.name(), (BaseValueObject)result);
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getParentRelById() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeParent(final Request request) {
        final OrgParentRelationshipBean data = (OrgParentRelationshipBean)request.get(OrganizationParams.ORG_REL_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        final boolean status = this.genericDao.removeById((Class)OrgParentRelationship.class, (Serializable)data.getId());
        return this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
    }
    
    @Transactional(readOnly = true)
    public Response getParentRelationshipsByOrgId(final Request request) {
        final Identifier orgId = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.name());
        if (orgId == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        try {
            final List<OrgParentRelationshipBean> data = new ArrayList<OrgParentRelationshipBean>();
            final Search search = new Search((Class)OrgParentRelationship.class);
            search.addFilterEqual("sourceIdentity.id", (Object)orgId.getId());
            search.addFilterLessOrEqual("startDate", (Object)new Timestamp(new Date().getTime()));
            search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(new Date().getTime())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
            final List<OrgParentRelationship> result = (List<OrgParentRelationship>)this.genericDao.search((ISearch)search);
            if (result != null && !result.isEmpty()) {
                for (final OrgParentRelationship iterator : result) {
                    data.add((OrgParentRelationshipBean)this.modelMapper.map((Object)iterator, (Class)OrgParentRelationshipBean.class));
                }
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)data);
            return this.OK(OrganizationParams.ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getParentRelationshipsByOrgId() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getParentRelationships(final Request request) {
        final List<OrgParentRelationshipBean> data = new ArrayList<OrgParentRelationshipBean>();
        final Search search = new Search((Class)OrgParentRelationship.class);
        search.addFilterLessOrEqual("startDate", (Object)new Timestamp(new Date().getTime()));
        search.addFilterOr(new Filter[] { Filter.greaterOrEqual("endDate", (Object)new Timestamp(new Date().getTime())), Filter.or(new Filter[] { Filter.isNull("endDate") }) });
        final List<OrgParentRelationship> result = (List<OrgParentRelationship>)this.genericDao.search((ISearch)search);
        if (result != null && !result.isEmpty()) {
            for (final OrgParentRelationship iterator : result) {
                data.add((OrgParentRelationshipBean)this.modelMapper.map((Object)iterator, (Class)OrgParentRelationshipBean.class));
            }
        }
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)data);
        return this.OK(OrganizationParams.ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
    }
    
    @Transactional(readOnly = true)
    public Response getAllParentRelationships(final Request request) {
        final List<OrgParentRelationshipBean> data = new ArrayList<OrgParentRelationshipBean>();
        final List<OrgParentRelationship> result = (List<OrgParentRelationship>)this.genericDao.findAll((Class)OrgParentRelationship.class);
        OrganizationUnitDataServiceImpl.log.debug("Get All Parent Relationships - found " + result.size() + " relationships.");
        if (result != null && !result.isEmpty()) {
            for (final OrgParentRelationship iterator : result) {
                data.add((OrgParentRelationshipBean)this.modelMapper.map((Object)iterator, (Class)OrgParentRelationshipBean.class));
            }
        }
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)data);
        return this.OK(OrganizationParams.ORG_REL_DATA_LIST.name(), (BaseValueObject)list);
    }
    
    @Transactional
    public Response suspendParentRelationshipsByIds(final Request request) {
        final IdentifierList list = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        final DateResponse endDate = (DateResponse)request.get(OrganizationParams.END_DATE.name());
        BooleanResponse delete = (BooleanResponse)request.get(OrganizationParams.DELETE.name());
        if (list == null || endDate == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        if (delete == null) {
            delete = new BooleanResponse(false);
        }
        final List<OrgParentRelationship> result = new ArrayList<OrgParentRelationship>();
        final List<OrgParentRelationshipBean> data = new ArrayList<OrgParentRelationshipBean>();
        try {
            Search search = new Search((Class)OrgParentRelationship.class);
            search.addFilterIn("sourceIdentity.id", (Collection)list.getIdsList());
            List<OrgParentRelationship> relations = (List<OrgParentRelationship>)this.genericDao.search((ISearch)search);
            for (final OrgParentRelationship iterator : relations) {
                iterator.setEndDate(endDate.getDate());
                iterator.setDeleted(delete.isResponse());
            }
            this.genericDao.save(relations.toArray());
            result.addAll(relations);
            search = new Search((Class)OrgParentRelationship.class);
            search.addFilterIn("destinationIdentity.id", (Collection)list.getIdsList());
            relations = (List<OrgParentRelationship>)this.genericDao.search((ISearch)search);
            for (final OrgParentRelationship iterator : relations) {
                iterator.setEndDate(endDate.getDate());
                iterator.setDeleted(delete.isResponse());
            }
            this.genericDao.save(relations.toArray());
            result.addAll(relations);
            for (final OrgParentRelationship iterator : result) {
                data.add((OrgParentRelationshipBean)this.modelMapper.map((Object)iterator, (Class)OrgParentRelationshipBean.class));
            }
            final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
            baseValueObjectList.setValueObjectList((List)data);
            final Response response = this.OK(OrganizationParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(OrganizationParams.ORG_REL_DATA_LIST.name(), (BaseValueObject)baseValueObjectList);
            return response;
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - suspendParentRelationshipsByIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response updateOrgPasswordPolicy(final Request request) {
        final PasswordPolicyData data = (PasswordPolicyData)request.get(OrganizationParams.PASSWORD_POLICY_DATA.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        data.setIsDefault(0);
        final PasswordPolicy passwordPolicy = (PasswordPolicy)this.genericDao.find((Class)PasswordPolicy.class, (Serializable)data.getId());
        try {
            this.modelMapper.map((Object)data, (Object)passwordPolicy);
            this.genericDao.save((Object)passwordPolicy);
            return this.OK(OrganizationParams.PASSWORD_POLICY_ID.name(), (BaseValueObject)new Identifier(passwordPolicy.getId()));
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - updateOrgPasswordPolicy() : " + e);
            throw new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e);
        }
    }
    
    @Transactional(readOnly = true)
    public Response getDefaultPasswordPolicy(final Request request) {
        PasswordPolicy policy = null;
        try {
            final Search search = new Search((Class)PasswordPolicy.class);
            search.addFilterEqual("isDefault", (Object)1);
            policy = (PasswordPolicy)this.genericDao.searchUnique((ISearch)search);
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getDefaultPasswordPolicy() : " + e);
            throw new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e);
        }
        if (policy == null) {
            throw new DataAccessException("ORG-DAC-000", "Unknown error while loading default password policy");
        }
        final PasswordPolicyData policyData = (PasswordPolicyData)this.modelMapper.map((Object)policy, (Class)PasswordPolicyData.class);
        return this.OK(OrganizationParams.PASSWORD_POLICY_DATA.name(), (BaseValueObject)policyData);
    }
    
    @Transactional(readOnly = true)
    public Response getOrgPasswordPolicy(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrganizationParams.ORG_IDENTIFIER.name());
        if (identifier == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_PARAM", "No input data in the request"));
        }
        PasswordPolicy policy = null;
        try {
            final Search search = new Search((Class)PasswordPolicy.class);
            search.addFilterEqual("orgId", (Object)identifier.getId());
            policy = (PasswordPolicy)this.genericDao.searchUnique((ISearch)search);
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getOrgPasswordPolicy() : " + e);
            throw new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e);
        }
        if (policy == null) {
            throw new DataAccessException("ORG-DAC-103", "Organization password policy with id {0} does not exist.", new Object[] { identifier.getId() });
        }
        final PasswordPolicyData policyData = (PasswordPolicyData)this.modelMapper.map((Object)policy, (Class)PasswordPolicyData.class);
        return this.OK(OrganizationParams.PASSWORD_POLICY_DATA.name(), (BaseValueObject)policyData);
    }
    
    @Transactional(readOnly = true)
    public Response searchDepartment(final Request request) {
        final OrganizationUnitData objOrganizationUnitData = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        if (objOrganizationUnitData == null) {
            throw new DataAccessException("ORG-DAC-101", "No Org unit data found in request");
        }
        try {
            String str = "";
            str = str + "orgName like '" + objOrganizationUnitData.getOrgName() + "%' ";
            final Search search = new Search((Class)OrganizationUnit.class);
            search.addFilterCustom(str);
            final List<OrganizationUnit> result = (List<OrganizationUnit>)this.organizationUnitDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgUnitDataList = (List<OrganizationUnitData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)orgUnitDataList);
            return this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - searchDepartment() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-101", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOrganizationsByIdList(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", "No data object in the request"));
        }
        try {
            final Search search = new Search((Class)OrganizationUnitBase.class);
            search.addFilterIn("id", (Collection)idList.getIdsList());
            search.addFetch("filedData");
            final List<OrganizationUnitBase> list = (List<OrganizationUnitBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgList = (List<OrganizationUnitData>)this.modelMapper.map((Object)list, listType);
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)orgList);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)bvol);
            return response;
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getOrganizationsByIdList() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchOrgByFieldData(final Request request) {
        final OrganizationUnitData objOrganizationUnitData = (OrganizationUnitData)request.get(OrganizationParams.ORG_DATA.name());
        objOrganizationUnitData.setStartDate((Date)null);
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "orgName";
        final OrganizationUnit orgObj = (OrganizationUnit)this.modelMapper.map((Object)objOrganizationUnitData, (Class)OrganizationUnit.class);
        final Search search = new Search((Class)OrganizationUnit.class);
        final ExampleOptions options = new ExampleOptions();
        options.setLikeMode(3);
        options.setIgnoreCase(true);
        final Filter filter = this.organizationUnitDao.getFilterFromExample(orgObj, options);
        search.addFilter(filter);
        final List<OrganizationFiledData> orgFiledList = (List<OrganizationFiledData>)objOrganizationUnitData.getFiledData();
        if (orgFiledList != null && orgFiledList.size() > 0) {
            final OrganizationFiledData orgFiledData = orgFiledList.get(0);
            if (orgFiledData.getId() != null && orgFiledData.getId() > 0) {
                search.addFilterEqual("filedData.id", (Object)orgFiledData.getId());
            }
            if (orgFiledData.getFieldId() != null && orgFiledData.getFieldId() > 0) {
                search.addFilterEqual("filedData.fieldId", (Object)orgFiledData.getFieldId());
            }
            if (orgFiledData.getData() != null && orgFiledData.getData() != "") {
                search.addFilterILike("filedData.data", "%" + orgFiledData.getData() + "%");
            }
        }
        try {
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, this.organizationUnitDao.count((ISearch)search), defaultField);
            final List<OrganizationUnit> result = (List<OrganizationUnit>)this.organizationUnitDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgUnitDataList = (List<OrganizationUnitData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)orgUnitDataList);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)list);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - searchOrgByFieldData() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-101", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOrgBaseByIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrganizationParams.ORG_IDENTIFIER_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", "No data object in the request"));
        }
        final Page page = request.getPage();
        final SortList sortList = request.getSortList();
        final String defaultField = "orgName";
        try {
            final Search search = new Search((Class)OrganizationUnitBase.class);
            search.addFilterIn("id", (Collection)idList.getIdsList());
            PaginationSortDAOHelper.addSortAndPaginationToSearch(search, page, sortList, idList.getIdsList().size(), defaultField);
            final List<OrganizationUnitBase> list = (List<OrganizationUnitBase>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgList = (List<OrganizationUnitData>)this.modelMapper.map((Object)list, listType);
            final BaseValueObjectList bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)orgList);
            final Response response = this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)bvol);
            response.setPage((int)page.getPageNumber(), (int)page.getTotalCount());
            response.setSortList(sortList);
            return response;
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getOrgBaseByIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response loadOrgUnitsToCache(final Request request) {
        List<OrganizationUnit> allOrgUnits = null;
        try {
            final Search search = new Search((Class)OrganizationUnit.class);
            search.addFilterEqual("status", (Object)1);
            allOrgUnits = (List<OrganizationUnit>)this.genericDao.search((ISearch)search);
            if (allOrgUnits == null || allOrgUnits.isEmpty()) {
                throw new DataAccessException("ORG-DAC-102", "Organization units does not exist.");
            }
            final Type listType = new TypeToken<List<OrganizationUnitData>>() {}.getType();
            final List<OrganizationUnitData> orgUnitsDataList = (List<OrganizationUnitData>)this.modelMapper.map((Object)allOrgUnits, listType);
            final BaseValueObjectList baseValueObjectList = new BaseValueObjectList();
            baseValueObjectList.setValueObjectList((List)orgUnitsDataList);
            return this.OK(OrganizationParams.ORG_DATA_LIST.name(), (BaseValueObject)baseValueObjectList);
        }
        catch (Exception e) {
            OrganizationUnitDataServiceImpl.log.error("Exception in OrganizationUnitDataServiceImpl - getOrganizationUnits() : " + e);
            throw new DataAccessException("ORG-DAC-000", e.getMessage(), (Throwable)e);
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OrganizationUnitDataServiceImpl.class);
    }
}
