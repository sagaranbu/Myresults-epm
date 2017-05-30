package com.kpisoft.org.dac.impl;

import com.kpisoft.org.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.org.dac.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import org.apache.log4j.*;
import com.canopus.mw.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.params.*;
import java.io.*;
import com.kpisoft.org.dac.impl.domain.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.dto.*;
import java.lang.reflect.*;
import com.googlecode.genericdao.search.*;

@Service
public class OrganizationDimensionDataServiceImpl extends BaseDataAccessService implements OrganizationDimensionDataService
{
    @Autowired
    private OrganizationDimensionDao objOrganizationDimensionDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public OrganizationDimensionDataServiceImpl() {
        this.objOrganizationDimensionDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)OrganizationDimensionData.class, (Class)OrganizationDimension.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)OrganizationDimensionStructureData.class, (Class)OrganizationUnitType.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response saveOrganizationDimension(final Request request) {
        final OrganizationDimensionData objOrganizationDimensionData = (OrganizationDimensionData)request.get(OrgDimensionParams.ORG_DIM_DATA.name());
        OrganizationDimension objOrganizationDimension = null;
        try {
            if (objOrganizationDimensionData != null && objOrganizationDimensionData.getId() != null && objOrganizationDimensionData.getId() > 0) {
                objOrganizationDimension = (OrganizationDimension)this.objOrganizationDimensionDao.find((Serializable)objOrganizationDimensionData.getId());
            }
            else {
                objOrganizationDimension = new OrganizationDimension();
            }
            this.modelMapper.map((Object)objOrganizationDimensionData, (Object)objOrganizationDimension);
            if (this.objOrganizationDimensionDao.isAttached(objOrganizationDimension)) {
                this.objOrganizationDimensionDao.merge((Object)objOrganizationDimension);
            }
            else {
                this.genericDao.save((Object)objOrganizationDimension);
            }
            if (objOrganizationDimension.getOrganizationStructure() != null && !objOrganizationDimension.getOrganizationStructure().isEmpty()) {
                for (final OrganizationUnitType iterator : objOrganizationDimension.getOrganizationStructure()) {
                    iterator.setOrgDimId(objOrganizationDimension.getId());
                }
            }
            if (objOrganizationDimension.getOrganizationDimensionLanguage() != null && !objOrganizationDimension.getOrganizationDimensionLanguage().isEmpty()) {
                for (final OrganizationDimensionLanguage iterator2 : objOrganizationDimension.getOrganizationDimensionLanguage()) {
                    iterator2.setOrgDimId(objOrganizationDimension.getId());
                }
            }
            objOrganizationDimensionData.setId(objOrganizationDimension.getId());
            return this.OK(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)objOrganizationDimensionData);
        }
        catch (Exception e) {
            OrganizationDimensionDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionDataServiceImpl - saveOrganizationDimension() : " + e.getMessage()));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", "Unknown error while loading dimension", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOrganizationDimension(final Request request) {
        OrganizationDimension objOrganizationDimension = null;
        OrganizationDimensionData objDimensionData = null;
        final Identifier objIdentifier = (Identifier)request.get(OrgDimensionParams.ORG_DIM_DATA_ID.name());
        try {
            if (objIdentifier.getId() > 0) {
                objOrganizationDimension = (OrganizationDimension)this.objOrganizationDimensionDao.find((Serializable)objIdentifier.getId());
                if (objOrganizationDimension == null) {
                    throw new DataAccessException("DIM-DAC-101", "Dimension id {0} does not exist.");
                }
                objDimensionData = (OrganizationDimensionData)this.modelMapper.map((Object)objOrganizationDimension, (Class)OrganizationDimensionData.class);
            }
            return this.OK(OrgDimensionParams.ORG_DIM_DATA.name(), (BaseValueObject)objDimensionData);
        }
        catch (Exception e) {
            OrganizationDimensionDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionDataServiceImpl - getOrganizationDimension() : " + e.getMessage()));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", "Unknown error while loading Dimension", (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeOrganizationDimension(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgDimensionParams.ORG_DIM_DATA_ID.name());
        if (identifier == null) {
            throw new DataAccessException("DIM-DAC-101", "Dimension Id is required");
        }
        boolean flag = false;
        if (identifier.getId() > 0) {
            flag = this.objOrganizationDimensionDao.removeById((Serializable)identifier.getId());
        }
        return this.OK(OrgDimensionParams.ORG_DIM_STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(flag));
    }
    
    @Transactional(readOnly = true)
    public Response getAllOrganizationDimension(final Request request) {
        try {
            final List<OrganizationDimension> alOrganizationDimension = (List<OrganizationDimension>)this.objOrganizationDimensionDao.findAll();
            final Type listType = new TypeToken<List<OrganizationDimensionData>>() {}.getType();
            final List<? extends BaseValueObject> dimensionBVList = (List<? extends BaseValueObject>)this.modelMapper.map((Object)alOrganizationDimension, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)dimensionBVList);
            return this.OK(OrgDimensionParams.ORG_DIM_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationDimensionDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionDataServiceImpl - getAllOrganizationDimension() : " + e.getMessage()));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", "Unknown error while loading Dimension", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchOrgStructure(final Request request) {
        final OrganizationDimensionStructureData objOrganizationDimensionStructureData = (OrganizationDimensionStructureData)request.get(OrgDimensionParams.ORG_STR_DATA.name());
        if (objOrganizationDimensionStructureData == null) {
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-301", "No data object in the request"));
        }
        try {
            final OrganizationUnitType objOrganizationStructure = (OrganizationUnitType)this.modelMapper.map((Object)objOrganizationDimensionStructureData, (Class)OrganizationUnitType.class);
            final Filter filter = this.genericDao.getFilterFromExample((Object)objOrganizationStructure);
            final Search search = new Search((Class)OrganizationUnitType.class);
            search.addFilter(filter);
            final List<OrganizationUnitType> result = (List<OrganizationUnitType>)this.genericDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationDimensionStructureData>>() {}.getType();
            final List<OrganizationDimensionStructureData> dimensionStructureDataList = (List<OrganizationDimensionStructureData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)dimensionStructureDataList);
            return this.OK(OrgDimensionParams.ORG_DIM_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationDimensionDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionDataServiceImpl - search() : " + e.getMessage()));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", "Unknown error while loading Dimension", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response searchOrgDimension(final Request request) {
        final OrganizationDimensionData dimData = (OrganizationDimensionData)request.get(OrgDimensionParams.ORG_DIM_DATA.name());
        if (dimData == null) {
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-301", "No data object in the request"));
        }
        try {
            final OrganizationDimension orgDim = (OrganizationDimension)this.modelMapper.map((Object)dimData, (Class)OrganizationDimension.class);
            final ExampleOptions options = new ExampleOptions();
            options.setLikeMode(3);
            final Filter filter = this.objOrganizationDimensionDao.getFilterFromExample(orgDim, options);
            final Search search = new Search((Class)OrganizationDimension.class);
            search.addFilter(filter);
            final List<OrganizationDimension> result = (List<OrganizationDimension>)this.objOrganizationDimensionDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OrganizationDimensionData>>() {}.getType();
            final List<OrganizationDimensionData> orgDimList = (List<OrganizationDimensionData>)this.modelMapper.map((Object)result, listType);
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)orgDimList);
            return this.OK(OrgDimensionParams.ORG_DIM_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrganizationDimensionDataServiceImpl.log.error((Object)("Exception in OrganizationDimensionDataServiceImpl - searchOrgDimension() : " + e.getMessage()));
            return this.ERROR((Exception)new DataAccessException("DIM-DAC-000", "Unknown error while loading Dimension", (Throwable)e));
        }
    }
    
    static {
        log = Logger.getLogger((Class)OrganizationDimensionDataServiceImpl.class);
    }
}
