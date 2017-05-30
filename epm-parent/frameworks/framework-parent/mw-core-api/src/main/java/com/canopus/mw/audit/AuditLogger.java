package com.canopus.mw.audit;

public interface AuditLogger
{
    void log(final AuditOperation p0, final AuditStatus p1, final AuditCategory p2, final String p3, final Auditable p4);
}
