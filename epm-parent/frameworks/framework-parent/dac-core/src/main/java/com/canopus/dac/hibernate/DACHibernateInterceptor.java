package com.canopus.dac.hibernate;

import java.io.*;
import org.hibernate.type.*;
import org.hibernate.type.Type;

import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import org.hibernate.*;
import java.util.concurrent.*;

public class DACHibernateInterceptor extends EmptyInterceptor
{
    private static Map<String, Integer> tenantFieldIndexMap;
    private static int NON_TENANT_ENTITY_INDEX;
    
    public boolean onSave(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) {
        final int tenantFieldIndex = this.getTenantFieldIndex(entity, propertyNames);
        if (tenantFieldIndex != DACHibernateInterceptor.NON_TENANT_ENTITY_INDEX) {
            state[tenantFieldIndex] = 10;
            return true;
        }
        return false;
    }
    
    public String onPrepareStatement(final String sql) {
        return super.onPrepareStatement(sql);
    }
    
    private int getTenantFieldIndex(final Object entity, final String[] propertyNames) {
        Integer tenantFieldIndex = DACHibernateInterceptor.tenantFieldIndexMap.get(entity.getClass().getCanonicalName());
        if (tenantFieldIndex == null) {
            for (final Field field : entity.getClass().getDeclaredFields()) {
                if (field.getAnnotations() != null) {
                    for (final Annotation annotation : field.getAnnotations()) {
                        int fieldIndex = 0;
                        for (final String propertyName : propertyNames) {
                            if (field.getName().equals(propertyName)) {
                                DACHibernateInterceptor.tenantFieldIndexMap.put(entity.getClass().getCanonicalName(), fieldIndex);
                                return fieldIndex;
                            }
                            ++fieldIndex;
                        }
                    }
                }
            }
            tenantFieldIndex = DACHibernateInterceptor.NON_TENANT_ENTITY_INDEX;
            DACHibernateInterceptor.tenantFieldIndexMap.put(entity.getClass().getCanonicalName(), tenantFieldIndex);
        }
        return tenantFieldIndex;
    }
    
    public void onDelete(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) {
        super.onDelete(entity, id, state, propertyNames, types);
    }
    
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        return super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }
    
    public boolean onLoad(final Object entity, final Serializable id, final Object[] state, final String[] propertyNames, final Type[] types) {
        return super.onLoad(entity, id, state, propertyNames, types);
    }
    
    public void postFlush(final Iterator entities) {
        super.postFlush(entities);
    }
    
    public void preFlush(final Iterator entities) {
        super.preFlush(entities);
    }
    
    public Boolean isTransient(final Object entity) {
        return super.isTransient(entity);
    }
    
    public Object instantiate(final String entityName, final EntityMode entityMode, final Serializable id) {
        return super.instantiate(entityName, entityMode, id);
    }
    
    public int[] findDirty(final Object entity, final Serializable id, final Object[] currentState, final Object[] previousState, final String[] propertyNames, final Type[] types) {
        return super.findDirty(entity, id, currentState, previousState, propertyNames, types);
    }
    
    public String getEntityName(final Object object) {
        return super.getEntityName(object);
    }
    
    public Object getEntity(final String entityName, final Serializable id) {
        return super.getEntity(entityName, id);
    }
    
    public void afterTransactionBegin(final Transaction tx) {
        super.afterTransactionBegin(tx);
    }
    
    public void afterTransactionCompletion(final Transaction tx) {
        super.afterTransactionCompletion(tx);
    }
    
    public void beforeTransactionCompletion(final Transaction tx) {
        super.beforeTransactionCompletion(tx);
    }
    
    public void onCollectionRemove(final Object collection, final Serializable key) throws CallbackException {
        super.onCollectionRemove(collection, key);
    }
    
    public void onCollectionRecreate(final Object collection, final Serializable key) throws CallbackException {
        super.onCollectionRecreate(collection, key);
    }
    
    public void onCollectionUpdate(final Object collection, final Serializable key) throws CallbackException {
        super.onCollectionUpdate(collection, key);
    }
    
    static {
        DACHibernateInterceptor.tenantFieldIndexMap = new ConcurrentHashMap<String, Integer>();
        DACHibernateInterceptor.NON_TENANT_ENTITY_INDEX = -1;
    }
}
