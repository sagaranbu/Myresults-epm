package com.canopus.mw.audit;

import org.slf4j.*;

public final class AuditLoggerFactory
{
    private static final Logger LOG;
    private static AuditLogger AUDIT_LOGGER;
    
    public AuditLoggerFactory(final AuditLogger al) {
        AuditLoggerFactory.AUDIT_LOGGER = al;
        AuditLoggerFactory.LOG.info("Initialized AuditLoggerFactory. AuditLogger implementation used is " + al.getClass().getName());
    }
    
    public static AuditLogger getLogger() {
        if (AuditLoggerFactory.AUDIT_LOGGER == null) {
            throw new IllegalStateException("Audit Logger has not been set! Please configure this before starting server.");
        }
        return AuditLoggerFactory.AUDIT_LOGGER;
    }
    
    static {
        LOG = LoggerFactory.getLogger((Class)AuditLoggerFactory.class);
        AuditLoggerFactory.AUDIT_LOGGER = null;
    }
}
