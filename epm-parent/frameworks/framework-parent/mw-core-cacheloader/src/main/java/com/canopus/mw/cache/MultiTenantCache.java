package com.canopus.mw.cache;

import com.canopus.mw.dto.*;

public class MultiTenantCache<K, T> implements Cache<K, T>
{
    private Cache<String, T> cache;
    
    public MultiTenantCache(final Cache<String, T> cache) {
        this.cache = cache;
    }
    
    @Override
    public void put(final K key, final T obj) {
        final Integer tenantId = ExecutionContext.getTenantId();
        final String newKey = tenantId.toString() + ":" + key;
        this.cache.put(newKey, obj);
    }
    
    @Override
    public T get(final K key, final CacheLoader<K, T> loader) {
        final MultiTenantCacheLoader<K, T> cacheLoader = new MultiTenantCacheLoader<K, T>(key, loader);
        final Integer tenantId = ExecutionContext.getTenantId();
        final String newKey = tenantId.toString() + ":" + key;
        return this.cache.get(newKey, cacheLoader);
    }
    
    @Override
    public T getIfPresent(final K key) {
        final Integer tenantId = ExecutionContext.getTenantId();
        final String newKey = tenantId.toString() + ":" + key;
        final T obj = this.cache.getIfPresent(newKey);
        return obj;
    }
    
    @Override
    public void remove(final K key) {
        final Integer tenantId = ExecutionContext.getTenantId();
        final String newKey = tenantId.toString() + ":" + key;
        this.cache.remove(newKey);
    }
    
    @Override
    public void clear() {
        this.cache.clear();
    }
    
    public static class MultiTenantCacheLoader<K, T> implements CacheLoader<String, T>
    {
        private CacheLoader<K, T> loader;
        private K key;
        
        public MultiTenantCacheLoader(final K key, final CacheLoader<K, T> loader) {
            this.loader = null;
            this.key = null;
            this.loader = loader;
            this.key = key;
        }
        
        @Override
        public T load(final String strKey) {
            return this.loader.load(this.key);
        }
    }
}
