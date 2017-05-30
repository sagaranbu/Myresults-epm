package com.canopus.event.mgr;

import com.canopus.mw.aggregation.*;
import java.util.*;

public interface AggregationRuleManager
{
    AggregationRule getAggregationRule(final Integer p0);
    
    Integer saveAggregationRule(final AggregationRule p0);
    
    List<AggregationRule> getAllAggregationRules();
}
