package com.canopus.dac.query;

import com.canopus.dac.*;
import com.canopus.dac.query.vo.*;
import java.util.*;

public interface QueryManager extends DataAccessService
{
    public static final String DAC_SERVICE_ID = "DAC_SERVICE_QMGR";
    
    QueryMetadata getQuery(final String p0);
    
    void execute(final String p0, final Map<String, Object> p1);
    
    int executeForInt(final String p0, final Map<String, Object> p1);
    
    Table executeForTable(final String p0, final Map<String, Object> p1);
    
    Object executeForObject(final String p0, final Map<String, Object> p1);
    
    List<Object> executeForList(final String p0, final Map<String, Object> p1);
}
