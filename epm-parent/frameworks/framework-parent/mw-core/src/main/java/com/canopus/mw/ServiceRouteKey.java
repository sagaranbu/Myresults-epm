package com.canopus.mw;

public class ServiceRouteKey
{
    private String serviceId;
    private String tenantId;
    private String partitionId;
    
    public String getServiceId() {
        return this.serviceId;
    }
    
    public void setServiceId(final String serviceId) {
        this.serviceId = serviceId;
    }
    
    public String getTenantId() {
        return this.tenantId;
    }
    
    public void setTenantId(final String tenantId) {
        this.tenantId = tenantId;
    }
    
    public String getPartitionId() {
        return this.partitionId;
    }
    
    public void setPartitionId(final String partitionId) {
        this.partitionId = partitionId;
    }
    
    @Override
    public String toString() {
        return "ServiceRouteKey [" + ((this.serviceId != null) ? ("serviceId=" + this.serviceId + ", ") : "") + ((this.tenantId != null) ? ("tenantId=" + this.tenantId) : "") + ((this.partitionId != null) ? ("partitionId=" + this.partitionId) : "") + "]";
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.partitionId == null) ? 0 : this.partitionId.hashCode());
        result = 31 * result + ((this.serviceId == null) ? 0 : this.serviceId.hashCode());
        result = 31 * result + ((this.tenantId == null) ? 0 : this.tenantId.hashCode());
        return result;
    }
    
    @Override
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
        final ServiceRouteKey other = (ServiceRouteKey)obj;
        if (this.partitionId == null) {
            if (other.partitionId != null) {
                return false;
            }
        }
        else if (!this.partitionId.equals(other.partitionId)) {
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
        if (this.tenantId == null) {
            if (other.tenantId != null) {
                return false;
            }
        }
        else if (!this.tenantId.equals(other.tenantId)) {
            return false;
        }
        return true;
    }
}
