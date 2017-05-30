package com.canopus.mw.search;

import com.canopus.mw.dto.*;
import com.canopus.mw.audit.*;
import java.util.*;

public class SearchParams<V, W> extends BaseValueObject
{
    private V orgId;
    private final Map<String, List<Object>> customSearchMap;
    private final Set<AuditStatus> statusSearchSet;
    private W userId;
    private Date fromDate;
    private Date toDate;
    private int entrySize;
    private int startNum;
    private AuditOperation operation;
    private AuditCategory category;
    private String dataChunk;
    
    public SearchParams() {
        this.customSearchMap = new HashMap<String, List<Object>>();
        this.statusSearchSet = new HashSet<AuditStatus>();
        this.userId = null;
        this.fromDate = null;
        this.toDate = null;
        this.entrySize = 10;
        this.startNum = 0;
        this.operation = null;
        this.category = null;
    }
    
    public void setFromDate(final Date fromDate) {
        this.fromDate = fromDate;
    }
    
    public void setToDate(final Date toDate) {
        this.toDate = toDate;
    }
    
    public void setOrgId(final V orgId) {
        this.orgId = orgId;
    }
    
    public void addCustomSearch(final String key, final Object keyword) {
        if (!this.customSearchMap.containsKey(key)) {
            this.customSearchMap.put(key, new ArrayList<Object>());
        }
        this.customSearchMap.get(key).add(keyword);
    }
    
    public void addStatusSearch(final AuditStatus status) {
        this.statusSearchSet.add(status);
    }
    
    public void setEntrySize(final int entrySize) {
        this.entrySize = entrySize;
    }
    
    public void setStartNum(final int startNum) {
        this.startNum = startNum;
    }
    
    public void setOperation(final AuditOperation operation) {
        this.operation = operation;
    }
    
    public void setCategory(final AuditCategory category) {
        this.category = category;
    }
    
    public void setUserId(final W userId) {
        this.userId = userId;
    }
    
    public void setDataChunk(final String dataChunk) {
        this.dataChunk = dataChunk;
    }
    
    public Date getFromDate() {
        return this.fromDate;
    }
    
    public Date getToDate() {
        return this.toDate;
    }
    
    public V getOrgId() {
        return this.orgId;
    }
    
    public Map<String, List<Object>> getCustomSearchMap() {
        return this.customSearchMap;
    }
    
    public Set<AuditStatus> getStatusSearchSet() {
        return this.statusSearchSet;
    }
    
    public int getEntrySize() {
        return this.entrySize;
    }
    
    public int getStartNum() {
        return this.startNum;
    }
    
    public AuditOperation getOperation() {
        return this.operation;
    }
    
    public AuditCategory getCategory() {
        return this.category;
    }
    
    public W getUserId() {
        return this.userId;
    }
    
    public String getDataChunk() {
        return this.dataChunk;
    }
}
