package com.canopus.dac.query.impl;

import com.canopus.dac.query.vo.*;
import java.util.*;

public interface QueryExecutor
{
    void execute(final QueryMetadata p0, final Map<String, Object> p1);
    
    int executeForInt(final QueryMetadata p0, final Map<String, Object> p1);
    
    Table executeForTable(final QueryMetadata p0, final Map<String, Object> p1);
    
    Object executeForObject(final QueryMetadata p0, final Map<String, Object> p1);
    
    List<Object> executeForList(final QueryMetadata p0, final Map<String, Object> p1);
}
