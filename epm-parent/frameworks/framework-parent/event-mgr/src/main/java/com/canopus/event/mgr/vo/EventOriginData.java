package com.canopus.event.mgr.vo;

import com.canopus.mw.dto.*;

public class EventOriginData extends BaseValueObject
{
    private static final long serialVersionUID = 1L;
    private String serviceId;
    private String operationId;
    private String operationType;
    
    public EventOriginData() {
    }
    
    public EventOriginData(final String serviceId, final String operationId, final String operationType) {
        this.serviceId = serviceId;
        this.operationId = operationId;
        this.operationType = operationType;
    }
    
    public String getOperationId() {
        return this.operationId;
    }
    
    public void setOperationId(final String operationId) {
        this.operationId = operationId;
    }
    
    public String getOperationType() {
        return this.operationType;
    }
    
    public void setOperationType(final String operationType) {
        this.operationType = operationType;
    }
    
    public String getServiceId() {
        return this.serviceId;
    }
    
    public void setServiceId(final String serviceId) {
        this.serviceId = serviceId;
    }
    
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.operationId == null) ? 0 : this.operationId.hashCode());
        result = 31 * result + ((this.operationType == null) ? 0 : this.operationType.hashCode());
        result = 31 * result + ((this.serviceId == null) ? 0 : this.serviceId.hashCode());
        return result;
    }
    
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final EventOriginData other = (EventOriginData)obj;
        if (this.operationId == null) {
            if (other.operationId != null) {
                return false;
            }
        }
        else if (!this.operationId.equals(other.operationId)) {
            return false;
        }
        if (this.operationType == null) {
            if (other.operationType != null) {
                return false;
            }
        }
        else if (!this.operationType.equals(other.operationType)) {
            return false;
        }
        if (this.serviceId == null) {
            if (other.serviceId != null) {
                return false;
            }
        }
        else if (!this.serviceId.equals(other.serviceId)) {
            return false;
        }
        return true;
    }
    
    public String toString() {
        return "EventOriginData [" + ((this.serviceId != null) ? ("serviceId=" + this.serviceId + ", ") : "") + ((this.operationId != null) ? ("operationId=" + this.operationId + ", ") : "") + ((this.operationType != null) ? ("operationType=" + this.operationType) : "") + "]";
    }
}
