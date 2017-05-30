package com.kpisoft.org.dac.impl;

import com.kpisoft.org.dac.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.kpisoft.org.vo.*;
import com.kpisoft.org.dac.impl.domain.*;
import com.canopus.mw.*;
import com.kpisoft.org.params.*;
import com.canopus.dac.*;
import java.io.*;
import org.springframework.transaction.annotation.*;
import com.googlecode.genericdao.search.*;
import org.modelmapper.*;
import com.canopus.mw.dto.*;
import java.util.*;
import java.lang.reflect.*;

@Component
public class OrganizationSummaryDataServiceImpl extends BaseDataAccessService implements OrganizationSummaryDataService
{
    @Autowired
    private GenericHibernateDao genericHibernateDao;
    private ModelMapper modelMapper;
    
    public OrganizationSummaryDataServiceImpl() {
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)OUSummaryData.class, (Class)OUSummaryEntity.class);
    }
    
    public StringIdentifier getServiceId() {
        return new StringIdentifier("OUSummary");
    }
    
    @Transactional(readOnly = true)
    public Response getOUSummary(final Request request) {
        final Identifier identifier = (Identifier)request.get(OUSummaryParms.OUSUMMARY_ID.name());
        if (identifier == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-102", "No input data in request"));
        }
        final OUSummaryData ouSummaryData = new OUSummaryData();
        try {
            final OUSummaryEntity ouSummaryEntity = (OUSummaryEntity)this.genericHibernateDao.find((Class)OUSummaryEntity.class, (Serializable)identifier.getId());
            if (ouSummaryEntity == null) {
                return this.ERROR((Exception)new DataAccessException("NO_DATA_FOUND", "ousumary entity not found"));
            }
            this.modelMapper.map((Object)ouSummaryEntity, (Object)ouSummaryData);
            return this.OK(OUSummaryParms.OUSUMMARY.name(), (BaseValueObject)ouSummaryData);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-000", "Unknown error while getting orgSummary", (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOUSummaries(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OUSummaryParms.OUSUMMARY_ID_LIST.name());
        if (idList == null) {
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-102", "No data object in the request"));
        }
        final List<Integer> idlist = new ArrayList<Integer>();
        for (final Integer orgId : idList.getIdsList()) {
            idlist.add(orgId);
        }
        try {
            BaseValueObjectList bvol = null;
            final Search search = new Search((Class)OUSummaryEntity.class);
            search.addFilterIn("id", (Collection)idlist);
            final List<OUSummaryEntity> list = (List<OUSummaryEntity>)this.genericHibernateDao.search((ISearch)search);
            final Type listType = new TypeToken<List<OUSummaryData>>() {}.getType();
            final List<OUSummaryData> ouSummaryDataList = (List<OUSummaryData>)this.modelMapper.map((Object)list, listType);
            bvol = new BaseValueObjectList();
            bvol.setValueObjectList((List)ouSummaryDataList);
            return this.OK(OUSummaryParms.OUSUMMARY_LIST.name(), (BaseValueObject)bvol);
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-000", "Unknown error while getting orgSumarys by id", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response saveOUSummary(final Request request) {
        OUSummaryData ouSummaryData = null;
        ouSummaryData = (OUSummaryData)request.get(OUSummaryParms.OUSUMMARY.name());
        if (ouSummaryData == null) {
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-102", "No input data in request"));
        }
        OUSummaryEntity ouSummaryEntity = null;
        if (ouSummaryData.getId() != 0) {
            ouSummaryEntity = (OUSummaryEntity)this.genericHibernateDao.find((Class)OUSummaryEntity.class, (Serializable)ouSummaryData.getId());
        }
        else {
            ouSummaryEntity = new OUSummaryEntity();
        }
        if (ouSummaryEntity == null) {
            ouSummaryEntity = new OUSummaryEntity();
        }
        try {
            if (ouSummaryEntity != null) {
                this.modelMapper.map((Object)ouSummaryData, (Object)ouSummaryEntity);
            }
            this.genericHibernateDao.save((Object)ouSummaryEntity);
            return this.OK(OUSummaryParms.OUSUMMARY_ID.name(), (BaseValueObject)new Identifier(ouSummaryEntity.getId()));
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-000", "Unknown error while saving Orgsummiri", (Throwable)ex));
        }
    }
    
    @Transactional
    public Response saveOUSummaries(final Request request) {
        final BaseValueObjectList list = (BaseValueObjectList)request.get(OUSummaryParms.OUSUMMARY_LIST.name());
        if (list == null) {
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-102", "No data object in the request"));
        }
        try {
            final List<OUSummaryData> list2 = (List<OUSummaryData>)list.getValueObjectList();
            final Type listType = new TypeToken<List<OUSummaryEntity>>() {}.getType();
            final List<OUSummaryEntity> list3 = (List<OUSummaryEntity>)this.modelMapper.map((Object)list2, listType);
            this.genericHibernateDao.save(list3.toArray());
            return this.OK();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return this.ERROR((Exception)new DataAccessException("ORGSUMMARY-DAC-000", "Unknown error while saving OUSumaries", (Throwable)ex));
        }
    }
}
