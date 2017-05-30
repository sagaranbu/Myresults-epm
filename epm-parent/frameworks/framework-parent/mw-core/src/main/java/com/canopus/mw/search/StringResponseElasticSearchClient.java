package com.canopus.mw.search;

import org.elasticsearch.client.*;

public class StringResponseElasticSearchClient extends AbstractElasticSearchClient<String>
{
    StringResponseElasticSearchClient(final Client client) {
        super(client);
    }
    
    public String execute(final String tenantId) {
        return this.getSearchResponse(tenantId).toString();
    }
}
