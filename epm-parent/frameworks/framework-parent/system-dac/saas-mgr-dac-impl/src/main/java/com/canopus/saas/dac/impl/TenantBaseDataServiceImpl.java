package com.canopus.saas.dac.impl;

import com.canopus.saas.dac.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import org.modelmapper.*;
import com.canopus.mw.*;
import com.canopus.saas.vo.*;
import com.canopus.saas.vo.params.*;
import com.canopus.dac.*;
import java.io.*;
import com.canopus.saas.dac.entity.*;
import org.springframework.transaction.annotation.*;
import com.canopus.mw.dto.*;
import java.util.*;

@Component
public class TenantBaseDataServiceImpl extends BaseDataAccessService implements TenantBaseDataService
{
    @Autowired
    private GenericHibernateDao genericDao;
    private ModelMapper modelMapper;
    
    public TenantBaseDataServiceImpl() {
        this.genericDao = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)TenantBaseData.class, (Class)TenantBase.class);
        TransformationHelper.createTypeMap(this.modelMapper, (Class)TenantContentData.class, (Class)TenantContent.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Response createTenant(final Request request) {
        final TenantBaseData tenantBaseData = (TenantBaseData)request.get(TenantParams.TENANT_DATA.name());
        if (tenantBaseData == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_TENANT_DATA", "No data object in the request"));
        }
        TenantBase tenantBase = null;
        try {
            if (tenantBaseData.getId() != null && tenantBaseData.getId() > 0) {
                tenantBase = (TenantBase)this.genericDao.find((Class)TenantBase.class, (Serializable)tenantBaseData.getId());
            }
            else {
                tenantBase = new TenantBase();
            }
            this.modelMapper.map((Object)tenantBaseData, (Object)tenantBase);
            this.genericDao.save((Object)tenantBase);
            final TenantLangBase tenantLangBase = new TenantLangBase();
            tenantLangBase.setTenantBase(tenantBase);
            tenantLangBase.setMaslang(tenantBaseData.getMasLang());
            this.genericDao.save((Object)tenantLangBase);
            return this.OK(TenantParams.TENANT_ID.name(), (BaseValueObject)new Identifier(tenantBase.getId()));
        }
        catch (Exception e) {
            e.printStackTrace(System.out);
            return this.ERROR((Exception)new DataAccessException("TARGET-DAC-000", "Unknown error while loading tenant", (Throwable)e));
        }
    }
    
    @Transactional
    public Response removeTenant(final Request request) {
        final Identifier id = (Identifier)request.get(TenantParams.TENANT_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_TENANT_DATA", "No data object in the request"));
        }
        try {
            final boolean status = this.genericDao.removeById((Class)TenantBase.class, (Serializable)id.getId());
            return this.OK(TenantParams.STATUS_RESPONSE.name(), (BaseValueObject)new BooleanResponse(status));
        }
        catch (Exception ex) {
            ex.printStackTrace(System.out);
            return this.ERROR((Exception)new DataAccessException("TARGET-DAC-000", "Unknown error while deleting tenant", (Throwable)ex));
        }
    }
    
    @Transactional(readOnly = true)
    public Response getTenant(final Request request) {
        final Identifier id = (Identifier)request.get(TenantParams.TENANT_ID.name());
        if (id == null) {
            return this.ERROR((Exception)new DataAccessException("INVALID_TENANT_DATA", "No data object in the request"));
        }
        TenantBase tenantBase = null;
        try {
            tenantBase = (TenantBase)this.genericDao.find((Class)TenantBase.class, (Serializable)id.getId());
        }
        catch (Exception ex) {
            return this.ERROR((Exception)new DataAccessException("TARGET-DAC-000", "Unknown error while loading tenant ", (Throwable)ex));
        }
        if (tenantBase == null) {
            return this.ERROR((Exception)new DataAccessException("TARGET-DAC-001", "tenant id {0} does not exist.", new Object[] { id }));
        }
        final TenantBaseData tenantBaseData = (TenantBaseData)this.modelMapper.map((Object)tenantBase, (Class)TenantBaseData.class);
        return this.OK(TenantParams.TENANT_DATA.name(), (BaseValueObject)tenantBaseData);
    }
    
    @Transactional(readOnly = true)
    public Response getAllTeanats(final Request request) {
        final List<TenantBaseData> result = new ArrayList<TenantBaseData>();
        try {
            final List<TenantBase> data = (List<TenantBase>)this.genericDao.findAll((Class)TenantBase.class);
            for (final TenantBase iterator : data) {
                result.add((TenantBaseData)this.modelMapper.map((Object)iterator, (Class)TenantBaseData.class));
            }
            final BaseValueObjectList list = new BaseValueObjectList();
            list.setValueObjectList((List)result);
            return this.OK(TenantParams.TENANT_TYPE_LIST.name(), (BaseValueObject)list);
        }
        catch (Exception e) {
            return this.ERROR((Exception)new DataAccessException("TARGET-DAC-000", "Unknown error while loading tenant ", (Throwable)e));
        }
    }
}
