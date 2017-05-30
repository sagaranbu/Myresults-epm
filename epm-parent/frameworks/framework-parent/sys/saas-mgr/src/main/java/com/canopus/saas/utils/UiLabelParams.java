package com.canopus.saas.utils;

import com.canopus.mw.dto.param.*;

public enum UiLabelParams implements IMiddlewareParam
{
    UI_LABEL_BASE_DATA_LIST, 
    UI_LABEL_LANG_DATA_LIST, 
    LOCALE_NAME, 
    UI_PAGE_CODES, 
    UI_LABEL_LANG_DATA, 
    ERROR_CODE, 
    UI_BASE_ID, 
    UI_BASE_DATA;
    
    public String getParamName() {
        return this.name();
    }
}
