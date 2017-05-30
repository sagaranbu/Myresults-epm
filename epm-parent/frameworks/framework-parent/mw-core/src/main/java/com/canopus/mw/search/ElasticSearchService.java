package com.canopus.mw.search;

import com.canopus.mw.audit.*;
import com.canopus.mw.dto.*;
import com.canopus.mw.dto.param.*;
import java.util.*;

public class ElasticSearchService<E> implements SearchService<E>
{
    private final AbstractElasticSearchClientFactory<E> factory;
    
    public ElasticSearchService(final AbstractElasticSearchClientFactory<E> factory) {
        this.factory = factory;
    }
    
    public <V, W> E getSearchResults(final SearchParams<V, W> searchParams) {
        final SearchClient<E> searchClient = (SearchClient<E>)this.factory.getInstance().dateRange(searchParams.getFromDate(), searchParams.getToDate()).entrySize(searchParams.getEntrySize()).searchEntryFrom(searchParams.getStartNum()).searchOperation(searchParams.getOperation()).searchCategory(searchParams.getCategory()).searchUser(searchParams.getUserId()).searchOrg(searchParams.getOrgId()).searchText(searchParams.getDataChunk());
        for (final Map.Entry<String, List<Object>> entry : searchParams.getCustomSearchMap().entrySet()) {
            for (final Object value : entry.getValue()) {
                searchClient.searchCustom((String)entry.getKey(), value);
            }
        }
        for (final AuditStatus status : searchParams.getStatusSearchSet()) {
            searchClient.searchStatus(status);
        }
        return (E)searchClient.execute(ExecutionContext.getCurrent().getContextValues().get(HeaderParam.TENANT_ID.getParamName()).toString());
    }
}
