package com.canopus.mw.aggregation;

import com.canopus.mw.dto.*;

public class AggregationData extends BaseValueObject
{
    private static final long serialVersionUID = -4097876034985325028L;
    private Object originPayload;
    
    public Object getOriginPayload() {
        return this.originPayload;
    }
    
    public void setOriginPayload(final Object originPayload) {
        this.originPayload = originPayload;
    }
}
