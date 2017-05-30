package com.canopus.mw;

import com.canopus.mw.utils.*;
import org.springframework.beans.factory.annotation.*;
import javax.annotation.*;
import com.canopus.mw.dto.*;

public class DynamicServiceLocator extends RemoteServiceLocator
{
    @Autowired
    PartitionResolver partitionResolver;
    
    @PostConstruct
    @Override
    public void init() {
        super.init();
        this.partitionResolver.init((IServiceLocator)this);
    }
    
    @Override
    public ServiceRouteKey getServiceRouteKey(final String serviceId) {
        final Integer tenantId = ExecutionContext.getTenantId();
        final ServiceRouteKey key = new ServiceRouteKey();
        if (tenantId != null) {
            key.setTenantId(tenantId.toString());
        }
        else {
            key.setTenantId("*");
        }
        key.setServiceId(serviceId);
        String partitionId = null;
        if (serviceId.equals(this.partitionResolver.getServiceName())) {
            partitionId = "*";
        }
        else {
            partitionId = this.partitionResolver.getPartitionID();
        }
        key.setPartitionId(partitionId);
        return key;
    }
    
    @Override
    public String getMiddlewareServiceMapKey(final ServiceRouteKey key) {
        return key.getServiceId() + "_" + key.getTenantId() + "_" + key.getPartitionId();
    }
    
    @Override
    public ServiceRouteDefinition getServiceRouteDefinition(final ServiceRouteKey key) {
        ServiceRouteDefinition servRouteDef = this.routingTable.get(key);
        if (servRouteDef == null) {
            final String tenantId = ExecutionContext.getTenantId().toString();
            key.setTenantId(tenantId);
            key.setPartitionId("*");
            servRouteDef = this.routingTable.get(key);
            if (servRouteDef == null) {
                key.setTenantId("*");
                servRouteDef = this.routingTable.get(key);
                if (servRouteDef == null) {
                    throw new ServiceLocatorException("No service found for the the serviceId {" + key.getServiceId() + "}, Tenant {" + key.getTenantId() + "}");
                }
            }
        }
        return servRouteDef;
    }
    
    @Override
    public void addPartitionToMap(final Integer contextId, final Integer partitionId) {
        this.partitionResolver.addPartitionToMap(contextId, partitionId);
    }
}
