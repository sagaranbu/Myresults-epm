package com.canopus.saas.dac;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface UiLabelDataService extends DataAccessService
{
    public static final String LABEL_DAC_SERVICE_ID = "LABEL_DAC_SERVICE";
    public static final String ERR_LABEL_UNKNOWN_EXCEPTION = "LABEL-DAC-000";
    public static final String ERR_INVALID_DATA = "INVALID_DATA";
    
    Response getAllUILables(final Request p0);
    
    Response getUILablesByLocale(final Request p0);
    
    Response getUiLabelByPageCodes(final Request p0);
    
    Response searchLabelLang(final Request p0);
    
    Response saveLabelData(final Request p0);
    
    Response getUILableById(final Request p0);
}
