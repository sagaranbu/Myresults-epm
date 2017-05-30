package com.canopus.entity.impl;

import com.canopus.entity.*;
import com.canopus.entity.dao.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.interceptor.param.*;
import com.canopus.entity.domain.*;
import com.canopus.mw.*;
import org.modelmapper.*;
import java.util.*;
import com.canopus.mw.vo.params.*;
import java.lang.reflect.*;
import org.springframework.transaction.annotation.*;
import com.canopus.mw.dto.*;
import com.canopus.dac.*;
import com.googlecode.genericdao.search.*;
import java.io.*;

public class PartitionDataServiceImpl extends BaseDataAccessService implements PartitionDataService
{
    @Autowired
    private PartitionDao partitionDao;
    private ModelMapper modelMapper;
    private Integer defaultPartitionValue;
    
    public PartitionDataServiceImpl() {
        this.partitionDao = null;
        this.modelMapper = null;
        TransformationHelper.createTypeMap(this.modelMapper = new ModelMapper(), (Class)PartitionData.class, (Class)Partition.class);
    }
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    @Transactional(readOnly = true)
    public Response getAllPartitions(final Request request) {
        final BaseValueObjectList list = new BaseValueObjectList();
        final List<Partition> partitionList = (List<Partition>)this.partitionDao.findAll();
        if (partitionList != null && partitionList.size() > 0) {
            final Type listType = new TypeToken<List<PartitionData>>() {}.getType();
            final List<PartitionData> partitions = (List<PartitionData>)this.modelMapper.map((Object)partitionList, listType);
            list.setValueObjectList((List)partitions);
        }
        return this.OK(PartitionParams.PARTITION_LIST.name(), (BaseValueObject)list);
    }
    
    @Transactional(readOnly = true)
    public Response getPartitionId(final Request request) {
        final Identifier contextId = (Identifier)request.get(PartitionParams.CONTEXT_ID.name());
        if (contextId == null) {
            return this.ERROR((Exception)new DataAccessException("ERR_NO_KEY", "No Key in the request to get Partition"));
        }
        final Search search = new Search();
        search.addFilterEqual("contextId", (Object)contextId.getId());
        final Partition partition = (Partition)this.partitionDao.searchUnique((ISearch)search);
        if (partition == null) {
            return this.OK(PartitionParams.PARTITION_ID.name(), (BaseValueObject)new Identifier(this.defaultPartitionValue));
        }
        return this.OK(PartitionParams.PARTITION_ID.name(), (BaseValueObject)new Identifier(partition.getPartitionId()));
    }
    
    @Transactional
    public Response savePartition(final Request request) {
        final PartitionData partitionData = (PartitionData)request.get(PartitionParams.PARTITION.name());
        Partition partition = null;
        if (partitionData == null) {
            return this.ERROR((Exception)new DataAccessException("ERR_PARTITION_INVALID_INPUT", "No data in request."));
        }
        if (partitionData.getId() == null) {
            partition = new Partition();
        }
        else {
            partition = (Partition)this.partitionDao.find((Serializable)partitionData.getId());
            if (partition == null) {
                return this.ERROR((Exception)new DataAccessException("ERR_PARTITION_INVALID_ID", "Invalid Id to update the partitiion."));
            }
        }
        this.modelMapper.map((Object)partitionData, (Object)partition);
        partition = (Partition)this.partitionDao.merge((Object)partition);
        partitionData.setId(partition.getId());
        return this.OK(PartitionParams.PARTITION.name(), (BaseValueObject)partitionData);
    }
    
    public Integer getDefaultPartitionValue() {
        return this.defaultPartitionValue;
    }
    
    public void setDefaultPartitionValue(final Integer defaultPartitionValue) {
        this.defaultPartitionValue = defaultPartitionValue;
    }
}
