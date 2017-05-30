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
public class OrgUserRoleDataServiceImpl extends BaseDataAccessService implements OrgUserRoleDataService
{
    @Autowired
    private OrgUserRoleRelDao relDao;
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    private static final Logger log;
    
    public OrgUserRoleDataServiceImpl() {
        this.relDao = null;
        this.genericDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)OrgUserRoleRelationBean.class, (Class)OrgUserRoleRelation.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response saveOrgUserRole(final Request request) {
        final OrgUserRoleRelationBean data = (OrgUserRoleRelationBean)request.get(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-001", "Invalid input in request"));
        }
        final OrgUserRoleRelation relData = new OrgUserRoleRelation();
        this.modelMapper.map((Object)data, (Object)relData);
        try {
            if (this.relDao.isAttached(relData)) {
                this.relDao.merge((Object)relData);
            }
            else {
                this.relDao.save(relData);
            }
            return this.OK(OrgUserRoleParams.ORG_USER_ROLE_ID.name(), (BaseValueObject)new Identifier(relData.getId()));
        }
        catch (Exception e) {
            OrgUserRoleDataServiceImpl.log.error("Exception in OrgUserRoleDataServiceImpl - saveOrgUserRole() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getOrgUserRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgUserRoleParams.ORG_USER_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final OrgUserRoleRelation relData = (OrgUserRoleRelation)this.relDao.find((Serializable)identifier.getId());
            if (relData == null) {
                return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-002", "Org user role with id {0} does not exist.", new Object[] { identifier.getId() }));
            }
            final OrgUserRoleRelationBean data = (OrgUserRoleRelationBean)this.modelMapper.map((Object)relData, (Class)OrgUserRoleRelationBean.class);
            return this.OK(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name(), (BaseValueObject)data);
        }
        catch (Exception e) {
            OrgUserRoleDataServiceImpl.log.error("Exception in OrgUserRoleDataServiceImpl - getOrgUserRole() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional(readOnly = true)
    public Response search(final Request request) {
        final OrgUserRoleRelationBean data = (OrgUserRoleRelationBean)request.get(OrgUserRoleParams.ORG_USER_ROLE_BEAN.name());
        if (data == null) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final OrgUserRoleRelation relData = (OrgUserRoleRelation)this.modelMapper.map((Object)data, (Class)OrgUserRoleRelation.class);
            final Filter filter = this.relDao.getFilterFromExample(relData);
            final Search search = new Search((Class)OrgUserRoleRelation.class);
            search.addFilter(filter);
            final List<OrgUserRoleRelation> relResult = (List<OrgUserRoleRelation>)this.relDao.search((ISearch)search);
            final List<OrgUserRoleRelationBean> result = new ArrayList<OrgUserRoleRelationBean>();
            for (final OrgUserRoleRelation iterator : relResult) {
                result.add((OrgUserRoleRelationBean)this.modelMapper.map((Object)iterator, (Class)OrgUserRoleRelationBean.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(OrgUserRoleParams.ORG_USER_ROLE_BEAN_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            OrgUserRoleDataServiceImpl.log.error("Exception in OrgUserRoleDataServiceImpl - search() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeOrgUserRole(final Request request) {
        final Identifier identifier = (Identifier)request.get(OrgUserRoleParams.ORG_USER_ROLE_ID.name());
        if (identifier == null || identifier.getId() == null || identifier.getId() <= 0) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final boolean status = this.relDao.removeById((Serializable)identifier.getId());
            return this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception e) {
            OrgUserRoleDataServiceImpl.log.error("Exception in OrgUserRoleDataServiceImpl - removeOrgUserRole() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeRelationForUserIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrgUserRoleParams.USER_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final List<Integer> ids = this.relDao.removeRelations(idList.getIdsList(), 1);
            final Response response = this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(OrgUserRoleParams.ORG_USER_ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)ids));
            return response;
        }
        catch (Exception e) {
            OrgUserRoleDataServiceImpl.log.error("Exception in OrgUserRoleDataServiceImpl - removeRelationForUserIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeRelationForRoleIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrgUserRoleParams.ROLE_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final List<Integer> ids = this.relDao.removeRelations(idList.getIdsList(), 2);
            final Response response = this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(OrgUserRoleParams.ORG_USER_ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)ids));
            return response;
        }
        catch (Exception e) {
            OrgUserRoleDataServiceImpl.log.error("Exception in OrgUserRoleDataServiceImpl - removeRelationForRoleIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeRelationForOrgIds(final Request request) {
        final IdentifierList idList = (IdentifierList)request.get(OrgUserRoleParams.ORG_ID_LIST.name());
        if (idList == null || idList.getIdsList() == null || idList.getIdsList().isEmpty()) {
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-001", "Invalid input in request"));
        }
        try {
            final List<Integer> ids = this.relDao.removeRelations(idList.getIdsList(), 3);
            final Response response = this.OK(OrgUserRoleParams.STATUS.name(), (BaseValueObject)new BooleanResponse(true));
            response.put(OrgUserRoleParams.ORG_USER_ROLE_ID_LIST.name(), (BaseValueObject)new IdentifierList((List)ids));
            return response;
        }
        catch (Exception e) {
            OrgUserRoleDataServiceImpl.log.error("Exception in OrgUserRoleDataServiceImpl - removeRelationForOrgIds() : " + e);
            return this.ERROR((Exception)new DataAccessException("ORG-USER-ROLE-DAC-003", e.getMessage(), (Throwable)e));
        }
    }
    
    static {
        log = LoggerFactory.getLogger((Class)OrgUserRoleDataServiceImpl.class);
    }
}
