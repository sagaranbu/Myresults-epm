package com.canopus.mw.utils;

import com.canopus.mw.*;
import com.canopus.mw.vo.params.*;
import com.canopus.mw.interceptor.param.*;
import java.util.*;
import com.canopus.mw.dto.*;

public class PartitionResolver
{
    private Map<Integer, Integer> partitionMap;
    private String key;
    private Integer value;
    private IServiceLocator serviceLocator;
    private String SERVICE_NAME;
    
    public PartitionResolver() {
        this.partitionMap = null;
        this.SERVICE_NAME = "PartitionManagerServiceImpl";
    }
    
    public String getServiceName() {
        return this.SERVICE_NAME;
    }
    
    public void init(final IServiceLocator serviceLocator) {
        this.serviceLocator = serviceLocator;
    }
    
    private void loadPartitionMap() {
        final PartitionManagerService partitionService = (PartitionManagerService)this.serviceLocator.getService(this.SERVICE_NAME);
        final Response response = partitionService.getAllPartitions(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(PartitionParams.PARTITION_LIST.name());
        final List<PartitionData> partitions = (List<PartitionData>)list.getValueObjectList();
        if (partitions != null && partitions.size() > 0) {
            this.partitionMap.clear();
            for (final PartitionData partition : partitions) {
                this.partitionMap.put(partition.getContextId(), partition.getPartitionId());
            }
        }
    }
    
    public synchronized void getPartitionMap() {
        if (this.partitionMap == null) {
            this.partitionMap = new HashMap<Integer, Integer>();
            this.loadPartitionMap();
        }
    }
    
    public String getPartitionID() {
        if (this.partitionMap == null) {
            this.getPartitionMap();
        }
        if (this.key == null) {
            this.key = "ORG_ID";
        }
        final Object valueOfKey = ExecutionContext.getCurrent().getContextValues().get(this.key);
        Integer partitionId = this.partitionMap.get(valueOfKey);
        if (partitionId != null) {
            return partitionId.toString();
        }
        if (this.serviceLocator == null) {
            return "*";
        }
        partitionId = this.partitionMap.get(valueOfKey);
        if (partitionId == null) {
            return "*";
        }
        return partitionId.toString();
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(final Integer value) {
        this.value = value;
    }
    
    public void addPartitionToMap(final Integer contextId, final Integer partitionId) {
        if (this.partitionMap == null) {
            this.partitionMap = new HashMap<Integer, Integer>();
        }
        this.partitionMap.put(contextId, partitionId);
    }
}
