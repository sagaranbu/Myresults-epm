package com.canopus.dac.query.impl;

import com.canopus.dac.*;
import javax.persistence.*;
import javax.persistence.Table;
import com.canopus.dac.query.vo.*;

@Entity
@Table(name = "COR_MAS_QUERY", uniqueConstraints = { @UniqueConstraint(columnNames = { "SMQ_AT_KEY" }) })
public class Query extends BaseDataAccessEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_QUERY_ID_SEQ")
    @SequenceGenerator(name = "COR_QUERY_ID_SEQ", sequenceName = "COR_QUERY_ID_SEQ")
    @Column(name = "COR_QUERY_ID", length = 11)
    private Integer id;
    @Column(name = "SMQ_AT_KEY", length = 255)
    private String key;
    @Column(name = "SMQ_AT_QUERY", length = 1024)
    private String query;
    @Column(name = "SMQ_AT_QUERY_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private QueryType queryType;
    @Column(name = "SMQ_AT_QUERY_ETYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private QueryExecutionType execType;
    @Column(name = "SMQ_AT_RESULT_TYPE", length = 20)
    @Enumerated(EnumType.STRING)
    private ResultType resultType;
    @Column(name = "SMQ_AT_COMP", length = 20)
    private String component;
    @Column(name = "SMQ_AT_STATUS", length = 20)
    private String status;
    @Column(name = "SMQ_AT_VERIFIED", length = 20)
    private boolean verified;
    
    @Override
    public Integer getId() {
        return this.id;
    }
    
    @Override
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
    
    public ResultType getResultType() {
        return this.resultType;
    }
    
    public void setResultType(final ResultType resultType) {
        this.resultType = resultType;
    }
}
