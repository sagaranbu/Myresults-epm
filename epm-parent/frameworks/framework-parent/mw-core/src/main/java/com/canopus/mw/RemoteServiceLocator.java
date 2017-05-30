package com.canopus.mw;

import org.apache.log4j.*;
import org.springframework.beans.factory.annotation.*;
import javax.annotation.*;
import java.util.*;
import javax.naming.*;
import com.canopus.mw.dto.*;
import org.modelmapper.*;
import java.io.*;
import org.perf4j.log4j.*;
import org.perf4j.*;
import com.canopus.mw.dto.param.*;
import javax.ejb.*;
import java.lang.reflect.*;

public class RemoteServiceLocator implements IServiceLocator
{
    private static Logger logger;
    protected Map<ServiceRouteKey, ServiceRouteDefinition> routingTable;
    private Map<String, MiddlewareService> middleWareServiceMap;
    private boolean isJboss;
    @Value("${remoting.connectionurl:'127.0.0.1:4447'}")
    private String remotingConnectionUrl;
    private String remotingHost;
    private String remotingPort;
    private InitialContext localContext;
    private ModelMapper mapper;
    
    public RemoteServiceLocator() {
        this.middleWareServiceMap = new HashMap<String, MiddlewareService>();
        this.remotingConnectionUrl = "127.0.0.1:4447";
        this.remotingHost = "127.0.0.1";
        this.remotingPort = "4447";
        this.mapper = new ModelMapper();
    }
    
    @PostConstruct
    public void init() {
        RemoteServiceLocator.logger.info((Object)("Remote service locator initializing with " + this.routingTable.keySet().size() + " services"));
        this.isJboss = (System.getProperty("jboss.home.dir") != null);
        if (this.isJboss) {
            final String[] split = this.remotingConnectionUrl.split(":");
            this.remotingHost = ("localhost".equals(split[0]) ? "127.0.0.1" : split[0]);
            this.remotingPort = split[1];
        }
    }
    
    public Map<ServiceRouteKey, ServiceRouteDefinition> getRoutingTable() {
        return this.routingTable;
    }
    
    public void setRoutingTable(final Map<ServiceRouteKey, ServiceRouteDefinition> routingTable) {
        this.routingTable = routingTable;
    }
    
    private boolean isLocal(final String host, final String port) {
        return this.isJboss && this.remotingHost.equals(host) && this.remotingPort.equals(port);
    }
    
    private InitialContext initContextForUrl(final String host, final String port) {
        InitialContext context = null;
        final Properties props = new Properties();
        props.put("org.jboss.ejb.client.scoped.context", true);
        props.put("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
        props.put("endpoint.name", "endpoint-client");
        final String connection = "server2";
        props.put("remote.connections", connection);
        props.put("remote.connection." + connection + ".host", host);
        props.put("remote.connection." + connection + ".port", port);
        props.put("remote.connection." + connection + ".connect.options.org.xnio.Options.SASL_POLICY_NOANONYMOUS", "false");
        props.put("remote.connection." + connection + ".connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "true");
        props.put("remote.connection." + connection + ".channel.options.org.jboss.remoting3.RemotingOptions.MAX_OUTBOUND_MESSAGES", "2000");
        props.put("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        try {
            context = new InitialContext(props);
        }
        catch (NamingException e) {
            RemoteServiceLocator.logger.error((Object)("Error initializing context for " + host + ":" + port), (Throwable)e);
        }
        return context;
    }
    
    private InitialContext initContextForLocalUrl() {
        InitialContext context = null;
        if (this.localContext == null) {
            try {
                this.localContext = new InitialContext();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        context = this.localContext;
        return context;
    }
    
    public MiddlewareService getService(final String serviceId) {
        return this.getService(serviceId, true);
    }
    
    public MiddlewareService getConsumerService(final String serviceId) {
        MiddlewareService service = null;
        try {
            service = this.middleWareServiceMap.get(serviceId);
            if (service != null) {
                return service;
            }
            final Integer tenantIdVal = ExecutionContext.getTenantId();
            final String tenantId = tenantIdVal.toString();
            final ServiceRouteKey key = new ServiceRouteKey();
            key.setTenantId(tenantId);
            key.setServiceId(serviceId);
            ServiceRouteDefinition servRouteDef = this.routingTable.get(key);
            if (servRouteDef == null) {
                key.setTenantId("*");
                servRouteDef = this.routingTable.get(key);
                if (servRouteDef == null) {
                    throw new ServiceLocatorException("No service found for the the serviceId {" + serviceId + "}, Tenant {" + tenantId + "}");
                }
            }
            final Type destinationType = new TypeToken<ServiceRouteDefinition>() {}.getType();
            final ServiceRouteDefinition servRouteDef2 = (ServiceRouteDefinition)this.mapper.map((Object)servRouteDef, destinationType);
            final String war = servRouteDef.getRemoteLookup().split(".")[0];
            final String rout = war + "/mw-service-core/MiddlewareQueryService!com.canopus.mw.IMiddlewareQueryService";
            servRouteDef2.setRemoteLookup(rout);
            servRouteDef2.setServiceId("MiddlewareQueryService");
            final Object mwService = this.createMiddlewareService(servRouteDef2);
            final InvocationHandler handler = new DynamicRemoteProxyInvocationHandler(mwService, servRouteDef);
            final String serviceInterface = servRouteDef.getRemoteLookup().split("\\!")[1];
            service = (MiddlewareService)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { Class.forName(serviceInterface), Closeable.class }, handler);
            return service;
        }
        catch (Exception e) {
            throw new ServiceLocatorException("Unknown Error", (Throwable)e);
        }
    }
    
    public MiddlewareService borrowService(final String serviceId) {
        return this.getService(serviceId, false);
    }
    
    public void returnService(final MiddlewareService service) {
    }
    
    public ServiceRouteKey getServiceRouteKey(final String serviceId) {
        final String tenantId = ExecutionContext.getTenantId().toString();
        final ServiceRouteKey key = new ServiceRouteKey();
        key.setTenantId(tenantId);
        key.setServiceId(serviceId);
        return key;
    }
    
    public ServiceRouteDefinition getServiceRouteDefinition(final ServiceRouteKey key) {
        ServiceRouteDefinition servRouteDef = this.routingTable.get(key);
        if (servRouteDef == null) {
            key.setTenantId("*");
            servRouteDef = this.routingTable.get(key);
            if (servRouteDef == null) {
                throw new ServiceLocatorException("No service found for the the serviceId {" + key.getServiceId() + "}, Tenant {" + key.getTenantId() + "}");
            }
        }
        return servRouteDef;
    }
    
    public String getMiddlewareServiceMapKey(final ServiceRouteKey key) {
        return key.getServiceId() + "_" + key.getTenantId();
    }
    
    protected MiddlewareService getService(final String serviceId, final boolean isAutoReturn) {
        MiddlewareService service = null;
        try {
            final ServiceRouteKey key = this.getServiceRouteKey(serviceId);
            final String mapKey = this.getMiddlewareServiceMapKey(key);
            service = this.middleWareServiceMap.get(mapKey);
            if (service != null) {
                return service;
            }
            service = this.initService(serviceId, isAutoReturn, key, mapKey);
            return service;
        }
        catch (Exception e) {
            throw new ServiceLocatorException("Unknown Error", (Throwable)e);
        }
    }
    
    protected synchronized MiddlewareService initService(final String serviceId, final boolean isAutoReturn, final ServiceRouteKey key, final String mapKey) throws Exception {
        MiddlewareService service = null;
        service = this.middleWareServiceMap.get(mapKey);
        if (service != null) {
            return service;
        }
        RemoteServiceLocator.logger.debug((Object)"Service is being instantiated for the first time.");
        final ServiceRouteDefinition servRouteDef = this.getServiceRouteDefinition(key);
        RemoteServiceLocator.logger.debug((Object)("Lookup - " + serviceId + ", " + servRouteDef.getRemoteHost() + ":" + servRouteDef.getRemotePort() + " - " + servRouteDef.getRemoteLookup()));
        final StopWatch sw = (StopWatch)new Log4JStopWatch("RemoteServiceLocator-getService-New-" + serviceId);
        sw.start();
        final Object mwService = this.createMiddlewareService(servRouteDef);
        final InvocationHandler handler = new DynamicRemoteProxyInvocationHandler(mwService, servRouteDef);
        final String serviceInterface = servRouteDef.getRemoteLookup().split("\\!")[1];
        service = (MiddlewareService)Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { Class.forName(serviceInterface), Closeable.class }, handler);
        this.middleWareServiceMap.put(mapKey, service);
        sw.stop();
        return service;
    }
    
    private Object createMiddlewareService(final ServiceRouteDefinition servRouteDef) {
        final String serviceId = servRouteDef.getServiceId();
        InitialContext context = null;
        String host = servRouteDef.getRemoteHost().trim();
        host = ("localhost".equals(host) ? "127.0.0.1" : host);
        final boolean isLocal = this.isLocal(host, servRouteDef.getRemotePort().trim());
        final StopWatch sw = (StopWatch)new Log4JStopWatch("RemoteServiceLocator-createMiddlewareService-Ctx-" + serviceId);
        sw.start();
        if (isLocal) {
            context = this.initContextForLocalUrl();
        }
        else {
            context = this.initContextForUrl(servRouteDef.getRemoteHost().trim(), servRouteDef.getRemotePort().trim());
        }
        sw.stop();
        Object lookupResult = null;
        final String lookupUrl = "ejb:" + servRouteDef.getRemoteLookup();
        try {
            final StopWatch sw2 = (StopWatch)new Log4JStopWatch("RemoteServiceLocator-createMiddlewareService-Lookup-" + serviceId);
            sw2.start();
            try {
                lookupResult = context.lookup(lookupUrl);
            }
            catch (NamingException ne2) {
                lookupResult = context.lookup(lookupUrl);
            }
            sw2.stop();
            if (!isLocal) {
                this.closeContext(context);
            }
        }
        catch (NamingException ne) {
            throw new ServiceLocatorException("Unable to find the service", (Throwable)ne);
        }
        catch (Exception e) {
            throw new ServiceLocatorException("Unknown Error", (Throwable)e);
        }
        return lookupResult;
    }
    
    private void closeContext(final InitialContext context) {
        try {
            context.close();
        }
        catch (Exception ex) {}
    }
    
    public void addPartitionToMap(final Integer contextId, final Integer partitionId) {
    }
    
    static {
        RemoteServiceLocator.logger = Logger.getLogger((Class)RemoteServiceLocator.class);
    }
    
    public class DynamicRemoteProxyInvocationHandler implements InvocationHandler
    {
        private Object target;
        private ServiceRouteDefinition servRouteDef;
        
        public DynamicRemoteProxyInvocationHandler(final Object target, final ServiceRouteDefinition servRouteDef) {
            this.target = target;
            this.servRouteDef = servRouteDef;
        }
        
        @Override
        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
            final String tagName = ExecutionContext.getCurrent().getContextValues().get(HeaderParam.REQUEST_ID.getParamName()) + "-" + this.servRouteDef.getRemoteLookup() + "." + method.getName() + "-" + this.servRouteDef.getRemoteHost();
            final StopWatch sw = (StopWatch)new Log4JStopWatch(tagName);
            Object returnValue = null;
            try {
                try {
                    sw.start();
                    returnValue = method.invoke(this.target, args);
                    sw.stop();
                    RemoteServiceLocator.logger.debug((Object)("Client side response " + this.servRouteDef.getRemoteHost() + "- " + sw.getTag() + ":" + sw.getElapsedTime()));
                    return returnValue;
                }
                catch (InvocationTargetException t) {
                    final String targetExpName = t.getTargetException().getClass().getName();
                    if (targetExpName.equals(EJBException.class.getName()) || targetExpName.equals(IllegalStateException.class.getName())) {
                        this.target = RemoteServiceLocator.this.createMiddlewareService(this.servRouteDef);
                        sw.start();
                        returnValue = method.invoke(this.target, args);
                        sw.stop();
                        RemoteServiceLocator.logger.debug((Object)("Client side response " + this.servRouteDef.getRemoteHost() + "- " + sw.getTag() + ":" + sw.getElapsedTime()));
                        return returnValue;
                    }
                    throw t;
                }
            }
            catch (Exception t2) {
                throw t2;
            }
        }
    }
}
