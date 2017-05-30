package com.canopus.saas;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface UiLabelManagerService extends MiddlewareService
{
    public static final String ERR_LABEL_UNKNOWN_EXCEPTION = "LABEL-000";
    public static final String ERR_LABEL_DOES_NOT_EXIST = "LABEL-101";
    public static final String ERR_INVALID_DATA = "INVALID_DATA";
    
    Response getAllUILables(final Request p0);
    
    Response getUILablesByLocale(final Request p0);
    
    Response getUiLabelByPageCodes(final Request p0);
    
    Response searchLabelLang(final Request p0);
    
    Response getUILableLangByErrorCode(final Request p0);
    
    Response saveLabelData(final Request p0);
}
