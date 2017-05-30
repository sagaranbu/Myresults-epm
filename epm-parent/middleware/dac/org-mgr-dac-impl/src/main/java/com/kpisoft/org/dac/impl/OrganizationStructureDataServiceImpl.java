package com.kpisoft.org.dac.impl;

import com.kpisoft.org.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.org.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import org.apache.log4j.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.dac.impl.domain.*;
import com.canopus.mw.*;
import com.kpisoft.org.params.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;

@Service
public class OrganizationStructureDataServiceImpl extends BaseDataAccessService implements OrganizationStructureDataService
{
    @Autowired
    private OrganizationStructureDao orgTypeDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public OrganizationStructureDataServiceImpl() {
        this.orgTypeDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)OrganizationDimensionStructureData.class, (Class)OrganizationUnitType.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response saveOrganizationStructureDimension(final Request request) {
        final OrganizationDimensionStructureData orgTypeData = (OrganizationDimensionStructureData)request.get(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name());
        if (orgTypeData == null) {
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-001", "Invalid data in request"));
        }
        OrganizationUnitType orgType = null;
        try {
            if (orgTypeData != null && orgTypeData.getId() != null && orgTypeData.getId() > 0) {
                orgType = (OrganizationUnitType)this.orgTypeDao.find((Serializable)orgTypeData.getId());
            }
            else {
                orgType = new OrganizationUnitType();
            }
            this.modelMapper.map((Object)orgTypeData, (Object)orgType);
            if (this.orgTypeDao.isAttached(orgType)) {
                this.orgTypeDao.merge((Object)orgType);
            }
            else {
                this.genericDao.save((Object)orgType);
            }
            orgTypeData.setId(orgType.getId());
            return this.OK(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name(), (BaseValueObject)orgTypeData);
        }
        catch (Exception e) {
            OrganizationStructureDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionStructureDataServiceImpl - saveOrganizationStructureDimension() : " + e));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOrganizationDimensionStructure(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgDimensionStructureParams.ORG_DIM_STR_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-001", "Invalid data in request"));
        }
        OrganizationUnitType orgType = null;
        try {
            orgType = (OrganizationUnitType)this.orgTypeDao.find((Serializable)identifier.getId());
        }
        catch (Exception e) {
            OrganizationStructureDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionStructureDataServiceImpl - getOrganizationDimensionStructure() : " + e));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", e.getMessage(), (Throwable)e));
        }
        if (orgType == null) {
            return this.ERROR((Exception)new DataAccessException("STRUCTURE-101", "Structure id {0} does not exist.", new Object[] { identifier.getId() }));
        }
        final OrganizationDimensionStructureData orgTypeData = (OrganizationDimensionStructureData)this.modelMapper.map((Object)orgType, (Class)OrganizationDimensionStructureData.class);
        return this.OK(OrgDimensionStructureParams.ORG_DIM_STR_DATA.name(), (BaseValueObject)orgTypeData);
    }
    
    @Transactional
    public Response removeOrganizationDimensionStructure(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgDimensionStructureParams.ORG_DIM_STR_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-001", "Invalid data in request"));
        }
        try {
            final boolean status = this.orgTypeDao.removeById((Serializable)identifier.getId());
            return this.OK(OrgDimensionStructureParams.ORG_STR_STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrganizationStructureDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionStructureDataServiceImpl - removeOrganizationDimensionStructure() : " + e));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getAllOrganizationStructure(final Request request) {
        try {
            final List<OrganizationUnitType> orgTypes = (List<OrganizationUnitType>)this.orgTypeDao.findAll();
            final Type listType = new TypeToken<List<OrganizationDimensionStructureData>>() {}.getType();
            final List<? extends BaseValueObject> strBvList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)orgTypes, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)strBvList);
            return this.OK(OrgDimensionStructureParams.ORG_STR_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationStructureDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionStructureDataServiceImpl - getAllOrganizationStructure() : " + e));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = Logger.getLogger((Class)OrganizationStructureDataServiceImpl.class);
    }
}
