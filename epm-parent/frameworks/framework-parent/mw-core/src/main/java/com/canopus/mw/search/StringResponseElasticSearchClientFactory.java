package com.canopus.mw.search;

import java.util.*;
import org.elasticsearch.common.transport.*;

public class StringResponseElasticSearchClientFactory extends AbstractElasticSearchClientFactory<String>
{
    public StringResponseElasticSearchClientFactory(final Set<TransportAddress> elasticSearchServerAddressSet) {
        super(elasticSearchServerAddressSet);
    }
    
    @Override
    public SearchClient<String> getInstance() {
        return (SearchClient<String>)new StringResponseElasticSearchClient(this.getClientInstance());
    }
}
