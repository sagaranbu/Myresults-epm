package com.canopus.interceptor;

import com.canopus.dac.*;
import java.util.*;
import com.canopus.interceptor.vo.*;
import java.io.*;

public interface InterceptorDataService extends DataAccessService
{
    public static final String ERR_INTR_SAVE_ADT_LOG = "INTR_SAV_ADT-00";
    public static final String ERR_INTR_SAVE_OPPRF_STATS = "INTR_SAV_OPPRF-00";
    public static final String ERR_INTR_SAVE_USG_STATS = "INTR_SAV_USG-00";
    public static final String ERR_INTR_INVALID_ADTLOG_KEY = "INTR_INV_ADT_KEY-00";
    
    Map<String, AuditLoggingData> saveRequstStatsList(final Map<String, AuditLoggingData> p0);
    
    void saveOperationProfilerList(final List<OperationProfilerData> p0);
    
    void saveUsageInterceptorList(final List<UsageInterceptorData> p0);
    
    Integer saveAuditData(final AuditLoggingData p0);
    
    boolean removeAuditData(final Serializable p0);
}
