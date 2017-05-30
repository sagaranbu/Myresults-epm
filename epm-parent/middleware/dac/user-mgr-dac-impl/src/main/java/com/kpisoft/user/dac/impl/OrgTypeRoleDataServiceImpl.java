package com.kpisoft.user.dac.impl;

import com.kpisoft.user.dac.*;
import org.springframework.stereotype.*;
import com.kpisoft.user.dac.impl.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.dac.hibernate.*;
import org.modelmapper.*;
import com.kpisoft.user.vo.*;
import com.kpisoft.user.dac.impl.entity.*;
import com.canopus.mw.*;
import com.kpisoft.user.vo.param.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import java.io.*;
import com.googlecode.genericdao.search.*;
import java.util.*;
import com.canopus.mw.dto.*;
import org.slf4j.*;

@Component
public class OrgTypeRoleDataServiceImpl extends BaseDataAccessService implements OrgTypeRoleDataService
{
    @Autowired
    private OrgTypeRoleRelDao relDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public OrgTypeRoleDataServiceImpl() {
        this.relDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)OrgTypeRoleRelationBean.class, (Class)OrgTypeRoleRelation.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response saveOrgTypeRole(final Request request) {
        final OrgTypeRoleRelationBean data = (OrgTypeRoleRelationBean)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        final OrgTypeRoleRelation relData = new OrgTypeRoleRelation();
        this.modelMapper.map((Object)data, (Object)relData);
        try {
            if (this.relDao.isAttached(relData)) {
                this.relDao.merge((Object)relData);
            }
            else {
                this.relDao.save(relData);
            }
            return this.OK(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name(), (BaseValueObject)new Identifier(relData.getId()));
        }
        catch (Exception e) {
            OrgTypeRoleDataServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - saveOrgTypeRole() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOrgTypeRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final OrgTypeRoleRelation relData = (OrgTypeRoleRelation)this.relDao.find((Serializable)identifier.getId());
            final OrgTypeRoleRelationBean data = (OrgTypeRoleRelationBean)this.modelMapper.map((Object)relData, (Class)OrgTypeRoleRelationBean.class);
            return this.OK(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name(), (BaseValueObject)data);
        }
        catch (Exception e) {
            OrgTypeRoleDataServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - getOrgTypeRole() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final OrgTypeRoleRelationBean data = (OrgTypeRoleRelationBean)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final OrgTypeRoleRelation relBean = (OrgTypeRoleRelation)this.modelMapper.map((Object)data, (Class)OrgTypeRoleRelation.class);
            final Filter filter = this.relDao.getFilterFromExample(relBean);
            final Search search = new Search((Class)OrgTypeRoleRelation.class);
            search.addFilter(filter);
            final List<OrgTypeRoleRelation> relList = (List<OrgTypeRoleRelation>)this.relDao.search((ISearch)search);
            final List<OrgTypeRoleRelationBean> result = new ArrayList<OrgTypeRoleRelationBean>();
            for (final OrgTypeRoleRelation iterator : relList) {
                result.add((OrgTypeRoleRelationBean)this.modelMapper.map((Object)iterator, (Class)OrgTypeRoleRelationBean.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(OrgTypeRoleParams.ORG_TYPE_ROLE_BEAN_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrgTypeRoleDataServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - search() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeOrgTypeRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgTypeRoleParams.ORG_TYPE_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.relDao.removeById((Serializable)identifier.getId());
            return this.OK(OrgTypeRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgTypeRoleDataServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - removeOrgTypeRole() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeRelationshipByRoleIds(final Request request) {
        final IdentifierList roleIds = (IdentifierList)request.get(OrgTypeRoleParams.ROLE_ID_LIST.name());
        if (roleIds == null || roleIds.getIdsList() == null || roleIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final List<Integer> idList = this.relDao.removeRelationships(roleIds.getIdsList(), 1);
            final Response response = this.OK(OrgTypeRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(OrgTypeRoleParams.ORG_TYPE_ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrgTypeRoleDataServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - removeRelationshipByRoleIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeRelationshipByOrgTypeIds(final Request request) {
        final IdentifierList orgTypeIds = (IdentifierList)request.get(OrgTypeRoleParams.ORGTYPE_ID_LIST.name());
        if (orgTypeIds == null || orgTypeIds.getIdsList() == null || orgTypeIds.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final List<Integer> idList = this.relDao.removeRelationships(orgTypeIds.getIdsList(), 2);
            final Response response = this.OK(OrgTypeRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(OrgTypeRoleParams.ORG_TYPE_ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)idList));
            return response;
        }
        catch (Exception e) {
            OrgTypeRoleDataServiceImpl.log.error("Exception in OrgTypeRoleDataServiceImpl - removeRelationshipByOrgTypeIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORGTYPE-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OrgTypeRoleDataServiceImpl.class);
    }
}
