package com.canopus.mw.audit;

import org.fluentd.logger.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import java.util.*;
import org.slf4j.*;

public class FluentdAuditLogger implements AuditLogger
{
    private static final Logger LOG;
    private final FluentLogger logger;
    
    public FluentdAuditLogger(final String tag, final String hostname, final int port) {
        this.logger = FluentLogger.getLogger(tag, hostname, port);
        this.addShutdownHook();
        FluentdAuditLogger.LOG.info("Initialized FluentdAuditLogger.");
    }
    
    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                FluentdAuditLogger.this.logger.close();
            }
        });
    }
    
    public void log(final AuditOperation op, final AuditStatus status, final AuditCategory category, final String entityId, final Auditable auditData) {
        final Map<String, Object> dataMap = new HashMap<String, Object>();
        final Map<String, Object> ctxMap = (Map<String, Object>)ExecutionContext.getCurrent().getContextValues();
        final Object username = ctxMap.get(HeaderParam.USER_NAME.getParamName());
        final Object orgName = ctxMap.get(HeaderParam.TEMP_ORG_NAME.getParamName());
        dataMap.put(HeaderParam.USER_NAME.getParamName(), username);
        dataMap.put(HeaderParam.TEMP_ORG_NAME.getParamName(), orgName);
        dataMap.put(AuditLogEnum.CATEGORY.name(), category.name());
        dataMap.put(AuditLogEnum.ENTITY_ID.name(), entityId);
        dataMap.put(AuditLogEnum.OPERATION_ID.name(), op.name());
        dataMap.put(AuditLogEnum.STATUS.name(), status.name());
        dataMap.put(AuditLogEnum.DATA.name(), auditData.getAuditData());
        this.logger.log(ctxMap.get(HeaderParam.TENANT_ID.getParamName()).toString(), (Map)dataMap);
        FluentdAuditLogger.LOG.debug("Writing to FluentdAuditLogger: User-{}, Org-{}", username, orgName);
    }
    
    static {
        LOG = LoggerFactory.getLogger((Class)FluentdAuditLogger.class);
    }
}
