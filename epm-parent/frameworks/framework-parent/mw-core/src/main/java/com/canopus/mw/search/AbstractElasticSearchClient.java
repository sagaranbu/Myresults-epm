package com.canopus.mw.search;

import org.elasticsearch.client.*;
import java.util.*;
import org.elasticsearch.index.query.*;
import com.canopus.mw.audit.*;
import com.canopus.mw.dto.param.*;
import org.elasticsearch.action.search.*;

public abstract class AbstractElasticSearchClient<E> implements SearchClient<E>
{
    static final String TIMESTAMP = "@timestamp";
    private static final String DATA_FIELD;
    private static final int DEFAULT_SIZE = 10;
    private final Client client;
    private BoolQueryBuilder mustQueryBuilder;
    private BoolQueryBuilder statusQueryBuilder;
    private BoolQueryBuilder customQueryBuilder;
    private int entrySize;
    private int entryNo;
    
    AbstractElasticSearchClient(final Client client) {
        this.client = client;
        this.mustQueryBuilder = null;
        this.customQueryBuilder = null;
        this.statusQueryBuilder = null;
        this.entrySize = 10;
        this.entryNo = 0;
    }
    
    public SearchClient<E> dateRange(final Date fromDate, final Date toDate) {
        final RangeQueryBuilder rqb = QueryBuilders.rangeQuery("@timestamp");
        rqb.from((Object)((fromDate == null) ? new Date(0L) : fromDate));
        rqb.to((Object)((toDate == null) ? new Date() : toDate));
        this.setQueriesIntoQueryBuilder((QueryBuilder)rqb);
        return (SearchClient<E>)this;
    }
    
    private void setQueriesIntoQueryBuilder(final QueryBuilder qb) {
        if (this.mustQueryBuilder == null) {
            this.mustQueryBuilder = QueryBuilders.boolQuery();
        }
        this.mustQueryBuilder.must(qb);
    }
    
    public SearchClient<E> searchText(final String text) {
        if (text != null) {
            this.setQueriesIntoQueryBuilder((QueryBuilder)QueryBuilders.multiMatchQuery((Object)text, new String[] { AbstractElasticSearchClient.DATA_FIELD + '*' }).type((Object)MatchQueryBuilder.Type.PHRASE_PREFIX));
        }
        return (SearchClient<E>)this;
    }
    
    public SearchClient<E> searchCustom(final String key, final Object keyword) {
        if (key != null && keyword != null) {
            if (this.customQueryBuilder == null) {
                this.customQueryBuilder = QueryBuilders.boolQuery();
            }
            this.customQueryBuilder.should((QueryBuilder)QueryBuilders.matchQuery(key, keyword).type(MatchQueryBuilder.Type.PHRASE_PREFIX));
        }
        return (SearchClient<E>)this;
    }
    
    public SearchClient<E> searchStatus(final AuditStatus status) {
        if (status != null) {
            if (this.statusQueryBuilder == null) {
                this.statusQueryBuilder = QueryBuilders.boolQuery();
            }
            this.statusQueryBuilder.should((QueryBuilder)QueryBuilders.matchQuery(AuditLogEnum.STATUS.name(), (Object)status.name()).type(MatchQueryBuilder.Type.PHRASE));
        }
        return (SearchClient<E>)this;
    }
    
    public SearchClient<E> searchOperation(final AuditOperation operation) {
        if (operation != null) {
            this.setQueriesIntoQueryBuilder((QueryBuilder)QueryBuilders.matchQuery(AuditLogEnum.OPERATION_ID.name(), (Object)operation.name()).type(MatchQueryBuilder.Type.PHRASE));
        }
        return (SearchClient<E>)this;
    }
    
    public SearchClient<E> searchCategory(final AuditCategory category) {
        if (category != null) {
            this.setQueriesIntoQueryBuilder((QueryBuilder)QueryBuilders.matchQuery(AuditLogEnum.CATEGORY.name(), (Object)category.name()).type(MatchQueryBuilder.Type.PHRASE));
        }
        return (SearchClient<E>)this;
    }
    
    public <V> SearchClient<E> searchOrg(final V orgId) {
        if (orgId != null) {
            this.setQueriesIntoQueryBuilder((QueryBuilder)QueryBuilders.matchQuery(HeaderParam.TEMP_ORG_NAME.getParamName(), (Object)orgId).type(MatchQueryBuilder.Type.PHRASE_PREFIX));
        }
        return (SearchClient<E>)this;
    }
    
    public <W> SearchClient<E> searchUser(final W userId) {
        if (userId != null) {
            this.setQueriesIntoQueryBuilder((QueryBuilder)QueryBuilders.matchQuery(HeaderParam.USER_NAME.getParamName(), (Object)userId).type(MatchQueryBuilder.Type.PHRASE_PREFIX));
        }
        return (SearchClient<E>)this;
    }
    
    public SearchClient<E> searchEntryFrom(final int entryNo) {
        this.entryNo = entryNo;
        return (SearchClient<E>)this;
    }
    
    public SearchClient<E> entrySize(final int entrySize) {
        this.entrySize = entrySize;
        return (SearchClient<E>)this;
    }
    
    protected final SearchResponse getSearchResponse(final String tenantId) {
        final SearchRequestBuilder srb = this.client.prepareSearch(new String[] { "*" }).setTypes(new String[] { tenantId }).setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
        this.setQuery(srb);
        return (SearchResponse)srb.setFrom(this.entryNo).setSize(this.entrySize).setExplain(false).execute().actionGet();
    }
    
    private void setQuery(final SearchRequestBuilder srb) {
        final BoolQueryBuilder bqb = QueryBuilders.boolQuery();
        if (this.mustQueryBuilder != null) {
            bqb.must((QueryBuilder)this.mustQueryBuilder);
        }
        if (this.customQueryBuilder != null) {
            bqb.must((QueryBuilder)this.customQueryBuilder.minimumNumberShouldMatch(1));
        }
        if (this.statusQueryBuilder != null) {
            bqb.must((QueryBuilder)this.statusQueryBuilder.minimumNumberShouldMatch(1));
        }
        if (bqb.hasClauses()) {
            srb.setQuery((QueryBuilder)bqb);
        }
    }
    
    static {
        DATA_FIELD = AuditLogEnum.DATA.name() + '.';
    }
}
