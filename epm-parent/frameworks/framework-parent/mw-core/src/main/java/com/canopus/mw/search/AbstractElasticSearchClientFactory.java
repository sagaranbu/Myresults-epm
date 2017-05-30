package com.canopus.mw.search;

import org.elasticsearch.common.transport.*;
import java.util.*;
import org.elasticsearch.client.*;
import org.elasticsearch.client.transport.*;

public abstract class AbstractElasticSearchClientFactory<E>
{
    private final TransportAddress[] elasticSearchServerAddresses;
    
    protected AbstractElasticSearchClientFactory(final Set<TransportAddress> elasticSearchServerAddressSet) {
        this.elasticSearchServerAddresses = elasticSearchServerAddressSet.toArray(new TransportAddress[0]);
    }
    
    protected final Client getClientInstance() {
        return (Client)new TransportClient().addTransportAddresses(this.elasticSearchServerAddresses);
    }
    
    public abstract SearchClient<E> getInstance();
}
