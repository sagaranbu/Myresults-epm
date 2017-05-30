package com.canopus.mw;

import java.util.*;
import javax.naming.*;
import org.slf4j.*;

public class EmbeddedContainerServiceLocator implements IServiceLocator
{
    private static final Logger LOGGER;
    private static Context context;
    private Map<String, String> serviceMap;
    private Map<String, String> serviceMap2;
    private Map<String, String> serviceMap3;
    private Map<String, String> tempMap;
    
    public EmbeddedContainerServiceLocator() {
        this.serviceMap = new HashMap<String, String>();
        this.serviceMap2 = new HashMap<String, String>();
        this.serviceMap3 = new HashMap<String, String>();
        this.tempMap = new HashMap<String, String>();
    }
    
    public Map<String, String> getServiceMap() {
        return this.serviceMap;
    }
    
    public void setServiceMap(final Map<String, String> serviceMap) {
        this.serviceMap = serviceMap;
    }
    
    public Map<String, String> getServiceMap2() {
        return this.serviceMap2;
    }
    
    public void setServiceMap2(final Map<String, String> serviceMap2) {
        this.serviceMap2 = serviceMap2;
    }
    
    public void init() {
        EmbeddedContainerServiceLocator.LOGGER.info("Embedded service locator initializing with " + this.serviceMap.keySet().size() + " services in map 1, " + this.serviceMap2.keySet().size() + " services in map 2" + "-----" + this.serviceMap3.keySet().size() + "Services in map 3");
        for (final String key : this.serviceMap.keySet()) {
            this.tempMap.put(key.trim(), this.serviceMap.get(key).trim());
        }
        this.serviceMap.clear();
        this.serviceMap.putAll(this.tempMap);
        this.tempMap.clear();
        for (final String key : this.serviceMap2.keySet()) {
            this.tempMap.put(key.trim(), this.serviceMap2.get(key).trim());
        }
        this.serviceMap2.putAll(this.tempMap);
    }
    
    public MiddlewareService getService(final String serviceID) {
        System.out.println("serviceID" + serviceID);
        MiddlewareService service = null;
        try {
            final String servicePath = this.serviceMap.get(serviceID);
            if (servicePath == null) {
                EmbeddedContainerServiceLocator.LOGGER.warn("Service not configured in servicelocator.xml");
                return null;
            }
            EmbeddedContainerServiceLocator.LOGGER.debug("Looking for " + serviceID + " - java:" + servicePath);
            service = (MiddlewareService)EmbeddedContainerServiceLocator.context.lookup("java:" + servicePath);
            System.out.println(service);
        }
        catch (NamingException ne) {
            EmbeddedContainerServiceLocator.LOGGER.warn("Service not found using serviceMap1 - " + ne.getMessage());
            try {
                final String servicePath2 = this.serviceMap2.get(serviceID);
                if (servicePath2 == null) {
                    EmbeddedContainerServiceLocator.LOGGER.warn("Service not configured in serviceMap2");
                    return null;
                }
                EmbeddedContainerServiceLocator.LOGGER.debug("Looking for(map2) " + serviceID + " - java:" + servicePath2);
                service = (MiddlewareService)EmbeddedContainerServiceLocator.context.lookup("java:" + servicePath2);
            }
            catch (NamingException e) {
                try {
                    final String servicePath3 = this.serviceMap3.get(serviceID);
                    if (servicePath3 == null) {
                        EmbeddedContainerServiceLocator.LOGGER.warn("Service not configured in serviceMap2");
                        return null;
                    }
                    EmbeddedContainerServiceLocator.LOGGER.debug("Looking for service in map3 " + serviceID + " - java:" + servicePath3);
                    service = (MiddlewareService)EmbeddedContainerServiceLocator.context.lookup("java:" + servicePath3);
                    return service;
                }
                catch (NamingException e3) {
                    throw new ServiceLocatorException("Unable to find the service", (Throwable)e);
                }
            }
        }
        catch (Exception e2) {
            throw new ServiceLocatorException("Unknown Error", (Throwable)e2);
        }
        return service;
    }
    
    public void destroy() {
    }
    
    public static void setEjbContext(final Context context) {
        EmbeddedContainerServiceLocator.context = context;
    }
    
    public MiddlewareService borrowService(final String serviceId) {
        return this.getService(serviceId);
    }
    
    public void returnService(final MiddlewareService service) {
    }
    
    public MiddlewareService getConsumerService(final String serviceId) {
        return null;
    }
    
    public void addPartitionToMap(final Integer contextId, final Integer partitionId) {
    }
    
    public Map<String, String> getServiceMap3() {
        return this.serviceMap3;
    }
    
    public void setServiceMap3(final Map<String, String> serviceMap3) {
        this.serviceMap3 = serviceMap3;
    }
    
    static {
        LOGGER = LoggerFactory.getLogger((Class)EmbeddedContainerServiceLocator.class);
    }
}
