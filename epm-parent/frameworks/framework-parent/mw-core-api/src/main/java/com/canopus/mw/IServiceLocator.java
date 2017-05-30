package com.canopus.mw;

public interface IServiceLocator
{
    MiddlewareService getService(final String p0);
    
    MiddlewareService borrowService(final String p0);
    
    void returnService(final MiddlewareService p0);
    
    MiddlewareService getConsumerService(final String p0);
    
    void addPartitionToMap(final Integer p0, final Integer p1);
}
