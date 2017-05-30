package com.canopus.entity;

import com.canopus.mw.*;
import com.canopus.mw.dto.*;

public interface TagManagerService extends MiddlewareService
{
    public static final String TAG_INVALID_INPUT_ERROR = "TAG_INVALID_INPUT_ERROR";
    public static final String TAG_SAVE_ERROR = "TAG_SAVE_PDI_ERROR";
    public static final String TAG_GET_ERROR = null;
    public static final String TAG_DELETE_ERROR = "TAG_DELETE_ERROR";
    public static final String TAGSUM_INVALID_INPUT_ERROR = "TAGSUM_INVALID_INPUT_ERROR";
    public static final String TAGSUM_SAVE_ERROR = "TAGSUM_SAVE_ERROR";
    public static final String TAGSUM_GET_ERROR = "TAGSUM_GET_ERROR";
    public static final String TAGSUM_DELETE_ERROR = "TAGSUM_DELETE_ERROR";
    
    Response createTag(final Request p0);
    
    Response getTag(final Request p0);
    
    Response getAllTags(final Request p0);
    
    Response deleteTag(final Request p0);
    
    Response createTagSummary(final Request p0);
    
    Response getTagSummary(final Request p0);
    
    Response deleteTagSummary(final Request p0);
}
