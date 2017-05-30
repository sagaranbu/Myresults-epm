package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface PartitionDataService extends DataAccessService
{
    public static final String ERR_NO_KEY = "ERR_NO_KEY";
    public static final String ERR_PARTITION_INVALID_INPUT = "ERR_PARTITION_INVALID_INPUT";
    public static final String ERR_PARTITION_INVALID_ID = "ERR_PARTITION_INVALID_ID";
    
    Response getAllPartitions(final Request p0);
    
    Response getPartitionId(final Request p0);
    
    Response savePartition(final Request p0);
}
