package com.kpisoft.strategy.params;

import com.canopus.mw.dto.param.*;

public enum StrategyParams implements IMiddlewareParam
{
    STRATEGY_TEMPLATE, 
    STRATEGY_TEMPLATE_ID, 
    STRATEGY_MAP, 
    STRATEGY_TEMPLATE_LIST, 
    STRATEGY_MAP_LIST;
    
    public String getParamName() {
        return this.name();
    }
}
