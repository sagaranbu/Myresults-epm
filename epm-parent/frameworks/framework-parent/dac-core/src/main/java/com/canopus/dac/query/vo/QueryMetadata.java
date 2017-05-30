package com.canopus.dac.query.vo;

import com.canopus.mw.dto.*;

public class QueryMetadata extends BaseValueObject
{
    private Integer id;
    private String key;
    private String query;
    private QueryType queryType;
    private QueryExecutionType execType;
    private ResultType resultType;
    private String component;
    private String status;
    private boolean verified;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getQuery() {
        return this.query;
    }
    
    public void setQuery(final String query) {
        this.query = query;
    }
    
    public QueryType getQueryType() {
        return this.queryType;
    }
    
    public void setQueryType(final QueryType queryType) {
        this.queryType = queryType;
    }
    
    public QueryExecutionType getExecType() {
        return this.execType;
    }
    
    public void setExecType(final QueryExecutionType execType) {
        this.execType = execType;
    }
    
    public ResultType getResultType() {
        return this.resultType;
    }
    
    public void setResultType(final ResultType resultType) {
        this.resultType = resultType;
    }
    
    public String getComponent() {
        return this.component;
    }
    
    public void setComponent(final String component) {
        this.component = component;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(final String status) {
        this.status = status;
    }
    
    public boolean isVerified() {
        return this.verified;
    }
    
    public void setVerified(final boolean verified) {
        this.verified = verified;
    }
}
