package com.canopus.mw.aggregation;

import java.util.*;

public interface ISummaryManager
{
    void init();
    
    ISummaryData getSummaryEntity(final int p0);
    
    List<ISummaryData> getSummaryEntities(final List<Integer> p0);
    
    int saveSummaryEntity(final ISummaryData p0);
    
    int saveSummaryEntities(final List<ISummaryData> p0);
    
    String getSummaryEntityName();
    
    List<ISummaryData> getParentSummaryEntities(final List<Integer> p0);
    
    List<Integer> getParentIds(final List<Integer> p0);
}
