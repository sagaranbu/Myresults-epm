package com.canopus.dac.hibernate;

import java.io.*;
import com.googlecode.genericdao.dao.hibernate.*;
import com.canopus.dac.utils.*;
import java.util.*;
import com.canopus.mw.dto.*;
import com.googlecode.genericdao.search.*;

public class BaseHibernateDao<T, ID extends Serializable> extends GenericDAOImpl<T, ID>
{
    public int count(final ISearch search) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.count(search);
    }
    
    public T[] find(final Serializable... ids) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T[])super.find(ids);
    }
    
    public T find(final Serializable id) {
        final T result = (T)super.find(id);
        HibernateDAOHelper.validateEntityTenantId(result);
        return result;
    }
    
    public List<T> findAll() {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (List<T>)super.findAll();
    }
    
    public void flush() {
        super.flush();
    }
    
    public Filter getFilterFromExample(final T example, final ExampleOptions options) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.getFilterFromExample(example, options);
    }
    
    public Filter getFilterFromExample(final T example) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.getFilterFromExample(example);
    }
    
    public T getReference(final Serializable id) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T)super.getReference(id);
    }
    
    public T[] getReferences(final Serializable... ids) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T[])super.getReferences(ids);
    }
    
    public boolean isAttached(final T entity) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return super.isAttached(entity);
    }
    
    public void refresh(final T... entities) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        super.refresh(entities);
    }
    
    public void remove(final T... entities) {
        HibernateDAOHelper.validateEntityTenantId((Object[])entities);
        super.remove(entities);
    }
    
    public boolean remove(final T entity) {
        HibernateDAOHelper.validateEntityTenantId(entity);
        return super.remove(entity);
    }
    
    public boolean removeById(final Serializable id) {
        if (!ExecutionContext.getCurrent().isCrossTenant()) {
            final T result = (T)super.find(id);
            HibernateDAOHelper.validateEntityTenantId(result);
        }
        return super.removeById(id);
    }
    
    public void removeByIds(final Serializable... ids) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        super.removeByIds(ids);
    }
    
    public boolean[] save(final T... entities) {
        HibernateDAOHelper.setSystemData((Object[])entities);
        return super.save(entities);
    }
    
    public boolean save(final T entity) {
        HibernateDAOHelper.setSystemData(entity);
        return super.save(entity);
    }
    
    public List<T> search(final ISearch search) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (List<T>)super.search(search);
    }
    
    public SearchResult<T> searchAndCount(final ISearch arg0) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (SearchResult<T>)super.searchAndCount(arg0);
    }
    
    public T searchUnique(final ISearch search) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T)super.searchUnique(search);
    }
    
    public <T> T merge(final T entity) {
        HibernateDAOHelper.setTenantFilter(this.getSession());
        return (T)super._merge((Object)entity);
    }
}
