package com.canopus.mw.interceptor.param;

import com.canopus.mw.dto.*;

public class PartitionData extends BaseValueObject
{
    private Integer id;
    private Integer partitionId;
    private Integer contextId;
    private Integer loadFactor;
    private Integer users;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public Integer getPartitionId() {
        return this.partitionId;
    }
    
    public void setPartitionId(final Integer partitionId) {
        this.partitionId = partitionId;
    }
    
    public Integer getContextId() {
        return this.contextId;
    }
    
    public void setContextId(final Integer contextId) {
        this.contextId = contextId;
    }
    
    public Integer getLoadFactor() {
        return this.loadFactor;
    }
    
    public void setLoadFactor(final Integer loadFactor) {
        this.loadFactor = loadFactor;
    }
    
    public Integer getUsers() {
        return this.users;
    }
    
    public void setUsers(final Integer users) {
        this.users = users;
    }
}
