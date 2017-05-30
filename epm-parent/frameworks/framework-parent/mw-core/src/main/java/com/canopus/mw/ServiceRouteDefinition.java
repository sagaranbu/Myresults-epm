package com.canopus.mw;

public class ServiceRouteDefinition
{
    private String serviceId;
    private String tenantId;
    private String remoteHost;
    private String remotePort;
    private String remoteLookup;
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
    
    public String getRemoteHost() {
        return this.remoteHost;
    }
    
    public void setRemoteHost(final String remoteHost) {
        this.remoteHost = remoteHost;
    }
    
    public String getRemotePort() {
        return this.remotePort;
    }
    
    public void setRemotePort(final String remotePort) {
        this.remotePort = remotePort;
    }
    
    public String getRemoteLookup() {
        return this.remoteLookup;
    }
    
    public void setRemoteLookup(final String remoteLookup) {
        this.remoteLookup = remoteLookup;
    }
    
    public String getPartitionId() {
        return this.partitionId;
    }
    
    public void setPartitionId(final String partitionId) {
        this.partitionId = partitionId;
    }
    
    @Override
    public String toString() {
        return "ServiceRouteDefinition [" + ((this.serviceId != null) ? ("serviceId=" + this.serviceId + ", ") : "") + ((this.tenantId != null) ? ("tenantId=" + this.tenantId + ", ") : "") + ((this.remoteHost != null) ? ("remoteHost=" + this.remoteHost + ", ") : "") + ((this.remotePort != null) ? ("remotePort=" + this.remotePort + ", ") : "") + ((this.remoteLookup != null) ? ("remoteLookup=" + this.remoteLookup) : "") + ((this.partitionId != null) ? ("partitionId=" + this.partitionId) : "") + "]";
    }
}
