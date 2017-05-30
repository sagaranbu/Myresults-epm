package com.canopus.dac.query.impl;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MAS_QUERY_PARAM")
public class QueryParameter extends BaseDataAccessEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "COR_QUERY_PARAM_ID_SEQ")
    @SequenceGenerator(name = "COR_QUERY_PARAM_ID_SEQ", sequenceName = "COR_QUERY_PARAM_ID_SEQ")
    @Column(name = "COR_QUERY_PARAM_ID_SEQ", length = 11)
    private Integer id;
    @Column(name = "SQP_AT_NAME", length = 255)
    private String paramName;
    @Column(name = "SQP_AT_DESCR", length = 1024)
    private String description;
    
    @Override
    public Integer getId() {
        return this.id;
    }
    
    @Override
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getParamName() {
        return this.paramName;
    }
    
    public void setParamName(final String paramName) {
        this.paramName = paramName;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(final String description) {
        this.description = description;
    }
}
