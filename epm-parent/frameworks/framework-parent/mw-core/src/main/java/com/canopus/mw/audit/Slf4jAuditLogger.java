package com.canopus.mw.audit;

import com.canopus.mw.util.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import java.io.*;
import java.util.*;
import org.slf4j.*;

public class Slf4jAuditLogger implements AuditLogger
{
    private static final Logger LOG;
    
    public void log(final AuditOperation operationId, final AuditStatus status, final AuditCategory category, final String entityId, final Auditable auditData) {
        try {
            final String jsonStr = TransformerUtil.convertToString(auditData.getAuditData());
            final Map<String, Object> map = (Map<String, Object>)ExecutionContext.getCurrent().getContextValues();
            final String sb = "TENANT_ID:" + map.get(HeaderParam.TENANT_ID.getParamName() + ",TEMP_ORG_NAME:" + map.get(HeaderParam.TEMP_ORG_NAME.getParamName()) + ",USER_NAME:" + map.get(HeaderParam.USER_NAME.getParamName())) + ",OPERATION_ID:" + operationId.name() + ",AUDIT_STATUS:" + status.name() + ",CATEGORY:" + category.name() + ",ENTITY_ID:" + entityId + ",DATA:" + jsonStr;
            Slf4jAuditLogger.LOG.info(sb);
        }
        catch (IOException ex) {
            Slf4jAuditLogger.LOG.error("Error converting map into json string", (Throwable)ex);
        }
    }
    
    static {
        LOG = LoggerFactory.getLogger((Class)Slf4jAuditLogger.class);
    }
}
