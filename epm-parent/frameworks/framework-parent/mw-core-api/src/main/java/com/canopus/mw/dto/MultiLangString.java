package com.canopus.mw.dto;

import java.util.*;
import com.canopus.mw.dto.param.*;

public class MultiLangString
{
    private Map<String, String> attribute;
    
    public MultiLangString() {
        this.attribute = new HashMap<String, String>();
    }
    
    public Map<String, String> getAttribute() {
        return this.attribute;
    }
    
    @Override
    public String toString() {
        if (ExecutionContext.getCurrent().getContextValues().get(HeaderParam.USER_LOCALE.getParamName()) != null) {
            return this.attribute.get(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.USER_LOCALE.getParamName()));
        }
        if (ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_LOCALE.getParamName()) != null) {
            return this.attribute.get(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_LOCALE.getParamName()));
        }
        return this.attribute.get(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.SYSTEM_LOCALE.getParamName()));
    }
}
