package com.canopus.mw.aggregation;

import com.canopus.mw.dto.*;

public class AggregationRule extends BaseValueObject
{
    private static final long serialVersionUID = 7023071806203175142L;
    private Integer id;
    private String originType;
    private String originId;
    private String originMechanism;
    private String targetEntity;
    private String connectorKey;
    private String connector;
    private String aggregationOperation;
    private boolean isGraphOperation;
    private String postTargetId;
    private boolean isDisabled;
    
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
    
    public AggregationRule() {
    }
    
    public AggregationRule(final String originType, final String originId, final String originMechanism, final String targetEntity, final String connectorKey, final String connector, final String aggregationOperation, final boolean isGraphOperation, final String postTargetId, final boolean isDisabled) {
        this.originType = originType;
        this.originId = originId;
        this.originMechanism = originMechanism;
        this.targetEntity = targetEntity;
        this.connectorKey = connectorKey;
        this.connector = connector;
        this.aggregationOperation = aggregationOperation;
        this.isGraphOperation = isGraphOperation;
        this.postTargetId = postTargetId;
        this.isDisabled = isDisabled;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "AggregationRule [id=" + this.id + ", originType=" + this.originType + ", originId=" + this.originId + ", originMechanism=" + this.originMechanism + ", targetEntity=" + this.targetEntity + ", connectorKey=" + this.connectorKey + ", connector=" + this.connector + ", aggregationOperation=" + this.aggregationOperation + ", isGraphOperation=" + this.isGraphOperation + ", postTargetId=" + this.postTargetId + ", isDisabled=" + this.isDisabled + "]";
    }
}
