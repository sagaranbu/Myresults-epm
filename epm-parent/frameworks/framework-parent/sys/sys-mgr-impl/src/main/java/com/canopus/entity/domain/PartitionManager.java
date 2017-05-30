package com.canopus.entity.domain;

import com.canopus.mw.*;
import org.springframework.stereotype.*;
import com.canopus.entity.*;
import org.springframework.beans.factory.annotation.*;
import java.util.*;
import com.canopus.mw.interceptor.param.*;
import com.canopus.mw.vo.params.*;
import com.canopus.mw.dto.*;

@Component
public class PartitionManager extends BaseDomainManager
{
    @Autowired
    PartitionDataService dataService;
    
    public List<PartitionData> getAllPartitions() {
        final Response response = this.dataService.getAllPartitions(new Request());
        final BaseValueObjectList list = (BaseValueObjectList)response.get(PartitionParams.PARTITION_LIST.name());
        final List<PartitionData> partitions = (List<PartitionData>)list.getValueObjectList();
        return partitions;
    }
    
    public PartitionData savePartition(final PartitionData partitionData) {
        final Request request = new Request();
        request.put(PartitionParams.PARTITION.name(), (BaseValueObject)partitionData);
        final Response response = this.dataService.savePartition(request);
        final PartitionData partition = (PartitionData)response.get(PartitionParams.PARTITION.name());
        return partition;
    }
}
