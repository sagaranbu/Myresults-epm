package com.canopus.interceptor.impl;

import com.canopus.interceptor.*;
import org.springframework.stereotype.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.dto.*;
import java.io.*;
import org.modelmapper.*;
import com.canopus.dac.*;
import org.springframework.transaction.annotation.*;
import com.canopus.interceptor.vo.*;
import com.canopus.interceptor.domain.*;

@Component
public class TestEntityDataServiceImpl implements TestEntityDataService
{
    @Autowired
    GenericHibernateDao genericDao;
    
    public TestEntityDataServiceImpl() {
        this.genericDao = null;
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional
    public Integer saveTestEntity1(final TestEntity1Data data) {
        TestEntity1 row = null;
        try {
            if (data.getId() == null) {
                row = new TestEntity1();
            }
            else {
                row = (TestEntity1)this.genericDao.find((Class)TestEntity1.class, (Serializable)data.getId());
            }
            if (row != null) {
                final ModelMapper modelMapper = new ModelMapper();
                modelMapper.map((Object)data, (Object)row);
                this.genericDao.save((Object)row);
                data.setId(row.getId());
                return data.getId();
            }
            throw new DataAccessException("ENT1_SAV-000", "Unknown error while saving entity instance field data ");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("", "Unknown error while saving entity instance field data ", (Throwable)e);
        }
    }
    
    @Transactional
    public Integer saveTestEntity2(final TestEntity2Data data) {
        TestEntity2 row = null;
        try {
            if (data.getId() == null) {
                row = new TestEntity2();
            }
            else {
                row = (TestEntity2)this.genericDao.find((Class)TestEntity2.class, (Serializable)data.getId());
            }
            if (row != null) {
                final ModelMapper modelMapper = new ModelMapper();
                modelMapper.map((Object)data, (Object)row);
                this.genericDao.save((Object)row);
                data.setId(row.getId());
                return data.getId();
            }
            throw new DataAccessException("ENT2_SAV-000", "Unknown error while saving entity instance field data ");
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new DataAccessException("", "Unknown error while saving entity instance field data ", (Throwable)e);
        }
    }
}
