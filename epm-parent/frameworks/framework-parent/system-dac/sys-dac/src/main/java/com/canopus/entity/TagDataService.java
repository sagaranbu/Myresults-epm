package com.canopus.entity;

import com.canopus.dac.*;
import com.canopus.mw.dto.*;

public interface TagDataService extends DataAccessService
{
    public static final String OBPDAC_INVALID_INPUT_ERROR = "OBPDAC_INVALID_INPUT_ERROR";
    public static final String OBPTAGSUMDAC_SAVE_ERROR = "OBPTAGSUMDAC_SAVE_ERROR";
    public static final String ERR_OBPDAC_UNKNOWN_EXCEPTION = "ERR_OBPDAC_UNKNOWN_EXCEPTION";
    public static final String ERR_TAG_UNABLE_TO_DELETE = "ERR_TAG_UNABLE_TO_DELETE";
    public static final String ERR_CST_UNABLE_TO_GET = "ERR_CST_UNABLE_TO_GET";
    public static final String OBPTAGDAC_SAVE_ERROR = "OBPTAGDAC_SAVE_ERROR";
    public static final String ERR_CMT_UNABLE_TO_GET = "ERR_CMT_UNABLE_TO_GET";
    public static final String ERR_TAG_SUM_UNABLE_TO_DELETE = "ERR_TAG_SUM_UNABLE_TO_DELETE";
    public static final String ERR_CMT_UNABLE_TO_GET_ALL = "ERR_CMT_UNABLE_TO_GET_ALL";
    
    Response saveTag(final Request p0);
    
    Response getTag(final Request p0);
    
    Response deleteTag(final Request p0);
    
    Response getAllTags(final Request p0);
    
    Response saveTagSummary(final Request p0);
    
    Response getTagSummary(final Request p0);
    
    Response deleteTagSummary(final Request p0);
}
