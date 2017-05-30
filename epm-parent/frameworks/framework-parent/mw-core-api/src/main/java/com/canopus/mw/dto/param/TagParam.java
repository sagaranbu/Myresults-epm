package com.canopus.mw.dto.param;

public enum TagParam implements IMiddlewareParam
{
    STATUS_RESPONSE, 
    TAG_SUMMARY, 
    TAG_ID, 
    TAG, 
    TAG_SUMMARY_ID, 
    TAG_LIST;
    
    @Override
    public String getParamName() {
        return null;
    }
}
