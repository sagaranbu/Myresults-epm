package com.canopus.dac.query.impl;

import com.canopus.dac.query.*;
import com.canopus.dac.hibernate.*;
import org.springframework.beans.factory.annotation.*;
import com.canopus.mw.cache.*;
import com.canopus.dac.query.vo.*;
import com.canopus.mw.dto.*;
import java.util.*;
import com.googlecode.genericdao.search.*;
import org.modelmapper.*;

public class QueryManagerImpl implements QueryManager, CacheLoader<String, QueryMetadata>
{
    @Autowired
    private GenericHibernateDao genericDao;
    private Cache<String, QueryMetadata> cache;
    
    public QueryManagerImpl() {
        this.cache = null;
    }
    
    private Cache<String, QueryMetadata> getCache() {
        return this.cache;
    }
    
    @Override
    public QueryMetadata getQuery(final String key) {
        final QueryMetadata query = (QueryMetadata)this.getCache().get(key, (CacheLoader)this);
        return query;
    }
    
    @Override
    public void execute(final String key, final Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public int executeForInt(final String key, final Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Table executeForTable(final String key, final Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public StringIdentifier getServiceId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public Object executeForObject(final String key, final Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    @Override
    public List<Object> executeForList(final String key, final Map<String, Object> params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public QueryMetadata load(final String key) {
        final Search search = new Search((Class)Query.class);
        search.addFilterEqual("key", (Object)key);
        final Query query = (Query)this.genericDao.searchUnique((ISearch)search);
        final ModelMapper modelMapper = new ModelMapper();
        final QueryMetadata metaData = (QueryMetadata)modelMapper.map((Object)query, (Class)QueryMetadata.class);
        return metaData;
    }
}
