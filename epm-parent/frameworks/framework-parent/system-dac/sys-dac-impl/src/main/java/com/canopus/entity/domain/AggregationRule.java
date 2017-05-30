package com.canopus.entity.domain;

import com.canopus.dac.*;
import javax.persistence.*;

@Entity
@Table(name = "COR_MET_AGGREGATION_RULE")
public class AggregationRule extends BaseTenantEntity
{
    private static final long serialVersionUID = 397985813881231416L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "AGGREGATION_RULE_ID_SEQ")
    @SequenceGenerator(name = "AGGREGATION_RULE_ID_SEQ", sequenceName = "AGGREGATION_RULE_ID_SEQ")
    @Column(name = "AGGREGATION_RULE_PK_ID", length = 11)
    private Integer id;
    @Column(name = "ORIGIN_TYPE")
    private String originType;
    @Column(name = "ORGIN_ID")
    private String originId;
    @Column(name = "ORIGIN_MECHANISM")
    private String originMechanism;
    @Column(name = "TARGET_ENTITY")
    private String targetEntity;
    @Column(name = "CONNECTOR_KEY")
    private String connectorKey;
    @Column(name = "CONNECTOR")
    private String connector;
    @Column(name = "AGGREGATION_OPERATION")
    private String aggregationOperation;
    @Column(name = "IS_GRAPH_OPERATION")
    private boolean isGraphOperation;
    @Column(name = "POST_TARGET_ID")
    private String postTargetId;
    @Column(name = "IS_DISABLED")
    private boolean isDisabled;
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public String getOriginType() {
        return this.originType;
    }
    
    public void setOriginType(final String originType) {
        this.originType = originType;
    }
    
    public String getOriginId() {
        return this.originId;
    }
    
    public void setOriginId(final String originId) {
        this.originId = originId;
    }
    
    public String getOriginMechanism() {
        return this.originMechanism;
    }
    
    public void setOriginMechanism(final String originMechanism) {
        this.originMechanism = originMechanism;
    }
    
    public String getTargetEntity() {
        return this.targetEntity;
    }
    
    public void setTargetEntity(final String targetEntity) {
        this.targetEntity = targetEntity;
    }
    
    public String getConnectorKey() {
        return this.connectorKey;
    }
    
    public void setConnectorKey(final String connectorKey) {
        this.connectorKey = connectorKey;
    }
    
    public String getConnector() {
        return this.connector;
    }
    
    public void setConnector(final String connector) {
        this.connector = connector;
    }
    
    public String getAggregationOperation() {
        return this.aggregationOperation;
    }
    
    public void setAggregationOperation(final String aggregationOperation) {
        this.aggregationOperation = aggregationOperation;
    }
    
    public boolean isGraphOperation() {
        return this.isGraphOperation;
    }
    
    public void setGraphOperation(final boolean isGraphOperation) {
        this.isGraphOperation = isGraphOperation;
    }
    
    public String getPostTargetId() {
        return this.postTargetId;
    }
    
    public void setPostTargetId(final String postTargetId) {
        this.postTargetId = postTargetId;
    }
    
    public boolean isDisabled() {
        return this.isDisabled;
    }
    
    public void setDisabled(final boolean isDisabled) {
        this.isDisabled = isDisabled;
    }
    
    public static long getSerialversionuid() {
        return 397985813881231416L;
    }
}
