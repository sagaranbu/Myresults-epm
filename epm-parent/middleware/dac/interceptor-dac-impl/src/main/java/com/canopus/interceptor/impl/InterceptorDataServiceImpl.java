package com.canopus.interceptor.impl;

import com.canopus.interceptor.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.dto.*;
import org.modelmapper.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import java.util.*;
import com.canopus.interceptor.vo.*;
import com.canopus.interceptor.domain.*;
import java.io.*;

@Component
public class InterceptorDataServiceImpl implements InterceptorDataService
{
    @Autowired
    GenericHibernateDao genericDao;
    
    public InterceptorDataServiceImpl() {
        this.genericDao = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Map<String, AuditLoggingData> saveRequstStatsList(final Map<String, AuditLoggingData> auditLoggingMap) {
        AuditLoggingEntity row = null;
        final Map<String, AuditLoggingData> updatedAuditLogMap = new HashMap<String, AuditLoggingData>();
        try {
            for (final Map.Entry mapEntry : auditLoggingMap.entrySet()) {
                final AuditLoggingData auditLoggingData = (AuditLoggingData) mapEntry.getValue();
                row = new AuditLoggingEntity();
                final ModelMapper modelMapper = new ModelMapper();
                modelMapper.map((Object)auditLoggingData, (Object)row);
                this.genericDao.save((Object)row);
                auditLoggingData.setId(row.getId());
                updatedAuditLogMap.put((String) mapEntry.getKey(), auditLoggingData);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new DataAccessException("INTR_SAV_ADT-00", "Error while saving Audit Logging Interceptor Data.", (Throwable)ex);
        }
        return updatedAuditLogMap;
    }
    
    @Transactional
    public void saveOperationProfilerList(final List<OperationProfilerData> operationProfilerList) {
        OperationProfilerEntity row = null;
        try {
            for (final OperationProfilerData operationProfilerData : operationProfilerList) {
                row = new OperationProfilerEntity();
                final ModelMapper modelMapper = new ModelMapper();
                modelMapper.map((Object)operationProfilerData, (Object)row);
                this.genericDao.save((Object)row);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            throw new DataAccessException("INTR_SAV_OPPRF-00", "Error while saving Operation Profiler Interceptor Stats.", (Throwable)ex);
        }
    }
    
    @Transactional
    public void saveUsageInterceptorList(final List<UsageInterceptorData> usageInterceptorList) {
        UsageInterceptorEntity row = null;
        try {
            for (final UsageInterceptorData usageInterceptorData : usageInterceptorList) {
                row = new UsageInterceptorEntity();
                final ModelMapper modelMapper = new ModelMapper();
                modelMapper.map((Object)usageInterceptorData, (Object)row);
                this.genericDao.save((Object)row);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("INTR_SAV_USG-00", "Error while saving Usage Interceptor stats. ", (Throwable)e);
        }
    }
    
    @Transactional
    public Integer saveAuditData(final AuditLoggingData data) {
        AuditLoggingEntity row = null;
        try {
            if (data.getId() == null) {
                row = new AuditLoggingEntity();
            }
            else {
                row = (AuditLoggingEntity)this.genericDao.find((Class)AuditLoggingEntity.class, (Serializable)data.getId());
            }
            if (row != null) {
                final ModelMapper modelMapper = new ModelMapper();
                modelMapper.map((Object)data, (Object)row);
                this.genericDao.save((Object)row);
                data.setId(row.getId());
                return data.getId();
            }
            throw new DataAccessException("INTR_SAV_ADT-00", "Unknown error while saving entity instance field data ");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("", "Unknown error while saving entity instance field data ", (Throwable)e);
        }
    }
    
    @Transactional
    public boolean removeAuditData(final Serializable id) {
        if (this.genericDao.find((Class)AuditLoggingEntity.class, id) != null) {
            return this.genericDao.removeById((Class)AuditLoggingEntity.class, id);
        }
        throw new DataAccessException("INTR_INV_ADT_KEY-00", "No Entry exist to delete with given Id:" + id);
    }
}
