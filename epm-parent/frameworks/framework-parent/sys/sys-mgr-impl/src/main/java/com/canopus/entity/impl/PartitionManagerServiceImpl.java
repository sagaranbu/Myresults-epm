package com.canopus.entity.impl;

import javax.ejb.*;
import javax.interceptor.*;
import org.springframework.ejb.interceptor.*;
import com.canopus.mw.facade.*;
import com.canopus.entity.domain.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.interceptor.param.*;
import java.util.*;
import com.canopus.mw.vo.params.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.*;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Lock(LockType.READ)
@Remote({ PartitionManagerService.class })
@Interceptors({ SpringBeanAutowiringInterceptor.class, MiddlewareBeanInterceptor.class })
public class PartitionManagerServiceImpl extends BaseMiddlewareBean implements PartitionManagerService
{
    @Autowired
    PartitionManager partitionManager;
    
    public StringIdentifier getServiceId() {
        final StringIdentifier identifier = new StringIdentifier();
        identifier.setId(this.getClass().getSimpleName());
        return identifier;
    }
    
    public Response getAllPartitions(final Request request) {
        List<PartitionData> partitions = this.partitionManager.getAllPartitions();
        if (partitions == null) {
            partitions = new ArrayList<PartitionData>();
        }
        final BaseValueObjectList list = new BaseValueObjectList();
        list.setValueObjectList((List)partitions);
        return this.OK(PartitionParams.PARTITION_LIST.name(), (BaseValueObject)list);
    }
    
    public Response getPartition(final Request request) {
        return null;
    }
    
    public Response savePartition(final Request request) {
        PartitionData partitionData = (PartitionData)request.get(PartitionParams.PARTITION.name());
        if (partitionData == null) {
            return this.ERROR((Exception)new MiddlewareException("ERR_PARTITION_INVALID_INPUT", "No data in request."));
        }
        partitionData = this.partitionManager.savePartition(partitionData);
        return this.OK(PartitionParams.PARTITION.name(), (BaseValueObject)partitionData);
    }
}
