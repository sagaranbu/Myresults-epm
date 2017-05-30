package com.canopus.mw.aggregation;

import java.util.*;

public interface ISummaryDataService
{
    void init();
    
    ISummaryData getSummaryEntity(final int p0);
    
    List<ISummaryData> getSummaryEntities(final List<Integer> p0);
    
    void saveSummaryEntity(final ISummaryData p0);
    
    void saveSummaryEntities(final List<ISummaryData> p0);
    
    String getSummaryEntityName();
}
