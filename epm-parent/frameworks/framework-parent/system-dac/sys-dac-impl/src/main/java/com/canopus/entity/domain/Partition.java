package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MET_PARTITION")
public class Partition extends BaseDataAccessEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "PARTITION_ID_SEQ")
    @SequenceGenerator(name = "PARTITION_ID_SEQ", sequenceName = "PARTITION_ID_SEQ")
    @Column(name = "ID", length = 11)
    private Integer id;
    @Column(name = "PARTITION_ID")
    private Integer partitionId;
    @Column(name = "CONTEXT_ID")
    private Integer contextId;
    @Column(name = "LOAD_FACTOR")
    private Integer loadFactor;
    @Column(name = "USERS")
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
