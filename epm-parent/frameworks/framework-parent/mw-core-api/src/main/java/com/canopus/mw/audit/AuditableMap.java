package com.canopus.mw.audit;

import java.util.*;
import java.text.*;

public final class AuditableMap implements Auditable
{
    private final Map<String, Object> map;
    
    private AuditableMap() {
        this.map = new HashMap<String, Object>();
    }
    
    public AuditableMap put(final String key, final String value) {
        return this.put(key, (Object)value);
    }
    
    public AuditableMap put(final String key, final List<Auditable> values) {
        final List<Map<String, Object>> auditableList = new ArrayList<Map<String, Object>>();
        for (final Auditable auditable : values) {
            auditableList.add(auditable.getAuditData());
        }
        return this.put(key, auditableList);
    }
    
    public AuditableMap put(final String key, final Number value) {
        return this.put(key, (Object)value);
    }
    
    public AuditableMap put(final String key, final Date value) {
        return this.put(key, new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(value));
    }
    
    public AuditableMap put(final String key, final Auditable value) {
        return this.put(key, value.getAuditData());
    }
    
    private AuditableMap put(final String key, final Object value) {
        this.map.put(key, value);
        return this;
    }
    
    public static AuditableMap newInstance() {
        return new AuditableMap();
    }
    
    @Override
    public Map<String, Object> getAuditData() {
        return this.map;
    }
}
