package com.canopus.dac.hibernate;

import com.googlecode.genericdao.dao.hibernate.*;
import com.canopus.dac.utils.*;
import java.io.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;

public class GenericHibernateDao extends GeneralDAOImpl
{
    public int count(final ISearch search) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.count(search);
    }
    
    public <T> T find(final Class<T> type, final Serializable id) {
        final T result = (T)super.find((Class)type, id);
        HibernateDAOHelper.validateEntityTenantId(result);
        return result;
    }
    
    public <T> T[] find(final Class<T> type, final Serializable... ids) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        final T[] entities = (T[])super.find((Class)type, ids);
        return entities;
    }
    
    public <T> List<T> findAll(final Class<T> type) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (List<T>)super.findAll((Class)type);
    }
    
    public void flush() {
        super.flush();
    }
    
    public <T> T getReference(final Class<T> type, final Serializable id) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T)super.getReference((Class)type, id);
    }
    
    public <T> T[] getReferences(final Class<T> type, final Serializable... ids) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T[])super.getReferences((Class)type, ids);
    }
    
    public boolean isAttached(final Object entity) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.isAttached(entity);
    }
    
    public void refresh(final Object... entities) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        super.refresh(entities);
    }
    
    public boolean remove(final Object entity) {
        HibernateDAOHelper.validateEntityTenantId(entity);
        return super.remove(entity);
    }
    
    public void remove(final Object... entities) {
        HibernateDAOHelper.validateEntityTenantId(entities);
        super.remove(entities);
    }
    
    public boolean removeById(final Class<?> type, final Serializable id) {
        if (!ExecutionContext.getCurrent().isCrossTenant()) {
            final Object result = this.find(type, id);
            HibernateDAOHelper.validateEntityTenantId(result);
        }
        return super.removeById((Class)type, id);
    }
    
    public void removeByIds(final Class<?> type, final Serializable... ids) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        super.removeByIds((Class)type, ids);
    }
    
    public boolean save(final Object entity) {
        HibernateDAOHelper.setSystemData(entity);
        return super.save(entity);
    }
    
    public boolean[] save(final Object... entities) {
        HibernateDAOHelper.setSystemData(entities);
        return super.save(entities);
    }
    
    public List search(final ISearch search) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.search(search);
    }
    
    public SearchResult searchAndCount(final ISearch search) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.searchAndCount(search);
    }
    
    public Object searchUnique(final ISearch search) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.searchUnique(search);
    }
    
    public Filter getFilterFromExample(final Object example) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.getFilterFromExample(example);
    }
    
    public Filter getFilterFromExample(final Object example, final ExampleOptions options) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.getFilterFromExample(example, options);
    }
    
    public <T> T merge(final T entity) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T)super._merge((Object)entity);
    }
}
